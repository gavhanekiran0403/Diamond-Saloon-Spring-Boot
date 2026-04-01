package com.diamond.saloon.serviceimpl;

import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diamond.saloon.dto.BuyNowRequestDto;
import com.diamond.saloon.dto.CartCheckoutDto;
import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;
import com.diamond.saloon.enums.OrderStatus;
import com.diamond.saloon.enums.PaymentMethod;
import com.diamond.saloon.enums.PaymentStatus;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.OrderMapper;
import com.diamond.saloon.model.Address;
import com.diamond.saloon.model.Cart;
import com.diamond.saloon.model.CartItem;
import com.diamond.saloon.model.Order;
import com.diamond.saloon.model.OrderAddress;
import com.diamond.saloon.model.OrderItem;
import com.diamond.saloon.model.Product;
import com.diamond.saloon.repository.AddressRepository;
import com.diamond.saloon.repository.CartRepository;
import com.diamond.saloon.repository.OrderRepository;
import com.diamond.saloon.repository.ProductRepository;
import com.diamond.saloon.service.CartService;
import com.diamond.saloon.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Value("${order.return.window.days}")
	private int RETURN_WINDOW_DAYS;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CartService cartService;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	// User

	@Transactional
	@Override
	public OrderDto buyNow(String userId, BuyNowRequestDto dto) {
		
		if (dto.getQuantity() <= 0) {
			throw new BadRequestException("Quantity must be at least 1");
		}

		if (dto.getPaymentMethod() == null) {
			throw new BadRequestException("Payment method is required");
		}

		Address address = addressRepository.findByAddressIdAndUserId(dto.getAddressId(), userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));

		Product product = decreaseStockAtomically(dto.getProductId(), dto.getQuantity());
		
		// Create OrderItem
		OrderItem item = new OrderItem();
		item.setProductId(product.getProductId());
		item.setProductName(product.getProductName());
		item.setBrand(product.getBrand());
		item.setImageUrl(product.getImageUrl());
		item.setPrice(product.getPrice());
		item.setQuantity(dto.getQuantity());

		double totalAmount = product.getPrice() * dto.getQuantity();

		// Create Order snapshot
		Order order = new Order();
		order.setOrderNumber(generateOrderNumber());
		order.setUserId(userId);
		order.setItems(List.of(item));
		order.setTotalAmount(totalAmount);
		order.setDeliveryAddress(createAddressSnapshot(address));
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentMethod(dto.getPaymentMethod());
		
		order.setPaymentStatus(
				dto.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY 
				? PaymentStatus.PENDING
				: PaymentStatus.INITIATED);
		
		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		return orderMapper.toDto(orderRepository.save(order));
	}
	

	// CART CHECKOUT
	@Transactional
	@Override
	public OrderDto checkoutCart(String userId, CartCheckoutDto dto) {
		
		cartService.validateCart(userId);

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		
		Set<String> selectedCartItemIds = dto.getCartItemIds() != null ? 
				new HashSet<>(dto.getCartItemIds())
				: Collections.emptySet();

		boolean fullCheckout = selectedCartItemIds.isEmpty();
		
		List<CartItem> itemsToCheckout = cart.getItems().stream()
				.filter(item -> fullCheckout || selectedCartItemIds.contains(item.getCartItemId()))
				.toList();
		
		if (itemsToCheckout.isEmpty()) {
			throw new BadRequestException("No items selected for checkout");
		}
		
		validateCartBeforeCheckout(cart, selectedCartItemIds);
        validateStockBeforeCheckout(itemsToCheckout);
        
        Map<String, Integer> deductedStock = new HashMap<>();
		List<OrderItem> orderItems = new ArrayList<>();
		List<CartItem> itemsToRemove = new ArrayList<>();
		double totalAmount = 0.0;
		
		try {
			for (CartItem cartItem : itemsToCheckout) {

				if (!cartItem.isInStock()) {
					throw new BadRequestException("Some items are unavailable. Please review your cart.");
				}

				Product product = decreaseStockAtomically(
						cartItem.getProductId(),
						cartItem.getQuantity()
					);

				
				deductedStock.merge(
						product.getProductId(),
						cartItem.getQuantity(), 
						Integer::sum
					);
				
				// Create OrderItem snapshot
				OrderItem orderItem = new OrderItem();
				orderItem.setProductId(product.getProductId());
				orderItem.setProductName(product.getProductName());
				orderItem.setBrand(product.getBrand());
				orderItem.setImageUrl(product.getImageUrl());
				orderItem.setPrice(product.getPrice());
				orderItem.setQuantity(cartItem.getQuantity());

				orderItems.add(orderItem);

				totalAmount += product.getPrice() * cartItem.getQuantity();
				itemsToRemove.add(cartItem);
			}

		} catch (Exception ex) {
			
			try {
				for (Map.Entry<String, Integer> entry : deductedStock.entrySet()) {

					Query query = new Query(Criteria.where("_id").is(entry.getKey()));

					Update update = new Update()
							.inc("stockQuantity", entry.getValue())
							.set("available", true)
							.set("updatedAt", LocalDateTime.now());

					mongoTemplate.updateFirst(query, update, Product.class);
				}
				
			} catch (Exception rollbackEx) {
				System.err.println("CRITICAL: Stock rollback failed!");
			}
			
			throw new BadRequestException("Checkout failed. Please try again.");
		}
		

		Address address = addressRepository.findByAddressIdAndUserId(dto.getAddressId(), userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found for this user"));

		// Create Order
		Order order = new Order();
		order.setOrderNumber(generateOrderNumber());
		order.setUserId(userId);
		order.setItems(orderItems);
		order.setTotalAmount(totalAmount);
		order.setDeliveryAddress(createAddressSnapshot(address));
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentMethod(dto.getPaymentMethod());

		order.setPaymentStatus(dto.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY 
				? PaymentStatus.PENDING
				: PaymentStatus.INITIATED);

		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		orderRepository.save(order);

		cart.getItems().removeAll(itemsToRemove);

		double remainingTotal = cart.getItems()
				.stream()
				.mapToDouble(item -> {
					Product product = productRepository.findById(item.getProductId())
							.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
					return product.getPrice() * item.getQuantity();
				}).sum();

		cart.setTotalAmount(remainingTotal);

		cartService.validateCart(userId);

		cartRepository.save(cart);

		return orderMapper.toDto(order);
	}

	

	
	// Get Order By ID
	@Override
	public OrderDto getOrderById(String orderId, String userId) {

		if (orderId == null || orderId.trim().isEmpty()) {
			throw new BadRequestException("Order id is required");
		}

		if (userId == null || userId.trim().isEmpty()) {
			throw new BadRequestException("User id is required");
		}

		Order order = orderRepository.findByOrderIdAndUserId(orderId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		return orderMapper.toDto(order);
	}

	

	// Get Orders By User
	@Override
	public List<OrderDto> getUserOrders(String userId) {

		if (userId == null || userId.trim().isEmpty()) {
			throw new BadRequestException("User id is required");
		}

		List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

		return orders.stream()
				.map(orderMapper::toDto)
				.toList();
	}

	
	// Cancel Order
	@Transactional
	@Override
	public OrderDto cancelOrder(String orderId, String userId) {

		Order order = orderRepository.findByOrderIdAndUserId(orderId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new BadRequestException("Order already cancelled");
		}
		
		validateOrderStatusTransition(order.getOrderStatus(), OrderStatus.CANCELLED);

	    restoreProductStock(order);

	    order.setOrderStatus(OrderStatus.CANCELLED);

	    if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
	        order.setPaymentStatus(PaymentStatus.REFUND_PENDING);
	    } else if (order.getPaymentStatus() == PaymentStatus.INITIATED) {
	        order.setPaymentStatus(PaymentStatus.FAILED);
	    }

	    order.setUpdatedAt(LocalDateTime.now());
		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}

	
	// Return Order
	@Transactional
	@Override
	public OrderDto returnOrder(String orderId, ReturnOrderDto dto) {

		Order order = orderRepository.findByOrderIdAndUserId(orderId, dto.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getOrderStatus() == OrderStatus.RETURN_REQUESTED) {
			throw new BadRequestException("Return already requested");
		}

		validateOrderStatusTransition(order.getOrderStatus(), OrderStatus.RETURN_REQUESTED);

		if (order.getDeliveredAt() == null) {
			throw new BadRequestException("Delivery date not available");
		}

		LocalDateTime returnDeadline = order.getDeliveredAt().plusDays(RETURN_WINDOW_DAYS);

		if (LocalDateTime.now().isAfter(returnDeadline)) {
			throw new BadRequestException("Return window expired");
		}

		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		order.setReturnReason(dto.getReason());

		if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
			order.setPaymentStatus(PaymentStatus.REFUND_PENDING);
		}

		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}

