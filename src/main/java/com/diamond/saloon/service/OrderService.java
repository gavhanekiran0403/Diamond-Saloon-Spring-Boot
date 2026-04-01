package com.diamond.saloon.service;

import java.util.List;

import com.diamond.saloon.dto.BuyNowRequestDto;
import com.diamond.saloon.dto.CartCheckoutDto;
import com.diamond.saloon.dto.OrderDto;
import com.diamond.saloon.dto.OrderStatusUpdateDto;
import com.diamond.saloon.dto.ReturnOrderDto;

public interface OrderService {

	// user

	public OrderDto buyNow(String userId, BuyNowRequestDto dto);

	public OrderDto checkoutCart(String userId, CartCheckoutDto dto);

	public List<OrderDto> getUserOrders(String userId);

	public OrderDto getOrderById(String orderId, String userId);

	public OrderDto cancelOrder(String orderId, String userId);

	public OrderDto returnOrder(String orderId, ReturnOrderDto dto);

//===========================================================================================================================================================
	// Admin Apis

	public List<OrderDto> getAllOrders();

	public List<OrderDto> getTodayOrders();

	public OrderDto updateOrderStatus(String orderId, OrderStatusUpdateDto dto);

	public OrderDto approveReturn(String orderId);

	public OrderDto rejectReturn(String orderId);

	public OrderDto refundOrder(String orderId);

}
