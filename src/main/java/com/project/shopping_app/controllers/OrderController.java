package com.project.shopping_app.controllers;

import com.project.shopping_app.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
  @GetMapping("")
  public String getAllOrders(
        @RequestParam("page") int page,
        @RequestParam("limit") int limit
  ) {
    System.out.printf("page: %d sze: %d", page, limit);
    return "All Orders";
  }

  @PostMapping("/createOrder")
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
}
