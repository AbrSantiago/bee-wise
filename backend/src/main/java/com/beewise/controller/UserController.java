package com.beewise.controller;

import com.beewise.controller.dto.AuthResponseDTO;
import com.beewise.controller.dto.LoginUserDTO;
import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.model.User;
import com.beewise.service.UserService;
import com.beewise.service.impl.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> registerUser (@Valid @RequestBody RegisterUserDTO registerUserDTO){
        User newUser = userService.registerUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDTO loginUserDTO) {
        User user = userService.authenticateUser(loginUserDTO);
        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getUsername()));
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<User>> getAllUsers (){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
