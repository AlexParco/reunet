package com.reunet.app.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.reunet.app.models.User;
import com.reunet.app.models.payload.request.RegisterRequest;
import com.reunet.app.repository.UserRepository;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void saveUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setLastname(registerRequest.getLastname());
        user.setFirstname(registerRequest.getFirstname());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setRole(registerRequest.getRole());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Long getIdFromEmail(String email) {
        return userRepository.findByEmail(email).get().getId();
    }

    public User findById(Long userid) {
        return userRepository.findById(userid).get();
    }

}
