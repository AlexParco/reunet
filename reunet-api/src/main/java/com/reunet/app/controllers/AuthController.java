package com.reunet.app.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.User;
import com.reunet.app.models.payload.LoginRequest;
import com.reunet.app.models.payload.RegisterRequest;
import com.reunet.app.services.UserServices;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userServices.saveUser(registerRequest);
            return ResponseEntity.ok().body("user registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Optional<User> user = userServices.existByEmail(loginRequest.getEmail());
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body("error: email not exists");
            }

            if (user.get().getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.ok().body(user);
            }

            return ResponseEntity.badRequest().body("error: password is incorrect");

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}