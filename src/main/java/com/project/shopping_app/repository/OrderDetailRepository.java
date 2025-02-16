package com.project.shopping_app.repository;

import com.project.shopping_app.model.OrderDetail;
import com.project.shopping_app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
  List<OrderDetail> findByOrderId(Long orderId);
}
