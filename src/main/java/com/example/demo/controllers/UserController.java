package com.example.demo.controllers;


import com.example.demo.dtos.UserDTO;
import com.example.demo.responses.UserResponse;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/timecoffee/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody UserDTO userDTO
    ) {
        try {
            UserResponse userResponse = userService.login(userDTO);
            return ResponseEntity.ok().body(userResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public void register(
            @RequestBody UserDTO userDTO
    ) {
        userService.register(userDTO);
    }

    @PostMapping("/hello")
    public void hello() {
        System.out.println("Hello");
    }
}
