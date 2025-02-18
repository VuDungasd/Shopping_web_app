package com.project.shopping_app.service.impl;

import com.project.shopping_app.dtos.OrderDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Order;
import com.project.shopping_app.model.OrderStatus;
import com.project.shopping_app.model.User;
import com.project.shopping_app.repository.OrderRepository;
import com.project.shopping_app.repository.ProductRepository;
import com.project.shopping_app.repository.UserRepository;
import com.project.shopping_app.response.OrderResponse;
import com.project.shopping_app.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;


  @Override
  public Page<OrderResponse> getAllOrders(PageRequest pageRequest) {
    // get all order by page and limit
    return orderRepository.findAll(pageRequest).map(order -> {
      // check active
      if(!order.getActive()){
        return null;
      }
      return OrderResponse.fromOrder(order);
    });
  }

  @Override
  public Order createOrder(OrderDTO orderDTO) throws Exception {
    User exitstingUser = userRepository.findById(orderDTO.getUserId())
          .orElseThrow(() -> new DataNotFoundException("User not found"));
    Order order = Order.builder()
          .user(exitstingUser)
          .fullname(orderDTO.getFullName())
          .email(orderDTO.getEmail())
          .phoneNumber(orderDTO.getPhoneNumber())
          .address(orderDTO.getAddress())
          .note(orderDTO.getNote())
          .orderDate(new Timestamp(System.currentTimeMillis()))
          .status(OrderStatus.PENDING)
          .totalMoney(orderDTO.getTotalMoney())
          .shippingMethod(orderDTO.getShippingMethod())
          .shippingAddress(orderDTO.getShippingAddress())
          .paymentMethod(orderDTO.getPaymentMethod())
          .active(true)
          .build();
    return orderRepository.save(order);
  }

  @Override
  public Order getOrderById(Long id) {
    return orderRepository.findById(id)
          .orElseThrow(() -> new DateTimeException("Order not found"));
  }

  @Override
  public Order updateStatusOrder(Long id, String s) throws DataNotFoundException {
    Order existingOrder = getOrderById(id);
    if(existingOrder.getStatus().equals(OrderStatus.PENDING)) {
      if (s.equals(OrderStatus.PROCESSING)) {
        existingOrder.setStatus(OrderStatus.PROCESSING);
      }else if (s.equals(OrderStatus.CANCELLED)) {
        existingOrder.setStatus(OrderStatus.CANCELLED);
      }
    }
    if(existingOrder.getStatus().equals(OrderStatus.PROCESSING)) {
      if (s.equals(OrderStatus.SHIPPING)) {
        existingOrder.setStatus(OrderStatus.SHIPPING);
      } else if (s.equals(OrderStatus.CANCELLED)) {
        existingOrder.setStatus(OrderStatus.CANCELLED);
      }
    }
    if(existingOrder.getStatus().equals(OrderStatus.SHIPPING)) {
      if (s.equals(OrderStatus.DELIVERED)) {
        existingOrder.setStatus(OrderStatus.DELIVERED);
      }
    }
    if(existingOrder.getStatus().equals(OrderStatus.DELIVERED)) {
      if (s.equals(OrderStatus.COMPLETED)) {
        existingOrder.setStatus(OrderStatus.COMPLETED);
      }else if (s.equals(OrderStatus.FAILED)) {
        existingOrder.setStatus(OrderStatus.FAILED);
      }
    }

    return null;
  }

  @Override
  public void deleteOrderByOrderID(Long id) {
    Optional<Order> order = orderRepository.findById(id);
    if(order.isPresent()) {
      Order orderToDelete = order.get();
      orderToDelete.setActive(false);
      orderRepository.save(orderToDelete);
    }else{
      throw new RuntimeException("Order not found");
    }
  }

  @Override
  public List<Order> findByUserId(Long userId) {
    return orderRepository.findAllByUserId(userId);
  }
}
