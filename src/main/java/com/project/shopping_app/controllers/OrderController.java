package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
  @GetMapping("")
  public String getAllOrders(
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
  ) {
    return "All Orders " + " Page: " + page + ", Limit: " + limit;
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
      return ResponseEntity.ok("Create order successfully");
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getOrderById(@PathVariable("order_id") Long orderId) {
    try{
      return ResponseEntity.ok("Order ID: " + orderId);
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping("/{user_id}")
  public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
    try{
      return ResponseEntity.ok("Get all order from user_id ");
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
