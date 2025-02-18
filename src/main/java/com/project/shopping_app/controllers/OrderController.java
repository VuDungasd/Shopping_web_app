package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.OrderDTO;
import com.project.shopping_app.response.OrderListResponse;
import com.project.shopping_app.response.OrderResponse;
import com.project.shopping_app.response.ProductListResponse;
import com.project.shopping_app.service.OrderService;
import com.project.shopping_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;
  private final ProductService productService;

  @GetMapping("")
  public ResponseEntity<OrderListResponse> getAllOrders(
        @RequestParam("page") int page,
        @RequestParam("limit") int limit ) {
    PageRequest pageRequest = PageRequest.of(
          page, limit,
          Sort.by(Sort.Direction.DESC, "createdAt").descending());
    Page<OrderResponse> orderPage = orderService.getAllOrders(pageRequest);
    // get total page
    int totalPages = orderPage.getTotalPages();
    List<OrderResponse> orders = orderPage.getContent();
    return ResponseEntity.ok(OrderListResponse.builder()
          .orders(orders)
          .totalOrders(totalPages)
          .build()
    );
  }

  @PostMapping("/create")
  public ResponseEntity<?> createOrder(
        @RequestBody @Valid OrderDTO orderDTO,
        BindingResult result
  ) {
    try {
      if (result.hasErrors()) {
        List<String> errors = result.getFieldErrors()
              .stream()
              .map(err -> err.getDefaultMessage())
              .toList();
        return ResponseEntity.badRequest().body(errors);
      }
      orderService.createOrder(orderDTO);
      return ResponseEntity.ok("Create order successfully");
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{order_id}")
  public ResponseEntity<?> getOrderById(@PathVariable("order_id") Long orderId) {
    try{
      return ResponseEntity.ok(orderService.getOrderById(orderId));
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("userid/{user_id}")
  public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
    try{
      return ResponseEntity.ok(orderService.findByUserId(userId));
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrder(
        @PathVariable Long id,
        @RequestBody @Valid OrderDTO orderDTO
  ) {
    return ResponseEntity.ok("Update order successfully");
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
    // xoa mem => cap nhat truong active = false
    return ResponseEntity.ok("Delete order successfully");
  }
}
