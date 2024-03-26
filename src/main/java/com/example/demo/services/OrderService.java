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
import com.example.demo.responses.OrderCreatingResponse;
import com.example.demo.responses.OrderDetailInOrderResponse;
import com.example.demo.responses.OrderDetailResponse;
import com.example.demo.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    @Override
    public OrderCreatingResponse createOrder(OrderDTO orderDTO) throws Exception {
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

        List<OrderDetailInOrderResponse> orderDetailResponseList = new ArrayList<>();
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

            orderDetailResponseList.add(OrderDetail.toOrderDetailInOrderResponse(orderDetail));
            orderDetailRepository.save(orderDetail);
        }

        // update total money
        orderSaved.setTotalMoney(totalMoney);
        orderRepository.save(orderSaved);

        OrderCreatingResponse orderCreatingResponse = OrderCreatingResponse.builder()
                .tableResponse(Tables.toTableResponse(tables))
                .id(orderSaved.getId())
                .createAt(orderSaved.getOrderDate())
                .totalMoney(totalMoney)
                .orderDetailResponseList(orderDetailResponseList)
                .build();

        return orderCreatingResponse;

    }


    @Override
    public OrderResponse updateOrder(OrderUpdatingDTO orderUpdatingDTO, int orderId) throws Exception {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow(
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
