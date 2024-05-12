package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderUpdatingDTO;
import com.example.demo.model.Order;
import com.example.demo.responses.OrderHasDetailResponse;
import com.example.demo.responses.OrderResponse;

public interface IOrderService {
    OrderHasDetailResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse updateOrder(OrderUpdatingDTO orderDTO) throws Exception;

    void deleteOrder(int id);

    OrderHasDetailResponse getOrder(int orderId) throws Exception;

    Order findOrderByTableId(int tableId) throws Exception;

    void payOrder(int tableId) throws Exception;
}
