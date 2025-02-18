package com.project.shopping_app.service.impl;

import com.project.shopping_app.dtos.OrderDetailDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Order;
import com.project.shopping_app.model.OrderDetail;
import com.project.shopping_app.model.Product;
import com.project.shopping_app.repository.OrderDetailRepository;
import com.project.shopping_app.repository.OrderRepository;
import com.project.shopping_app.repository.ProductRepository;
import com.project.shopping_app.repository.UserRepository;
import com.project.shopping_app.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
  private final OrderDetailRepository orderDetailRepository;
  private final OrderRepository orderRepository;
  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  @Override
  public OrderDetail createOrderDetail(OrderDetailDTO newOrderDetail) throws Exception {
    Order order = orderRepository.findById(newOrderDetail.getOrderId())
          .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + newOrderDetail.getOrderId()));

    Product product = productRepository.findById(newOrderDetail.getProductId())
          .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + newOrderDetail.getProductId()));

    OrderDetail orderDetail = OrderDetail.builder()
          .order(order)
          .product(product)
          .price(newOrderDetail.getPrice())
          .numberOfProducts(newOrderDetail.getNumberOfProducts())
          .totalMoney(newOrderDetail.getTotalMoney())
          .color(newOrderDetail.getColor())
          .build();
    return orderDetailRepository.save(orderDetail);
  }

  @Override
  public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
    return orderDetailRepository.findById(id)
          .orElseThrow(() -> new DataNotFoundException("Order not found with id: " + id));
  }

  @Override
  public OrderDetail updateOrderDetail(Long id, OrderDetailDTO newOrderDetailData) throws DataNotFoundException {
    OrderDetail existingOrderDetail = orderDetailRepository.findById(id)
          .orElseThrow(() -> new DataNotFoundException("Not found order detail with id: " + id));
    Order existingOrder = orderRepository.findById(newOrderDetailData.getOrderId())
          .orElseThrow(() -> new DataNotFoundException("Not found order with id: " + id));
    Product existingProduct = productRepository.findById(newOrderDetailData.getProductId())
          .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + id));
    existingOrderDetail.setOrder(existingOrder);
    existingOrderDetail.setProduct(existingProduct);
    existingOrderDetail.setPrice(newOrderDetailData.getPrice());
    existingOrderDetail.setNumberOfProducts(newOrderDetailData.getNumberOfProducts());
    existingOrderDetail.setTotalMoney(newOrderDetailData.getTotalMoney());
    existingOrderDetail.setColor(newOrderDetailData.getColor());
    return orderDetailRepository.save(existingOrderDetail);
  }

  @Override
  public void deleteById(Long id) {
    orderDetailRepository.deleteById(id);
  }

  @Override
  public List<OrderDetail> findByOrderId(Long orderId) {
    return orderDetailRepository.findByOrderId(orderId);
  }
}
