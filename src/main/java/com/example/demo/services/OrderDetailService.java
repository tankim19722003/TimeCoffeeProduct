package com.example.demo.services;

import com.example.demo.dtos.AddingOrderDetailDTO;
import com.example.demo.dtos.OrderDetailDTO;
import com.example.demo.dtos.UpdatingAndAddingItemDTO;
import com.example.demo.dtos.UpdatingOrderDetailItem;
import com.example.demo.model.Order;
import com.example.demo.model.OrderDetail;
import com.example.demo.model.Product;
import com.example.demo.model.Tables;
import com.example.demo.repositories.OrderDetailRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.TableRepository;
import com.example.demo.responses.ListOrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final TableRepository tableRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                ()-> new Exception("Order does not exist")
        );

        Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                ()->new Exception("Product does nto exist")
        );

        // update order
        int totalMoney = order.getTotalMoney() + product.getPrice() * orderDetailDTO.getQuantity();

        order.setTotalMoney(totalMoney);
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
    public void deleteOrderDetail(int orderDetailId) throws Exception {
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(
                () -> new Exception("Order detail does not exist")
        );

        Order order = orderDetail.getOrder();
        int totalMoney = order.getTotalMoney() - orderDetail.getQuantity() * orderDetail.getProduct().getPrice();
        order.setTotalMoney(totalMoney);

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
        // check order
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new Exception("order does not exist")
        );

        // handle order detail response
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails = orderDetailRepository.findAllByOrderId(orderId);

        if (orderDetails.isEmpty()) return null;

        ListOrderDetailResponse listOrderDetailResponse = new ListOrderDetailResponse();
        listOrderDetailResponse = ListOrderDetailResponse.fromListOrderDetail(orderDetails, order);
        return listOrderDetailResponse;
    }


    @Override
    public ListOrderDetailResponse getOrderDetailOfTable(int tableId) {
            Order order = orderRepository.findByTableId(tableId);
            List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrderId(order.getId());
            ListOrderDetailResponse listOrderDetailResponse = new ListOrderDetailResponse();
        return ListOrderDetailResponse.fromListOrderDetail(orderDetails, order);
    }

    @Override
    public void updatingAndAddingOrder(
        UpdatingAndAddingItemDTO updatingAndAddingItemDTO
    ) throws Exception {
        Tables table = tableRepository.findById(updatingAndAddingItemDTO.getTableId()).orElseThrow(
                () -> new Exception("Table not found")
        );

        if (!table.isStatus()) throw new Exception("Table is empty");
        // adding new item
         if (updatingAndAddingItemDTO.getAddingOrderDetails() != null) {
             saveOrderDetails(updatingAndAddingItemDTO.getAddingOrderDetails()
                     , updatingAndAddingItemDTO.getTableId());
         }

        // updating new item
        if (updatingAndAddingItemDTO.getUpdatingOrderDetails() != null) {
            updateOrderDetails(updatingAndAddingItemDTO.getUpdatingOrderDetails()
                    , updatingAndAddingItemDTO.getTableId());
        }
    }

    private void saveOrderDetails(
            List<AddingOrderDetailDTO> addingOrderDetailItems,
            int tableId
    ) throws Exception {
        Order order = orderRepository.findByTableId(tableId);
        if (order == null) return;

        int totalMoney = order.getTotalMoney();
        for (AddingOrderDetailDTO addingOrderDetailItem : addingOrderDetailItems) {
            Product product = productRepository.findById(addingOrderDetailItem.getProductId()).orElseThrow(
                    ()->new Exception("Product does not exist")
            );
            totalMoney +=  product.getPrice() * addingOrderDetailItem.getQuantity();

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(addingOrderDetailItem.getQuantity())
                    .build();
            orderDetailRepository.save(orderDetail);
        }

        // update order
        order.setTotalMoney(totalMoney);
        orderRepository.save(order);
    }

    private void updateOrderDetails(
            List<UpdatingOrderDetailItem> updatingOrderDetailItems,
            int tableId
    ) throws Exception {
        Order order = orderRepository.findByTableId(tableId);
        if (order == null) return;

        for (UpdatingOrderDetailItem updatingOrderDetailItem: updatingOrderDetailItems){
            OrderDetail orderDetail = orderDetailRepository.findById(updatingOrderDetailItem.getId()).orElseThrow(
                    () -> new Exception("Order detail does not exist")
            );
            orderDetail.setQuantity(updatingOrderDetailItem.getQuantity());
            orderDetailRepository.save(orderDetail);
        }
    }
}
