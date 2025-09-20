package com.beewise.service;

import com.beewise.controller.dto.LessonCompleteDTO;
import com.beewise.controller.dto.LessonCompleteRequestDTO;
import com.beewise.controller.dto.LoginUserDTO;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.model.User;

import java.util.List;

public interface UserService {
    User registerUser(RegisterUserDTO registerUserDTO);
    User authenticateUser(LoginUserDTO loginUserDTO);
    User getUserById(Long id);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    LessonCompleteDTO lessonComplete(LessonCompleteRequestDTO requestDTO);
}
