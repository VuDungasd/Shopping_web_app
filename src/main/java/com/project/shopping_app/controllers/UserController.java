package com.project.shopping_app.controllers;


import com.project.shopping_app.dtos.UserDTO;
import com.project.shopping_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("")
  public String getAllUser(@RequestPart("page") int page, @RequestPart("limit") int limit) {
    return "Page: " + page + ", Limit: " + limit;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult result) {
    try{
      if (result.hasErrors()) {
        List<String> errorMessage = result.getFieldErrors()
              .stream()
              //            .map(fieldError -> fieldError.getDefaultMessage())
              .map(FieldError::getDefaultMessage)
              .toList();
        return ResponseEntity.badRequest().body(errorMessage);
      }
      if(!userDTO.getRetypePassword().equals(userDTO.getPassword())){
        return ResponseEntity.badRequest().body("Password does not match");
      }
      userService.createUser(userDTO);
      return ResponseEntity.ok("Register user successfully");
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping("/login")
  public ResponseEntity<String> loginUser(@Valid @RequestBody UserDTO userDTO) {
    try{
      // kiem tra thong tin dang nhap + sinh token
      String token = userService.login(userDTO.getPhoneNumber(), userDTO.getPassword());
      // tra ve token trong response
      return ResponseEntity.ok(token);
    }catch (Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
