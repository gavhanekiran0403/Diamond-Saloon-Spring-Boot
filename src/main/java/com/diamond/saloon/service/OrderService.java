package com.diamond.saloon.service;

import java.util.List;  

import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderRequestDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;

public interface OrderService {

	public OrderDto createOrder(OrderRequestDto requestDto);
	
	public List<OrderDto> getUserOrders(String userId);
	
	public OrderDto getOrderById(String orderId);
	
	public OrderDto cancelOrder(String orderId, String userId);
	
	public OrderDto returnOrder(String orderId, ReturnOrderDto returnDto);
	
	public List<OrderDto> getAllOrders();
	
	public OrderDto updateOrderStatus(OrderStatusUpdateDto statusUpdateDto);
	
	public OrderDto approveReturn(String orderId);
	
	public OrderDto refundOrder(String orderId);
	
	public List<OrderDto> getTodayOrders();
}
