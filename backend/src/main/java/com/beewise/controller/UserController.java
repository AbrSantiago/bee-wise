package com.beewise.controller;

import com.beewise.controller.dto.RegisterUserDTO;
import com.beewise.model.User;
import com.beewise.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser (@Valid @RequestBody RegisterUserDTO registerUserDTO){
        User newUser = userService.registerUser(registerUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @GetMapping("/allUser")
    public ResponseEntity<List<User>> getAllUsers (){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
