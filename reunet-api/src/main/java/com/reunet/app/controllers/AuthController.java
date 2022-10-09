package com.reunet.app.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.User;
import com.reunet.app.models.UserDetailsImpl;
import com.reunet.app.models.payload.LoginRequest;
import com.reunet.app.models.payload.RegisterRequest;
import com.reunet.app.models.payload.UserWithTokenResponse;
import com.reunet.app.security.jwt.JwtUtils;
import com.reunet.app.services.UserServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserServices userServices;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            if (userServices.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity.badRequest().body("email is alredy taken");
            }
            userServices.saveUser(registerRequest);
            return ResponseEntity.ok().body("user registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            if (!userServices.existsByEmail(loginRequest.getEmail())) {
                return ResponseEntity.badRequest().body("user with this email doesnt exists");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                            loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            String token = jwtUtils.generateToken(loginRequest.getEmail());

            User user = userServices.findByEmail(jwtUtils.getEmailFromToken(token)).get();

            UserWithTokenResponse userWithTokenResponse = new UserWithTokenResponse(
                    userDetails.getId(),
                    user.getFirstname(),
                    user.getLastname(),
                    userDetails.getUsername(),
                    roles.get(0),
                    user.getAvatar(),
                    token);
            System.out.println(userWithTokenResponse.toString());

            return ResponseEntity.ok().body(userWithTokenResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}