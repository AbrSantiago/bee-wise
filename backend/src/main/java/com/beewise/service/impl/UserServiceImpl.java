package com.beewise.service.impl;

import com.beewise.controller.dto.*;
import com.beewise.exception.UserNotFoundException;
import com.beewise.model.Lesson;
import com.beewise.model.User;
import com.beewise.repository.UserRepository;
import com.beewise.service.LessonProgressService;
import com.beewise.service.LessonService;
import com.beewise.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LessonProgressService progressService;
    private final LessonService lessonService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LessonProgressService progressService, LessonService lessonService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.progressService = progressService;
        this.lessonService = lessonService;
    }

    @Override
    public User registerUser(RegisterUserDTO registerUserDTO) {
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User newUser = new User();
        newUser.setName(registerUserDTO.getName());
        newUser.setSurname(registerUserDTO.getSurname());
        newUser.setEmail(registerUserDTO.getEmail());
        newUser.setUsername(registerUserDTO.getUsername());

        String hashedPassword = passwordEncoder.encode(registerUserDTO.getPassword());
        newUser.setPasswordHash(hashedPassword);

        newUser.setPoints(0);

        return userRepository.save(newUser);
    }

    @Override
    public User authenticateUser(LoginUserDTO loginUserDTO) {
        User user = userRepository.findByUsername(loginUserDTO.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginUserDTO.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public LessonCompleteDTO lessonComplete(LessonCompleteRequestDTO requestDTO) {
        Lesson lesson = lessonService.getLessonById(requestDTO.getCompletedLessonId());
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setPoints(user.getPoints() + requestDTO.getCorrectExercises() * 10);
        user.setCurrentLesson(+1);
        userRepository.save(user);
        progressService.upsertProgress(user, lesson);
        return new LessonCompleteDTO(true, "Progress updated", user.getPoints());
    }

    @Override
    public UserPointsDTO getUserPoints(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return new UserPointsDTO(user.getId(), user.getUsername(), user.getPoints(), user.getCurrentLesson());
    }
}
