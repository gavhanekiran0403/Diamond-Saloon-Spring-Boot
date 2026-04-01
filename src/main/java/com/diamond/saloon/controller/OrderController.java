package com.diamond.saloon.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.common.ApiResponse;
import com.diamond.saloon.common.ApiResponseUtil;
import com.diamond.saloon.dto.BuyNowRequestDto;
import com.diamond.saloon.dto.CartCheckoutDto;
import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;
import com.diamond.saloon.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	// Buy Now
	@PostMapping("/buy-now/{userId}")
	public ResponseEntity<ApiResponse<OrderDto>> buyNow(@PathVariable String userId,
			@Valid @RequestBody BuyNowRequestDto dto, HttpServletRequest request) {

		OrderDto order = orderService.buyNow(userId, dto);
		
		return ApiResponseUtil.success(
				order,
				"Order placed successfully",
				HttpStatus.CREATED,
				request
		);
	}

	
	// Cart Checkout
	@PostMapping("/cart-checkout/{userId}")
	public ResponseEntity<ApiResponse<OrderDto>> checkoutCart(@PathVariable String userId,
			@Valid @RequestBody CartCheckoutDto dto, HttpServletRequest request) {

		OrderDto order = orderService.checkoutCart(userId, dto);
		
		return ApiResponseUtil.success(
				order,
				"Cart checkout successful",
				HttpStatus.CREATED,
				request
		);
	}
	

	// Get My Orders
	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getUserOrders(
			@PathVariable String userId, HttpServletRequest request) {

		List<OrderDto> orders = orderService.getUserOrders(userId);
		
		return ApiResponseUtil.success(
				orders,
				"User orders fetched successfully",
				HttpStatus.OK,
				request
		);
	}
	

	// Get Order By Id (get order details)
	@GetMapping("/{orderId}")
	public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable String orderId, 
			@RequestParam String userId, HttpServletRequest request) {

		OrderDto order = orderService.getOrderById(orderId, userId);
		
		return ApiResponseUtil.success(
				order,
				"Order fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	// Cancel Order
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(@PathVariable String orderId,
	        @RequestParam String userId, HttpServletRequest request) {

	    OrderDto order = orderService.cancelOrder(orderId, userId);
	    
	    return ApiResponseUtil.success(
				order,
				"Order cancelled successfully",
				HttpStatus.OK,
				request
		);
	}
	
	
	// Return Order
	@PutMapping("/{orderId}/return")
	public ResponseEntity<ApiResponse<OrderDto>> returnOrder(
	        @PathVariable String orderId,
	        @Valid @RequestBody ReturnOrderDto dto,
	        HttpServletRequest request) {

	    OrderDto order = orderService.returnOrder(orderId, dto);
	    
	    return ApiResponseUtil.success(
				order,
				"Return request submitted successfully",
				HttpStatus.OK,
				request
		);
	}

//===============================================================================================================================================================	

	// Admin APIs

	// Get All Orders
	@GetMapping
	public ResponseEntity<ApiResponse<List<OrderDto>>> getAllOrders(HttpServletRequest request) {

		List<OrderDto> orders = orderService.getAllOrders();
		
		return ApiResponseUtil.success(
				orders,
				"Orders fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Get Today's Orders
	@GetMapping("/today")
	public ResponseEntity<ApiResponse<List<OrderDto>>> getTodayOrders(HttpServletRequest request) {

		List<OrderDto> orders = orderService.getTodayOrders();
		
		return ApiResponseUtil.success(
				orders,
				"Today's orders fetched successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Update Order Status
	@PutMapping("/{orderId}/status")
	public ResponseEntity<ApiResponse<OrderDto>> updateOrderStatus(@PathVariable String orderId,
			@Valid @RequestBody OrderStatusUpdateDto dto, HttpServletRequest request) {

		OrderDto order = orderService.updateOrderStatus(orderId, dto);

		return ApiResponseUtil.success(
				order,
				"Order status updated successfully",
				HttpStatus.OK,
				request
		);
	}


	// Approve Return
	@PutMapping("/{orderId}/approve-return")
	public ResponseEntity<ApiResponse<OrderDto>> approveReturn(
			@PathVariable String orderId, HttpServletRequest request) {

		OrderDto order = orderService.approveReturn(orderId);
		
		return ApiResponseUtil.success(
				order,
				"Return approved successfully",
				HttpStatus.OK,
				request
		);
	}


	// Reject Return
	@PutMapping("/{orderId}/reject-return")
	public ResponseEntity<ApiResponse<OrderDto>> rejectReturn(
			@PathVariable String orderId, HttpServletRequest request) {

		OrderDto order = orderService.rejectReturn(orderId);
		
		return ApiResponseUtil.success(
				order,
				"Return rejected successfully",
				HttpStatus.OK,
				request
		);
	}

	
	// Refund Order
	@PutMapping("/{orderId}/refund")
	public ResponseEntity<ApiResponse<OrderDto>> refundOrder(
			@PathVariable String orderId, HttpServletRequest request) {

		OrderDto order = orderService.refundOrder(orderId);
		
		return ApiResponseUtil.success(
				order,
				"Refund processed successfully",
				HttpStatus.OK,
				request
		);
	}

}

