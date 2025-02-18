package com.project.shopping_app.service;

import com.project.shopping_app.dtos.UserDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.User;

public interface UserService {

  User createUser(UserDTO userDTO) throws DataNotFoundException;

  String login(String phoneNumber , String password);
  
}
