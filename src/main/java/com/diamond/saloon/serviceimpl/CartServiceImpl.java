package com.diamond.saloon.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import com.diamond.saloon.dto.AddToCartDto;
import com.diamond.saloon.dto.CartDto;
import com.diamond.saloon.dto.MergeCartDto;
import com.diamond.saloon.exception.BadRequestException;
import com.diamond.saloon.exception.ResourceNotFoundException;
import com.diamond.saloon.mapper.CartMapper;
import com.diamond.saloon.model.Cart;
import com.diamond.saloon.model.CartItem;
import com.diamond.saloon.model.Product;
import com.diamond.saloon.repository.CartRepository;
import com.diamond.saloon.repository.ProductRepository;
import com.diamond.saloon.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Value("${cart.low-stock-threshold}")
	private int LOW_STOCK_THRESHOLD;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private MongoTemplate mongoTemplate;

	private static final int MAX_CART_ITEMS = 10;

	// add products in cart
	@Override
	@Transactional
	public CartDto addToCart(AddToCartDto requestDto) {

		Product product = productRepository.findById(requestDto.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		if (!Boolean.TRUE.equals(product.getAvailable())) {
			throw new BadRequestException("Product currently unavailable");
		}

		if (product.getStockQuantity() <= 0) {
			throw new BadRequestException("Product out of stock");
		}

		if (requestDto.getQuantity() > product.getStockQuantity()) {
			throw new BadRequestException("Insufficient stock available");
		}

		Query query = new Query(Criteria.where("userId").is(requestDto.getUserId()));

		Update update = new Update()
				.setOnInsert("userId", requestDto.getUserId())
				.setOnInsert("items", new ArrayList<>())
				.setOnInsert("totalAmount", 0.0)
				.setOnInsert("createdAt", LocalDateTime.now())
				.set("updatedAt", LocalDateTime.now());

		Cart cart = mongoTemplate.findAndModify(
				query, 
				update,
				FindAndModifyOptions.options().returnNew(true).upsert(true), 
				Cart.class);

		if (cart.getItems() == null) {
			cart.setItems(new ArrayList<>());
		}

		CartItem existingItem = cart.getItems().stream()
				.filter(item -> item.getProductId().equals(product.getProductId()))
				.findFirst()
				.orElse(null);

		if (existingItem != null) {

			int updatedQuantity = existingItem.getQuantity() + requestDto.getQuantity();

			if (updatedQuantity > product.getStockQuantity()) {
				throw new BadRequestException("Insufficient stock available");
			}

			existingItem.setQuantity(updatedQuantity);
			existingItem.setPrice(product.getPrice());

		} else {

			if (cart.getItems().size() >= MAX_CART_ITEMS) {
				throw new BadRequestException("Cart limit exceeded (max 10 items allowed)");
			}

			CartItem item = new CartItem();
			item.setProductId(product.getProductId());
			item.setProductName(product.getProductName());
			item.setQuantity(requestDto.getQuantity());
			item.setPrice(product.getPrice());
			item.setImageUrl(product.getImageUrl());

			cart.getItems().add(item);
		}
		
		revalidateCartState(cart);
		mergeDuplicateProducts(cart);
		
		if (cart.getItems().size() > MAX_CART_ITEMS) {
	        throw new BadRequestException("Cart limit exceeded");
	    }
		calculateTotal(cart);

		if (cart.getCreatedAt() == null) {
			cart.setCreatedAt(LocalDateTime.now());
		}
		
		cart.setUpdatedAt(LocalDateTime.now());
		
		Cart savedCart = cartRepository.save(cart);

		return cartMapper.toDto(savedCart);
	}

	// get user cart
	@Override
	public CartDto getCart(String userId) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseGet(() -> {
					Cart newCart = new Cart();
					newCart.setUserId(userId);
					newCart.setItems(new ArrayList<>());
					newCart.setTotalAmount(0.0);
					newCart.setCreatedAt(LocalDateTime.now());
					newCart.setUpdatedAt(LocalDateTime.now());
					return cartRepository.save(newCart); 
				});
		
		boolean updated = revalidateCartState(cart);
		
		calculateTotal(cart);

		if (updated) {
			cart.setUpdatedAt(LocalDateTime.now());
			cart = cartRepository.save(cart);
		}

		return cartMapper.toDto(cart);
	}
	

	// get cart item count
	@Override
	public int getCartItemCount(String userId) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		return cart.getItems().size();
	}

	// validate cart
	@Override
	public CartDto validateCart(String userId) {

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		if (cart.getItems() == null) {
			cart.setItems(new ArrayList<>());
		}

		boolean updated = revalidateCartState(cart);
		mergeDuplicateProducts(cart);
		calculateTotal(cart);

		if (updated) {
			cart.setUpdatedAt(LocalDateTime.now());
			cart = cartRepository.save(cart);
		}

		return cartMapper.toDto(cart);
	}
	
	
	// merge cart
	@Override
	@Transactional
	public CartDto mergeCart(MergeCartDto dto) {

		String guestUserId = dto.getGuestUserId();
		String userId = dto.getUserId();

		if (guestUserId.equals(userId)) {
			throw new BadRequestException("Guest and user cannot be same");
		}

		Cart guestCart = cartRepository.findByUserId(guestUserId)
				.orElseThrow(() -> new ResourceNotFoundException("Guest cart not found"));

		Cart userCart = cartRepository.findByUserId(userId).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUserId(userId);
			newCart.setItems(new ArrayList<>());
			newCart.setTotalAmount(0.0);
			newCart.setCreatedAt(LocalDateTime.now());
			newCart.setUpdatedAt(LocalDateTime.now());
			return newCart;
		});

		if (guestCart.getItems() == null || guestCart.getItems().isEmpty()) {
			return cartMapper.toDto(userCart);
		}

		Cart mergedCart = new Cart();
		mergedCart.setCartId(userCart.getCartId());
		mergedCart.setUserId(userCart.getUserId());
		mergedCart.setItems(new ArrayList<>(userCart.getItems()));
		mergedCart.setTotalAmount(userCart.getTotalAmount());
		mergedCart.setCreatedAt(userCart.getCreatedAt() != null ? userCart.getCreatedAt() : LocalDateTime.now());
		mergedCart.setUpdatedAt(LocalDateTime.now());

		List<String> productIds = guestCart.getItems().stream()
				.map(CartItem::getProductId)
				.filter(Objects::nonNull)
				.toList();

		Map<String, Product> productMap = productRepository.findByProductIdIn(productIds)
				.stream()
				.collect(Collectors.toMap(Product::getProductId, p -> p, (a, b) -> a));

		for (CartItem guestItem : guestCart.getItems()) {

			if (guestItem.getProductId() == null)
				continue;

			Product product = productMap.get(guestItem.getProductId());

			if (product == null || !Boolean.TRUE.equals(product.getAvailable()) || product.getStockQuantity() <= 0) {
				continue;
			}

			int availableStock = product.getStockQuantity();
			int allowedQuantity = Math.min(guestItem.getQuantity(), availableStock);

			CartItem existingItem = mergedCart.getItems().stream()
					.filter(i -> i.getProductId().equals(product.getProductId())).findFirst().orElse(null);

			if (existingItem != null) {

				int requestedQty = existingItem.getQuantity() + allowedQuantity;
				int newQuantity = Math.min(requestedQty, availableStock);

				existingItem.setQuantity(newQuantity);
				existingItem.setPrice(product.getPrice());

				if (newQuantity < requestedQty) {
					existingItem.setStockMessage("Quantity adjusted during cart merge");
				}

			} else {

				if (mergedCart.getItems().size() >= MAX_CART_ITEMS) {
					throw new BadRequestException("Cart limit exceeded during merge");
				}

				CartItem newItem = new CartItem();
				newItem.setProductId(product.getProductId());
				newItem.setProductName(product.getProductName());
				newItem.setQuantity(allowedQuantity);
				newItem.setPrice(product.getPrice());
				newItem.setImageUrl(product.getImageUrl());

				if (allowedQuantity < guestItem.getQuantity()) {
					newItem.setStockMessage("Quantity adjusted during cart merge");
				}

				mergedCart.getItems().add(newItem);
			}
		}

		mergeDuplicateProducts(mergedCart);

		revalidateCartState(mergedCart);

		if (mergedCart.getItems().size() > MAX_CART_ITEMS) {
			throw new BadRequestException("Cart limit exceeded after merge");
		}

		calculateTotal(mergedCart);

		Cart savedCart = cartRepository.save(mergedCart);

		if (guestCart.getCartId() != null) {
			cartRepository.deleteById(guestCart.getCartId());
		}

		return cartMapper.toDto(savedCart);
	}

	
	// update cart
	@Override
	public CartDto updateCart(String userId, String cartItemId, int quantity) {

		if (cartItemId == null || cartItemId.trim().isEmpty()) {
			throw new BadRequestException("Cart item id is required");
		}

		if (quantity <= 0) {
			throw new BadRequestException("Quantity must be at least 1");
		}

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is empty");
		}

		CartItem cartItem = cart.getItems().stream()
				.filter(item -> cartItemId.equals(item.getCartItemId()))
				.findFirst()
				.orElseThrow(() -> new BadRequestException("Product not found in cart"));

		Product product = productRepository.findById(cartItem.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		// Product unavailable
		if (!Boolean.TRUE.equals(product.getAvailable())) {
			cartItem.setInStock(false);
			cartItem.setStockMessage("Product currently unavailable");
			cart.setUpdatedAt(LocalDateTime.now());
			cartRepository.save(cart);
			return cartMapper.toDto(cart);
		}

		int availableStock = product.getStockQuantity();

		// Out of stock
		if (availableStock <= 0) {
			cartItem.setInStock(false);
			cartItem.setStockMessage("Out of stock");
			cart.setUpdatedAt(LocalDateTime.now());
			cartRepository.save(cart);
			return cartMapper.toDto(cart);
		}

		// Adjust quantity safely
		if (quantity > availableStock) {
			cartItem.setQuantity(availableStock);
			cartItem.setStockMessage("Only " + availableStock + " items available");
		} else {
			cartItem.setQuantity(quantity);
			cartItem.setStockMessage(null);
		}

		cartItem.setPrice(product.getPrice());
		cartItem.setInStock(true);

		mergeDuplicateProducts(cart);

		boolean updated = revalidateCartState(cart);

		calculateTotal(cart);

		if (updated) {
			cart.setUpdatedAt(LocalDateTime.now());
		}

		cartRepository.save(cart);

		return cartMapper.toDto(cart);
	}
	

	// remove product from cart
	@Override
	public void removeProduct(String userId, String cartItemId) {

		if (cartItemId == null || cartItemId.trim().isEmpty()) {
			throw new BadRequestException("Cart item id is required");
		}

		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new BadRequestException("Cart is already empty");
		}

		CartItem itemToRemove = cart.getItems().stream()
				.filter(item -> cartItemId.equals(item.getCartItemId()))
				.findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

		cart.getItems().remove(itemToRemove);

		if (cart.getItems().isEmpty()) {
			cart.setItems(new ArrayList<>());
			cart.setTotalAmount(0.0);
			cart.setUpdatedAt(LocalDateTime.now());
			cartRepository.save(cart);
			return;
		}

		mergeDuplicateProducts(cart);
		revalidateCartState(cart);

		calculateTotal(cart);

		cart.setUpdatedAt(LocalDateTime.now());

		cartRepository.save(cart);
	}

	// clear cart
	@Override
	public void clearCart(String userId) {
		Cart cart = cartRepository.findByUserId(userId)
				.orElseThrow(() -> new ResourceNotFoundException("cart not found"));
		
		if (cart.getItems() == null || cart.getItems().isEmpty()) {
	        return;
	    }

		cart.setItems(new ArrayList<>());
		calculateTotal(cart);
		cart.setUpdatedAt(LocalDateTime.now());

		cartRepository.save(cart);
	}

	
	
	
	// Helper Methods

	// Total amount calculation Method
	private void calculateTotal(Cart cart) {
		double total = cart.getItems().stream()
				.mapToDouble(item -> item.getPrice() * item.getQuantity())
				.sum();

		cart.setTotalAmount(total);
	}

	// Validate method
	private boolean revalidateCartState(Cart cart) {

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			cart.setTotalAmount(0.0);
			return false;
		}

		boolean updated = false;

		List<String> productIds = cart.getItems().stream()
				.map(CartItem::getProductId)
				.toList();

		Map<String, Product> productMap = productRepository.findByProductIdIn(productIds)
				.stream()
				.collect(Collectors.toMap(Product::getProductId, p -> p, (a, b) -> a));

		Iterator<CartItem> iterator = cart.getItems().iterator();

		while (iterator.hasNext()) {

			CartItem item = iterator.next();
			Product product = productMap.get(item.getProductId());

			// Product removed
			if (product == null) {
				item.setInStock(false);
				item.setStockMessage("Product no longer available");
				updated = true;
				continue;
			}

			// Not available
			if (!Boolean.TRUE.equals(product.getAvailable())) {
				item.setInStock(false);
				item.setStockMessage("Product currently unavailable");
				updated = true;
				continue;
			}

			item.setInStock(true);
			String message = null;

			// Price update
			if (Double.compare(item.getPrice(), product.getPrice()) != 0) {
				item.setPrice(product.getPrice());
				message = "Price updated";
				updated = true;
			}

			int availableStock = product.getStockQuantity();

			// Out of stock
			if (availableStock <= 0) {
				item.setInStock(false);
				item.setStockMessage("Out of stock");
				updated = true;
				continue;
			}

			// Quantity adjust
			if (item.getQuantity() > availableStock) {
				item.setQuantity(availableStock);
				message = "Only " + availableStock + " items available";
				updated = true;
			}

			// Low stock
			if (availableStock <= LOW_STOCK_THRESHOLD && message == null) {
				message = "Hurry! Only " + availableStock + " left";
			}

			item.setStockMessage(message);
		}

		calculateTotal(cart);
		return updated;
	}

	
	// duplication safety method
	private void mergeDuplicateProducts(Cart cart) {

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			return;
		}

		Map<String, CartItem> uniqueItems = new LinkedHashMap<>();

		List<String> productIds = cart.getItems().stream()
				.map(CartItem::getProductId)
				.toList();

		Map<String, Product> productMap = productRepository.findByProductIdIn(productIds)
				.stream()
				.collect(Collectors.toMap(Product::getProductId, p -> p, (a, b) -> a));

		for (CartItem item : cart.getItems()) {

			if (item.getProductId() == null) {
				continue;
			}

			Product product = productMap.get(item.getProductId());

			if (product == null || !Boolean.TRUE.equals(product.getAvailable())) {
				uniqueItems.put(item.getProductId(), item);
				continue;
			}

			CartItem existing = uniqueItems.get(item.getProductId());

			if (existing == null) {

				item.setPrice(product.getPrice());
				uniqueItems.put(item.getProductId(), item);

			} else {

				int totalQty = existing.getQuantity() + item.getQuantity();
				int availableStock = product.getStockQuantity();

				if (totalQty > availableStock) {
					existing.setQuantity(availableStock);
					existing.setStockMessage("Only " + availableStock + " items available");
				} else {
					existing.setQuantity(totalQty);
				}

				if (Double.compare(existing.getPrice(), product.getPrice()) != 0) {
					existing.setPrice(product.getPrice());
					existing.setStockMessage("Price updated");
				}
			}
		}

		cart.setItems(new ArrayList<>(uniqueItems.values()));
	}

}