//===============================================================================================================================================================

	// Admin

	// Get All Orders
	@Override
	public List<OrderDto> getAllOrders() {

		List<Order> orders = orderRepository.findAll();

		if (orders.isEmpty()) {
			return new ArrayList<>();
		}

		orders.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));
		
		return orders.stream()
				.map(orderMapper::toDto)
				.toList();
	}
	
	// Get Today Orders
	@Override
	public List<OrderDto> getTodayOrders() {

		LocalDate today = LocalDate.now();

		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.plusDays(1).atStartOfDay();

		List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);

		if (orders.isEmpty()) {
	        return new ArrayList<>();
	    }

	    orders.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

	    return orders.stream()
				.map(orderMapper::toDto)
				.toList();
	}

	
	// Update Order Status
	@Transactional
	@Override
	public OrderDto updateOrderStatus(String orderId, OrderStatusUpdateDto dto) {

		if (orderId == null || orderId.trim().isEmpty()) {
			throw new BadRequestException("Order id is required");
		}

		if (dto == null || dto.getStatus() == null) {
			throw new BadRequestException("Order status is required");
		}
		
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		OrderStatus currentStatus = order.getOrderStatus();
		OrderStatus newStatus = dto.getStatus();

		validateOrderStatusTransition(currentStatus, newStatus);

		order.setOrderStatus(newStatus);

		if (newStatus == OrderStatus.DELIVERED) {
			order.setDeliveredAt(LocalDateTime.now());

			if (order.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
				order.setPaymentStatus(PaymentStatus.SUCCESS);
			}
		}
		
		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}

	
	// Approve Return
	@Transactional
	@Override
	public OrderDto approveReturn(String orderId) {
		
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new BadRequestException("Order id is required");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));
		
		if (order.getOrderStatus() == OrderStatus.RETURN_APPROVED) {
			return orderMapper.toDto(order);
		}

		if (order.getOrderStatus() != OrderStatus.RETURN_REQUESTED) {
			throw new BadRequestException("Return request not found for this order");
		}

		restoreProductStock(order);

		order.setOrderStatus(OrderStatus.RETURN_APPROVED);

		if (order.getPaymentStatus() == PaymentStatus.SUCCESS) {
			order.setPaymentStatus(PaymentStatus.REFUND_PENDING);
		} else if (order.getPaymentStatus() == PaymentStatus.INITIATED) {
			order.setPaymentStatus(PaymentStatus.FAILED);
		}

		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}

	
	// Reject Return
	@Transactional
	@Override
	public OrderDto rejectReturn(String orderId) {
		
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new BadRequestException("Order id is required");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		validateOrderStatusTransition(order.getOrderStatus(), OrderStatus.RETURN_REJECTED);

		order.setOrderStatus(OrderStatus.RETURN_REJECTED);
		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}
	

	// Refund Order
	@Transactional
	@Override
	public OrderDto refundOrder(String orderId) {
		
		if (orderId == null || orderId.trim().isEmpty()) {
			throw new BadRequestException("Order id is required");
		}

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		validateOrderStatusTransition(order.getOrderStatus(), OrderStatus.REFUNDED);

		if (order.getPaymentStatus() != PaymentStatus.REFUND_PENDING) {
			throw new BadRequestException("Refund cannot be processed");
		}

		order.setPaymentStatus(PaymentStatus.REFUNDED);
		order.setOrderStatus(OrderStatus.REFUNDED);
		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);

		return orderMapper.toDto(savedOrder);
	}

	// =============================================================================================================================================================
	// Helper Methods :

	// Address snapshot
	private OrderAddress createAddressSnapshot(Address address) {
		
		if (address == null) {
			throw new BadRequestException("Address is required");
		}
		
		OrderAddress snapshot = new OrderAddress();
		snapshot.setFullName(address.getFullName());
		snapshot.setPhone(address.getPhone());
		snapshot.setHouseNo(address.getHouseNo());
		snapshot.setStreetAddress(address.getStreetAddress());
		snapshot.setCity(address.getCity());
		snapshot.setState(address.getState());
		snapshot.setPincode(address.getPincode());
		snapshot.setLandmark(address.getLandmark());
		
		return snapshot;
	}

	// Generate Order Number
	private String generateOrderNumber() {

		String prefix = "ORD";

		String date = LocalDate.now().toString().replace("-", "");

		String random = UUID.randomUUID().toString().replace("-", "")
				.substring(0, 8)
				.toUpperCase();

		return prefix + "-" + date + "-" + random;
	}

	// Stock deduction
	private Product decreaseStockAtomically(String productId, int quantity) {
		Query query = new Query(
				Criteria.where("_id").is(productId)
				.and("stockQuantity").gte(quantity)
				.and("available").is(true));

		Update update = new Update()
				.inc("stockQuantity", -quantity)
				.set("updatedAt", LocalDateTime.now());;

		Product updatedProduct = mongoTemplate.findAndModify(
				query, 
				update,
				FindAndModifyOptions.options().returnNew(true),
				Product.class);

		if (updatedProduct == null) {
			throw new BadRequestException("Insufficient stock for product: " + productId);
		}

		if (updatedProduct.getStockQuantity() == 0) {
			Query q = new Query(Criteria.where("_id").is(productId));
			Update u = new Update()
					.set("available", false)
					.set("updatedAt", LocalDateTime.now());
			mongoTemplate.updateFirst(q, u, Product.class);
		}

		return updatedProduct;
	}

	//cart validation 
	private void validateCartBeforeCheckout(Cart cart, Set<String> selectedItems) {
		
		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		for (CartItem item : cart.getItems()) {

			if (!selectedItems.isEmpty() && !selectedItems.contains(item.getCartItemId())) {
				continue;
			}

			if (item.getProductId() == null) {
				throw new BadRequestException("Invalid product in cart");
			}

			if (item.getQuantity() <= 0) {
				throw new BadRequestException("Invalid quantity in cart");
			}
		}
	}

	private void validateStockBeforeCheckout(List<CartItem> items) {

		if (items == null || items.isEmpty()) {
			throw new BadRequestException("No items to checkout");
		}
		
	    List<String> productIds = items.stream()
	        .map(CartItem::getProductId)
	        .toList();

	    Map<String, Product> productMap = productRepository
	        .findByProductIdIn(productIds)
	        .stream()
	        .collect(Collectors.toMap(Product::getProductId, p -> p));

		for (CartItem item : items) {

			Product product = productMap.get(item.getProductId());

			if (product == null || !Boolean.TRUE.equals(product.getAvailable())) {
				throw new BadRequestException("Product unavailable: " + item.getProductId());
			}

			if (product.getStockQuantity() < item.getQuantity()) {
				throw new BadRequestException("Only " + product.getStockQuantity() + " items available for " + product.getProductName());
			}
		}
	}

	
	// Order Status validation method
	private boolean isValidTransition(OrderStatus current, OrderStatus next) {

		if (current == next) {
			return false;
		}

		return switch (current) {

		case PENDING_PAYMENT -> 
			next == OrderStatus.PLACED || next == OrderStatus.CANCELLED;

		case PLACED -> 
			next == OrderStatus.PACKED || next == OrderStatus.CANCELLED;

		case PACKED -> 
			next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;

		case SHIPPED -> 
			next == OrderStatus.OUT_FOR_DELIVERY;

		case OUT_FOR_DELIVERY -> 
			next == OrderStatus.DELIVERED;

		case DELIVERED -> 
			next == OrderStatus.RETURN_REQUESTED;

		case RETURN_REQUESTED -> 
			next == OrderStatus.RETURN_APPROVED
			|| next == OrderStatus.RETURN_REJECTED;

		case RETURN_APPROVED -> 
			next == OrderStatus.REFUNDED;

		default -> false;
		};
	}

	private void validateOrderStatusTransition(OrderStatus current, OrderStatus next) {

		if (!isValidTransition(current, next)) {
			throw new BadRequestException("Invalid order status transition from " + current + " to " + next);
		}
	}
	

	@Transactional
	private void restoreProductStock(Order order) {

		if (order.getItems() == null || order.getItems().isEmpty()) {
			return;
		}

		for (OrderItem item : order.getItems()) {

			Query query = new Query(Criteria.where("_id").is(item.getProductId()));

			Update update = new Update()
					.inc("stockQuantity", item.getQuantity())
					.set("available", true)
					.set("updatedAt", LocalDateTime.now());

			Product updatedProduct = mongoTemplate.findAndModify(
					query,
					update,
					FindAndModifyOptions.options().returnNew(true),
					Product.class);

			if (updatedProduct == null) {
				throw new ResourceNotFoundException("Product not found: " + item.getProductId());
			}
		}
	}
	
}


