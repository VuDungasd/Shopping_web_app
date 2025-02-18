package com.project.shopping_app.service;

import com.project.shopping_app.dtos.OrderDetailDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.OrderDetail;

import java.util.List;

public interface OrderDetailService {
  OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception;
  OrderDetail getOrderDetail(Long id) throws DataNotFoundException;
  OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData)
        throws DataNotFoundException;
  void deleteById(Long id);
  List<OrderDetail> findByOrderId(Long orderId);
}
