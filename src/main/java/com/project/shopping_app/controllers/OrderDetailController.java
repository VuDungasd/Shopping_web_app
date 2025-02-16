package com.project.shopping_app.controllers;


import com.project.shopping_app.dtos.CategoryDTO;
import com.project.shopping_app.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
  // get all order detail
  @GetMapping("")
  public ResponseEntity<?> getAllOrderDetail( // ex: localhost:8080/api/v1/categories?page=18&limit=10
                                              @RequestParam("page") int page,
                                              @RequestParam("limit") int limit
  ) {
    return ResponseEntity.ok(String.format("Page: " + page  + "Limit: " + limit));
  }

  @GetMapping("/orderdetail/{orderid}" )
  public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("orderid") Long orderid){
    return ResponseEntity.ok(String.format("OrderDetail: " + orderid));
  }

  @PostMapping("")
  public ResponseEntity<?> createOrderDetail(
        @Valid @RequestBody OrderDetailDTO orderDetailDTO,
        BindingResult result
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();
      return ResponseEntity.badRequest().body(errorMessages);
    }
    return ResponseEntity.ok("Hello world test order detail " + orderDetailDTO);
  }

  // get Order

  @PutMapping("/{id}")
  public ResponseEntity<?> updateOrderDetail(
        @Valid @PathVariable("id") Long id,
        @RequestBody OrderDetailDTO orderDetailDTO,
        BindingResult result
  ) {
    if(result.hasErrors()) {
      List<String> err = result.getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();
      return ResponseEntity.badRequest().body(err);
    }
    return ResponseEntity.ok("updateOrderDetail with orderdetailID= " + id + " new orderDetailData= " + orderDetailDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable("id") Long id){
    return ResponseEntity.noContent().build();
  }
}
