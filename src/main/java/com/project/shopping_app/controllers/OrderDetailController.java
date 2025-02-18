package com.project.shopping_app.controllers;


import com.project.shopping_app.dtos.CategoryDTO;
import com.project.shopping_app.dtos.OrderDetailDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Order;
import com.project.shopping_app.model.OrderDetail;
import com.project.shopping_app.repository.OrderDetailRepository;
import com.project.shopping_app.response.OrderDetailResponse;
import com.project.shopping_app.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {

  private final OrderDetailService orderDetailService;
  private final OrderDetailRepository orderDetailRepository;

  // get all order detail
  @GetMapping("")
  public ResponseEntity<?> getAllOrderDetail( // ex: localhost:8080/api/v1/categories?page=18&limit=10
                                              @RequestParam("page") int page,
                                              @RequestParam("limit") int limit
  ) {
    return ResponseEntity.ok(String.format("Page: " + page  + "Limit: " + limit));
  }

  @GetMapping("/{id}" )
  public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable("id") Long id) throws DataNotFoundException {
    OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
    return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(orderDetail));
//    return ResponseEntity.ok(orderDetail);
  }

  @PostMapping("")
  public ResponseEntity<?> createOrderDetail(
        @Valid @RequestBody OrderDetailDTO orderDetailDTO,
        BindingResult result) {
    try{
      if (result.hasErrors()) {
        List<String> errorMessages = result.getFieldErrors()
              .stream()
              .map(fieldError -> fieldError.getDefaultMessage())
              .toList();
        return ResponseEntity.badRequest().body(errorMessages);
      }
      OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);
      return ResponseEntity.ok(OrderDetailResponse.fromOrderDetail(newOrderDetail));
    } catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
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
    try{
      OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
      return ResponseEntity.ok(orderDetail);
    }catch (DataNotFoundException e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteOrderDetail(
        @Valid @PathVariable("id") Long id){
    orderDetailService.deleteById(id);
    return ResponseEntity.ok().body("Order detail deleted successfully");
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<?> getOrderDetailByOrderId(@PathVariable("orderId") Long orderId){
    List<OrderDetail> orderDetails = orderDetailService.findByOrderId(orderId);
    return ResponseEntity.ok(orderDetails);
  }
}
