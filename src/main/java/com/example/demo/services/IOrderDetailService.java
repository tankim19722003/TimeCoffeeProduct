package com.example.demo.services;

import com.example.demo.dtos.OrderDetailDTO;
import com.example.demo.dtos.UpdatingAndAddingItemDTO;
import com.example.demo.dtos.UpdatingOrderDetailItem;
import com.example.demo.model.OrderDetail;
import com.example.demo.responses.ListOrderDetailResponse;
import com.example.demo.responses.OrderDetailResponse;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
    void deleteOrderDetail(int orderDetailId) throws Exception;

    OrderDetail updateOrderDetail(OrderDetailDTO orderDetailDTO, int orderDetailId) throws Exception;
    ListOrderDetailResponse findAllByOrderId(int orderId) throws Exception;
    ListOrderDetailResponse getOrderDetailOfTable(int tableId);
    void updatingAndAddingOrder(UpdatingAndAddingItemDTO updatingAndAddingItemDTO) throws Exception;
}
