package com.beewise.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from the Bee-Wise backend!";
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Backend is running properly");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "Bee-Wise API");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cors")
    public String corsTest() {
        return "CORS configured correctly";
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
