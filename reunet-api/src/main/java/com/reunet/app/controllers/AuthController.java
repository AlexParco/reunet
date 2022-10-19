package com.reunet.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.reunet.app.models.payload.LoginResponse;
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
            Map<String, String> response = new HashMap<String, String>();

            if (userServices.existsByEmail(registerRequest.getEmail())) {
                response.put("message", "email is alredy taken");
                return ResponseEntity.badRequest().body(response);
            }
            userServices.saveUser(registerRequest);

            response.put("message", "user registered successfully");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, String> response = new HashMap<String, String>();

            if (!userServices.existsByEmail(loginRequest.getEmail())) {
                response.put("message", "user with this email doesnt exists");
                return ResponseEntity.badRequest().body(response);
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
            user.setRole(roles.get(0));

            return ResponseEntity.ok().body(new LoginResponse(
                    user,
                    token));

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}