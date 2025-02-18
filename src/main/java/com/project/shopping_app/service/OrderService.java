package com.project.shopping_app.service;


import com.project.shopping_app.dtos.OrderDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Order;
import com.project.shopping_app.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
  Page<OrderResponse> getAllOrders(PageRequest pageRequest);
  Order createOrder(OrderDTO orderDTO) throws Exception;
  Order getOrderById(Long id);
  Order updateStatusOrder(Long id, String s) throws DataNotFoundException;
  void deleteOrderByOrderID(Long id);
  List<Order> findByUserId(Long userId);
}
