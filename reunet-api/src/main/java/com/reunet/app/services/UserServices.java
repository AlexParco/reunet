package com.reunet.app.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.User;
import com.reunet.app.models.payload.RegisterRequest;
import com.reunet.app.repository.UserRepository;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setLastname(registerRequest.getLastname());
        user.setFirstname(registerRequest.getFirstname());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    public Optional<User> existByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
