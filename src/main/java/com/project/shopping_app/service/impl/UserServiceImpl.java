package com.project.shopping_app.service.impl;

import com.project.shopping_app.dtos.UserDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Role;
import com.project.shopping_app.model.User;
import com.project.shopping_app.repository.RoleRepository;
import com.project.shopping_app.repository.UserRepository;
import com.project.shopping_app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  @Override
  public User createUser(UserDTO userDTO) throws DataNotFoundException {
    String phoneNumber = userDTO.getPhoneNumber();
    // check phone number
    if (userRepository.existsByPhoneNumber(phoneNumber)) {
      throw new DataIntegrityViolationException("Phone number already exists");
    }
    Role existRole = roleRepository.findById(userDTO.getRoleID())
          .orElseThrow(() -> new DataNotFoundException("Role not found!!!"));
    User user = User.builder()
          .fullname(userDTO.getFullName())
          .phoneNumber(userDTO.getPhoneNumber())
          .address(userDTO.getAddress())
          .dateOfBirth(userDTO.getDateOfBirth())
          .password(userDTO.getPassword())
          .isActive(true)
          .role(existRole)
          .facebookAccountId(userDTO.getFacebookAccountId())
          .googleAccountId(userDTO.getGoogleAccountId())
          .build();
    User newUser = userRepository.save(user);
    log.info("New user created: {}", newUser);
    // check xem neu co accountID thi k check password
    if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
      String password = userDTO.getPassword();

      // ma hoa bang sha256 ...
//      String encodedPassword = passwordEncode.encode(password);
//      newUser.setPassword(encodedPassword);
    }
    return newUser;
  }

  @Override
  public String login(String phoneNumber, String password) {
    //
    return "";
  }
}
