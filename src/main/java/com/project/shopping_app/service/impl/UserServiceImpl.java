package com.project.shopping_app.service.impl;

import com.project.shopping_app.compoments.JwtTokenUtil;
import com.project.shopping_app.dtos.UserDTO;
import com.project.shopping_app.exceptions.DataNotFoundException;
import com.project.shopping_app.model.Role;
import com.project.shopping_app.model.User;
import com.project.shopping_app.repository.RoleRepository;
import com.project.shopping_app.repository.UserRepository;
import com.project.shopping_app.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final JwtTokenUtil jwtTokenUtil;

  private final AuthenticationManager authenticationManager;

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

    log.info("New user created: {}", user);

    // check xem neu co accountID thi k check password
    if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId() == 0) {
      String password = userDTO.getPassword();
      String encodedPassword = passwordEncoder.encode(password);
      user.setPassword(encodedPassword);

      // ma hoa bang sha256 ...
//      String encodedPassword = passwordEncode.encode(password);
//      newUser.setPassword(encodedPassword);

    }
    return userRepository.save(user);
  }

  @Override
  public String login(String phoneNumber, String password) throws Exception {
    Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
    if(optionalUser.isEmpty()){
      throw new DataNotFoundException("Invalid phonenumber / password");
    }

//     tra ve token
    User existingUser = optionalUser.get();
    // check password
    if(existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0){
      if(!passwordEncoder.matches(password, existingUser.getPassword())){
        throw new BadCredentialsException("Invalid password");
      }
    }
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
          phoneNumber, password, existingUser.getAuthorities());
    // authenticate with java spring security
    authenticationManager.authenticate(authenticationToken);
    return jwtTokenUtil.generateToken(existingUser);
  }
}
