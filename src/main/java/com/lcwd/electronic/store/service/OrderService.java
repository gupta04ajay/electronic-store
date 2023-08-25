package com.lcwd.electronic.store.service;

import java.util.List;

import com.lcwd.electronic.store.dto.CreateOrderRequest;
import com.lcwd.electronic.store.dto.OrderDto;
import com.lcwd.electronic.store.dto.OrderUpdateRequest;
import com.lcwd.electronic.store.dto.PageableResponse;

public interface OrderService {
	//create order
    OrderDto createOrder(CreateOrderRequest orderDto);

    //remove order
    void removeOrder(String orderId);

    //get orders of user
    List<OrderDto> getOrdersOfUser(String userId);

    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);

    OrderDto updateOrder(String orderId, OrderUpdateRequest request);

    //order methods(logic) related to order


}
