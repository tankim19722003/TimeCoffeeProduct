package com.example.demo.services;

import com.example.demo.dtos.OrderDTO;
import com.example.demo.dtos.OrderUpdatingDTO;
import com.example.demo.dtos.ProductOrder;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.model.Tables;
import com.example.demo.repositories.OrderDetailRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.TableRepository;
import com.example.demo.responses.OrderHasDetailResponse;
import com.example.demo.responses.OrderDetailInOrderResponse;
import com.example.demo.responses.OrderResponse;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;

//    @Override
//    public Order findOrderByTableId(int tableId) {
//        return null;
//    }

    private final TableRepository tableRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderHasDetailResponse createOrder(OrderDTO orderDTO) throws Exception {
        Tables tables = tableRepository.findById(orderDTO.getTableId()).orElseThrow(
                () -> new Exception("table does not exist")
        );
        if (tables.isStatus()) {
            throw new Exception("table is not empty");
        }
        // set status table is occupied
        tables.setStatus(true);
        tableRepository.save(tables);

        Order order = Order.builder()
                .orderDate(new Date(System.currentTimeMillis()))
                .totalMoney(0)
                .isPaid(false)
                .table(tables)
                .build();

        Order orderSaved = orderRepository.save(order);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        int totalMoney = 0;
        for (ProductOrder productOrder : orderDTO.getProducts()) {
            Product product = productRepository.findById(productOrder.getProductId())
                    .orElseThrow( () -> new Exception("Product not found")
            );

            // caculate total money
            totalMoney += product.getPrice() * productOrder.getQuantity();

            OrderDetail orderDetail = OrderDetail.builder()
                    .product(product)
                    .order(order)
                    .quantity(productOrder.getQuantity())
                    .build();

            orderDetailList.add(orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        // update total money
        orderSaved.setOrderDetails(orderDetailList);
        orderSaved.setTotalMoney(totalMoney);
        orderRepository.save(orderSaved);

        OrderHasDetailResponse orderCreatingResponse = OrderHasDetailResponse.fromOrderDetail(orderSaved);

        return orderCreatingResponse;

    }


    @Override
    public OrderResponse updateOrder(OrderUpdatingDTO orderUpdatingDTO) throws Exception {
        Order existingOrder = orderRepository.findById(orderUpdatingDTO.getOrderId()).orElseThrow(
                () -> new Exception("order does not exist")
        );

        Tables table = tableRepository.findById(orderUpdatingDTO.getTableId())
                .orElseThrow(() -> new Exception("Table does not exist"));

        if (table.isStatus()){
            throw new Exception("Table is not empty");
        }

        Tables existingTable = existingOrder.getTable();
        existingTable.setStatus(false);
        tableRepository.save(existingTable);

        // set table is ocuppied
        table.setStatus(true);
        tableRepository.save(table);

        existingOrder.setTable(table);
        orderRepository.save(existingOrder);
        OrderResponse orderResponse = Order.toOrderResponse(existingOrder);

        return orderResponse;
    }

    @Override
    public void deleteOrder(int id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderHasDetailResponse getOrder(int orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new Exception("order does not exist")
        );
        return OrderHasDetailResponse.fromOrderDetail(order);
    }

    @Override
    public Order findOrderByTableId(int tableId) throws Exception {
        Order order = orderRepository.findByTableId(tableId);
        if (order == null) {
            throw new Exception("Order does not exist");
        }

        return order;
    }

    @Override
    public void payOrder(
            int tableId
    ) throws Exception {
        Order order = orderRepository.findByTableId(tableId);
        if (order == null) return;
        // set order paid status
        order.setPaid(true);
        orderRepository.save(order);

        // reset table status
        // if table is empty stops
        if (!order.getTable().isStatus()) return;

        Tables table = order.getTable();
        table.setStatus(false);
        tableRepository.save(table);
    }

}
