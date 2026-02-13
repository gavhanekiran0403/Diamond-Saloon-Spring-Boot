package com.diamond.saloon.controller;

import java.util.List;  

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderRequestDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;
import com.diamond.saloon.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	
	// placed order
	@PostMapping("/create")
	public OrderDto createOrder(@RequestBody OrderRequestDto requestDto) {
		return orderService.createOrder(requestDto);
	}
	
	
	// get my orders
	@GetMapping("/user/{userId}")
	public List<OrderDto> getUserOrders(@PathVariable String userId){
		return orderService.getUserOrders(userId);
	}
	
	
	// get order details
	@GetMapping("/{orderId}")
	public OrderDto getOrderById(@PathVariable String orderId) {
		return orderService.getOrderById(orderId);
	}
	
	
	// cancel order (Allowed only before shipped)
	@PutMapping("/{orderId}/cancel")
	public OrderDto calcelOrder(@PathVariable String orderId, @RequestParam String userId) {
		return orderService.cancelOrder(orderId, userId);
	}
	
	
	// Return order (After delivery)
	@PutMapping("/return/{orderId}")
	public OrderDto returnOrder(@PathVariable String orderId, @RequestBody ReturnOrderDto returnDto) {
		return orderService.returnOrder(orderId, returnDto);
	}
	
	
	
	
	// Admin Apis
	// get all orders
	@GetMapping("/get-all")
	public List<OrderDto> getOrders(){
		return orderService.getAllOrders();
	} 
	
	// Update order status
	@PutMapping("/status")
	public OrderDto updateStatus(@RequestBody OrderStatusUpdateDto orderStatusUpdateDto) {
		return orderService.updateOrderStatus(orderStatusUpdateDto);
	}
	
	
	// Approved return
	@PutMapping("/approve-return/{orderId}")
	public OrderDto approveReturn(@PathVariable String orderId) {
		return orderService.approveReturn(orderId);
	}
	
	
	// Process refund
	@PutMapping("/refund/{orderId}")
	public OrderDto refundOrder(@PathVariable String orderId) {
		return orderService.refundOrder(orderId);
	}
	
	
	@GetMapping("/today")
	public List<OrderDto> getTodayOrders(){
		return orderService.getTodayOrders();
	}
	
	
}
