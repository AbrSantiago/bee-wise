package com.beewise.service;

import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.model.User;

import java.util.List;

public interface UserService {
    User registerUser (RegisterUserDTO registerUserDTO);
    User getUserById(Long id);
    List<User> getAllUsers();
}
