package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderUpdatingDTO;
import com.example.demo.responses.OrderCreatingResponse;
import com.example.demo.responses.OrderResponse;

public interface IOrderService {
    OrderCreatingResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse updateOrder(OrderUpdatingDTO orderDTO, int order_id) throws Exception;

    void deleteOrder(int id);

    OrderResponse getOrder(int orderId) throws Exception;
}