/*
 * 
	// CART CHECKOUT
	@Transactional
	@Override
	public OrderDto checkoutCart(String userId, CartCheckoutDto dto) {
		
		cartService.validateCart(userId);

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
		
		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		
		Set<String> selectedCartItemIds = dto.getCartItemIds() != null ? 
				new HashSet<>(dto.getCartItemIds())
				: Collections.emptySet();

		boolean fullCheckout = selectedCartItemIds.isEmpty();
		
		List<CartItem> itemsToCheckout = cart.getItems().stream()
				.filter(item -> fullCheckout || selectedCartItemIds.contains(item.getCartItemId()))
				.toList();
		
		if (itemsToCheckout.isEmpty()) {
			throw new BadRequestException("No items selected for checkout");
		}
		
		validateCartBeforeCheckout(cart, selectedCartItemIds);
		validateStockBeforeCheckout(itemsToCheckout);

		Map<String, Integer> deductedStock = new HashMap<>();
		List<OrderItem> orderItems = new ArrayList<>();
		List<CartItem> itemsToRemove = new ArrayList<>();
		double totalAmount = 0.0;

		for (CartItem cartItem : itemsToCheckout) {

			if (!cartItem.isInStock()) {
				throw new BadRequestException("Some items are unavailable. Please review your cart.");
			}

			Product product = decreaseStockAtomically(cartItem.getProductId(), cartItem.getQuantity());

			deductedStock.merge(
					product.getProductId(), 
					cartItem.getQuantity(), 
					Integer::sum
				);

			// Create OrderItem snapshot
			OrderItem orderItem = new OrderItem();
			orderItem.setProductId(product.getProductId());
			orderItem.setProductName(product.getProductName());
			orderItem.setBrand(product.getBrand());
			orderItem.setImageUrl(product.getImageUrl());
			orderItem.setPrice(product.getPrice());
			orderItem.setQuantity(cartItem.getQuantity());

			orderItems.add(orderItem);

			totalAmount += product.getPrice() * cartItem.getQuantity();
			itemsToRemove.add(cartItem);
		}

		Address address = addressRepository.findByAddressIdAndUserId(dto.getAddressId(), userId)
				.orElseThrow(() -> new ResourceNotFoundException("Address not found for this user"));

		// Create Order
		Order order = new Order();
		order.setOrderNumber(generateOrderNumber());
		order.setUserId(userId);
		order.setItems(orderItems);
		order.setTotalAmount(totalAmount);
		order.setDeliveryAddress(createAddressSnapshot(address));
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentMethod(dto.getPaymentMethod());

		order.setPaymentStatus(dto.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY 
				? PaymentStatus.PENDING
				: PaymentStatus.INITIATED);

		order.setCreatedAt(LocalDateTime.now());
		order.setUpdatedAt(LocalDateTime.now());

		Order savedOrder =  orderRepository.save(order);
		
		List<String> selectedIds = itemsToCheckout.stream()
	            .map(CartItem::getCartItemId)
	            .toList();

		cart.getItems().removeIf(item -> selectedIds.contains(item.getCartItemId()));

		cartService.validateCart(userId);

		cartRepository.save(cart);

		return orderMapper.toDto(savedOrder);
	}
 * 
 */
