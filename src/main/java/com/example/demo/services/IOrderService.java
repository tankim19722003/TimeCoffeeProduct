package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.responses.OrderResponse;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse updateOrder(OrderDTO orderDTO, int order_id) throws Exception;

    void deleteOrder(int id);

    OrderResponse getOrder(int orderId) throws Exception;
}
