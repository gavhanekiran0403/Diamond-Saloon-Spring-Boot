package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderRequestDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;
import com.diamond.saloon.enums.OrderStatus;
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
import com.diamond.saloon.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public OrderDto createOrder(OrderRequestDto requestDto) {
		
		List<OrderItem> orderItems;
		double totalAmount;
		
		
		// Buy now from product page 
		if(requestDto.getProductId() != null) {
			
			if(requestDto.getQuantity() <= 0) {
				throw new BadRequestException("Quantity must be greater than zero");
			}
			
			Product product = productRepository.findById(requestDto.getProductId())
					.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
			
			OrderItem item = new OrderItem();
			item.setProductId(product.getProductId());
			item.setProductName(product.getProductName());
			item.setPrice(product.getPrice());
			item.setQuantity(requestDto.getQuantity());
			
			orderItems = List.of(item);
			totalAmount = product.getPrice() * requestDto.getQuantity();
			
		}
		
		
		// Buy now from cart
		else if(requestDto.getCartItemId() != null){
			
			Cart cart = cartRepository.findByUserId(requestDto.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
			
			
			CartItem cartItem = cart.getProducts().stream()
					.filter(ci -> ci.getCartItemId().equals(requestDto.getCartItemId()))
					.findFirst()
					.orElseThrow(() -> new BadRequestException("Cart item not found"));
			
			OrderItem item = new OrderItem();
			item.setProductId(cartItem.getProductId());
			item.setProductName(cartItem.getProductName());
			item.setPrice(cartItem.getPrice());
			item.setQuantity(cartItem.getQuantity());
			
			orderItems = List.of(item);
			totalAmount = cartItem.getPrice() * cartItem.getQuantity();
			
			cart.getProducts().remove(cartItem);
			
			cart.setTotalAmount(
					cart.getProducts().stream()
					.mapToDouble(i -> i.getPrice() * i.getQuantity())
					.sum()
			);
			
			cartRepository.save(cart);
					
		}
		
		
		// Full cart checkout
		else if(requestDto.isCartCheckout()){
			
			Cart cart = cartRepository.findByUserId(requestDto.getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
			
			if(cart.getProducts().isEmpty()) {
				throw new BadRequestException("Cart is empty");
			}

			orderItems = cart.getProducts().stream().map(ci -> {
				OrderItem item = new OrderItem();
				item.setProductId(ci.getProductId());
				item.setProductName(ci.getProductName());
				item.setPrice(ci.getPrice());
				item.setQuantity(ci.getQuantity());
				return item;
			}).toList();
			
			totalAmount = orderItems.stream()
						.mapToDouble(i -> i.getPrice() * i.getQuantity())
						.sum();
			 
			cart.getProducts().clear();
			cart.setTotalAmount(0);
				
			cartRepository.save(cart);
			
		}
		else {
			throw new BadRequestException("Invalid checkout request");
		}
		
		
		
		Address address = addressRepository.findById(requestDto.getAddressId())
				.orElseThrow(() -> new ResourceNotFoundException("Address not found"));
		
		// Create order
		Order order = new Order();
		order.setUserId(requestDto.getUserId());
		order.setOrderStatus(OrderStatus.PLACED);
		order.setPaymentStatus(PaymentStatus.PENDING);
		order.setOrderAt(LocalDateTime.now());
		order.setDeliveryAddress(orderSnapshot(address));
		order.setItems(orderItems);
		order.setTotalAmount(totalAmount);
		
		Order savedOrder = orderRepository.save(order);

		savedOrder.getItems().forEach(
				i ->i.setOrderId(savedOrder.getOrderId())
				);
		
		orderRepository.save(savedOrder);
		
		savedOrder.setPaymentStatus(PaymentStatus.SUCCESS);
		orderRepository.save(savedOrder);

		return orderMapper.toDto(savedOrder);
	}

	// get all Orders of specific user
	@Override
	public List<OrderDto> getUserOrders(String userId) {

		return orderRepository.findByUserId(userId)
				.stream()
				.map(orderMapper::toDto)
				.toList();

	}

	
	// get order by id
	@Override
	public OrderDto getOrderById(String orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		return orderMapper.toDto(order);
	}

	
	// cancel order
	@Override
	public OrderDto cancelOrder(String orderId, String userId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (!order.getUserId().equals(userId)) {
			throw new BadRequestException("You are not allowed to cancel this order");
		}

		if (order.getOrderStatus() == OrderStatus.CANCELLED) {
			throw new BadRequestException("Order already cancelled");
		}

		if (order.getOrderStatus() == OrderStatus.SHIPPED || order.getOrderStatus() == OrderStatus.OUT_FOR_DELIVERY
				|| order.getOrderStatus() == OrderStatus.DELIVERED) {
			throw new BadRequestException("Order cannot be cancelled  after shipping");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderMapper.toDto(orderRepository.save(order));
	}

	
	
	// Return order 
	@Override
	public OrderDto returnOrder(String orderId, ReturnOrderDto returnDto) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (!order.getUserId().equals(returnDto.getUserId())) {
			throw new BadRequestException("You are not allowed to return this order");
		}

		if (order.getOrderStatus() != OrderStatus.DELIVERED) {
			throw new BadRequestException("Return allowed only after delivery");
		}

		
		if (order.getOrderStatus() == OrderStatus.RETURN_REQUESTED
				|| order.getOrderStatus() == OrderStatus.RETURN_APPROVED
				|| order.getOrderStatus() == OrderStatus.REFUNDED) {
			throw new BadRequestException("Return already requested");
		}
		

		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);

		return orderMapper.toDto(orderRepository.save(order));
	}
	
	
	
	
	// Admin 
	// get all orders
	@Override
	public List<OrderDto> getAllOrders() {
		
		return orderRepository.findAll()
				.stream()
				.map(orderMapper :: toDto)
				.toList();
	}
	
	
	// update order status
	@Override
	public OrderDto updateOrderStatus(OrderStatusUpdateDto statusUpdateDto) {

		Order order = orderRepository.findById(statusUpdateDto.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		OrderStatus current = order.getOrderStatus();
		OrderStatus next = statusUpdateDto.getStatus();

		if (next == OrderStatus.DELIVERED) {
			order.setDeliveredAt(LocalDateTime.now());
		}
		
		
		if (current == OrderStatus.DELIVERED
				&& !(next == OrderStatus.RETURN_APPROVED || next == OrderStatus.REFUNDED)) {

			throw new BadRequestException("Order already delivered");
		}

		if (!isValidTransition(current, next)) {
			throw new BadRequestException("Invalid order status transition " + current + " -> " + next);
		}

		order.setOrderStatus(next);

		return orderMapper.toDto(orderRepository.save(order));
	}
	

	// Approve return
	@Override
	public OrderDto approveReturn(String orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getOrderStatus() != OrderStatus.RETURN_REQUESTED) {
			throw new BadRequestException("Return not in requested state");
		}

		order.setOrderStatus(OrderStatus.RETURN_APPROVED);

		return orderMapper.toDto(orderRepository.save(order));
	}

	
	// Refund order amount
	@Override
	public OrderDto refundOrder(String orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		if (order.getOrderStatus() != OrderStatus.RETURN_APPROVED) {
			throw new BadRequestException("Return not allowed");
		}

		order.setOrderStatus(OrderStatus.REFUNDED);
		order.setPaymentStatus(PaymentStatus.REFUNDED);

		return orderMapper.toDto(orderRepository.save(order));
	}

	
	
	
	// Helper method
	private OrderAddress orderSnapshot(Address address) {
		OrderAddress deliveryAddress = new OrderAddress();
		deliveryAddress.setFullName(address.getFullName());
		deliveryAddress.setPhone(address.getPhone());
		deliveryAddress.setHouseNo(address.getHouseNo());
		deliveryAddress.setStreetAddress(address.getStreetAddress());
		deliveryAddress.setCity(address.getCity());
		deliveryAddress.setState(address.getState());
		deliveryAddress.setPincode(address.getPincode());
		deliveryAddress.setLandmark(address.getLandmark());

		return deliveryAddress;
	}

	
	
	// status transition validation method
	private boolean isValidTransition(OrderStatus current, OrderStatus next) {

		return switch (current) {
		case PLACED -> next == OrderStatus.PACKED || next == OrderStatus.CANCELLED;
		case PACKED -> next == OrderStatus.SHIPPED;
		case SHIPPED -> next == OrderStatus.OUT_FOR_DELIVERY;
		case OUT_FOR_DELIVERY -> next == OrderStatus.DELIVERED;
		case DELIVERED -> next == OrderStatus.RETURN_REQUESTED;
		case RETURN_REQUESTED -> next == OrderStatus.RETURN_APPROVED;
		case RETURN_APPROVED -> next == OrderStatus.REFUNDED;

		default -> false;
		};
	}



}
