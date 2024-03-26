package com.example.demo.services;

import com.example.demo.dtos.OrderDetailDTO;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.repositories.OrderDetailRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.responses.ItemOrderDetailResponse;
import com.example.demo.responses.ListOrderDetailResponse;
import com.example.demo.responses.OrderDetailResponse;
import com.example.demo.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                ()-> new Exception("Order does not exist")
        );

        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                ()->new Exception("Product does nto exist")
        );

        // update order
        orderRepository.save(order);


        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailDTO.getQuantity())
                .build();
        orderDetailRepository.save(orderDetail);
        return orderDetail;
    }

    @Override
    public void deleteOrderDetail(int orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetailDTO orderDetailDTO,
                                         int orderDetailId
    ) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                ()-> new Exception("Order does not exist")
        );

        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                ()->new Exception("Product does not exist")
        );

        OrderDetail orderDetailExisting = orderDetailRepository.findById(orderDetailId).orElseThrow(
                ()->new Exception("Order detail does not exist")
        );

        orderDetailExisting.setOrder(order);
        orderDetailExisting.setProduct(product);
        orderDetailExisting.setQuantity(orderDetailDTO.getQuantity());


        return orderDetailRepository.save(orderDetailExisting);
    }

    @Override
    public ListOrderDetailResponse findAllByOrderId(int orderId) throws Exception {
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails = orderDetailRepository.findAllByOrderId(orderId);

        if (orderDetails.isEmpty()) throw new Exception("OrderDetail with order id is not exist");

        ListOrderDetailResponse listOrderDetailResponse = new ListOrderDetailResponse();

        listOrderDetailResponse.setOrderId(orderId);

        List<ItemOrderDetailResponse> itemOrderDetailResponses = new ArrayList<>();
        itemOrderDetailResponses = orderDetails.stream().map(
                orderDetail -> {
                    ProductResponse productResponse = ProductResponse.builder()
                            .name(orderDetail.getProduct().getName())
                            .id(orderDetail.getProduct().getId())
                            .price(orderDetail.getProduct().getPrice())
                            .categoryId(orderDetail.getProduct().getCategory().getId())
                            .build();
                    return ItemOrderDetailResponse.builder()
                            .quantity(orderDetail.getQuantity())
                            .id(orderDetail.getId())
                            .productResponse(productResponse)
                            .build();
                }).collect(Collectors.toList());
        listOrderDetailResponse.setItemOrderDetailList(itemOrderDetailResponses);
        return listOrderDetailResponse;
    }

}
