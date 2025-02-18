package com.project.shopping_app.repository;

import com.project.shopping_app.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  @Override
  Page<Order> findAll(Pageable pageable);

  List<Order> findAllByUserId(Long userId);

}
