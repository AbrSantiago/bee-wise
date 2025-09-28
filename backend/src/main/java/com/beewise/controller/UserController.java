package com.beewise.controller;

import com.beewise.controller.dto.*;
import com.beewise.model.User;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import com.beewise.service.impl.TokenServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenServiceImpl tokenService;
    private final JwtService jwtService;

    public UserController(UserService userService, TokenServiceImpl tokenService, JwtService jwtService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> registerUser (@Valid @RequestBody RegisterUserDTO registerUserDTO){
        User newUser = userService.registerUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        User user = userService.authenticateUser(loginUserDTO);
        String accessToken = jwtService.generateAccessToken(user.getUsername());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getUsername()
        ));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<RefreshTokenResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        RefreshTokenResponseDTO response = tokenService.rotateRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.substring(7));
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(new UserDTO(user));
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers (){
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOS = users.stream().map(UserDTO::new).toList();
        return ResponseEntity.ok(userDTOS);
    }

    @PostMapping("/lessonComplete")
    public ResponseEntity<LessonCompleteDTO> lessonComplete(@RequestBody LessonCompleteRequestDTO requestDTO) {
        LessonCompleteDTO progress = userService.lessonComplete(requestDTO);
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/points")
    public ResponseEntity<UserPointsDTO> getUserPoints(@RequestHeader("Authorization") String token) {
        String username = jwtService.extractUsername(token.substring(7));
        UserPointsDTO userPoints = userService.getUserPoints(username);
        return ResponseEntity.ok(userPoints);
    }
}
