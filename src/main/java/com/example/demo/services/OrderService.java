package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.model.Order;
import com.example.demo.model.Tables;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.TableRepository;
import com.example.demo.responses.OrderResponse;
import com.example.demo.responses.TableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        Tables tables = tableRepository.findById(orderDTO.getTableId()).orElseThrow(
                () -> new Exception("table does not exist")
        );


    }

    @Override
    public OrderResponse updateOrder(OrderDTO orderDTO, int orderId) throws Exception {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
                () -> new Exception("order does not exist")
        );

        Tables table = tableRepository.findById(orderDTO.getTableId())
                .orElseThrow(() -> new Exception("Table does not exist"));
        existingOrder.setTable(table);

        OrderResponse orderResponse = Order.toOrderResponse(existingOrder);

        return orderResponse;
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponse getOrder(int orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new Exception("order does not exist")
        );

        OrderResponse orderResponse = Order.toOrderResponse(order);
        return orderResponse;
    }
}
