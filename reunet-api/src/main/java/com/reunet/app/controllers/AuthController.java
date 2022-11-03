package com.reunet.app.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

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
import com.reunet.app.models.payload.request.LoginRequest;
import com.reunet.app.models.payload.request.RegisterRequest;
import com.reunet.app.models.payload.response.LoginResponse;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.security.jwt.JwtUtils;
import com.reunet.app.services.UserServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
                return ResponseEntity.badRequest().body(new Response<String>(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "email is alredy taken",
                        ""));
            }
            userServices.saveUser(registerRequest);

            return ResponseEntity.ok().body(new Response<String>(
                    HttpServletResponse.SC_OK,
                    "user registered successfully",
                    null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error creating user",
                    ""));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {

            if (!userServices.existsByEmail(loginRequest.getEmail())) {
                return ResponseEntity.badRequest().body(new Response<String>(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "user with this email doesnt exists",
                        ""));
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

            return ResponseEntity.ok().body(new Response<LoginResponse>(
                    HttpServletResponse.SC_OK,
                    "",
                    new LoginResponse(user, token)));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ""));
        }

    }
}