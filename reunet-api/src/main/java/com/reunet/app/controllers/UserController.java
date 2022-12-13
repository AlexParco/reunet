package com.reunet.app.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.User;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.services.UserServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @GetMapping("{id}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userServices.findById(id);

            return ResponseEntity.ok().body(new Response<User>(
                    HttpServletResponse.SC_OK,
                    "",
                    user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<User>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    null));
        }
    }

    @GetMapping("")
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        try {
            List<User> users = userServices.findAll();
            return ResponseEntity.ok().body(new Response<List<User>>(
                    HttpServletResponse.SC_OK,
                    "",
                    users));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<List<User>>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    null));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<User>> putUser(@PathVariable("id") Long id, @RequestBody User param) {
        try {
            User user = userServices.findById(id);
            user.setAvatar(param.getAvatar());
            user.setFirstname(param.getFirstname());
            user.setLastname(param.getLastname());

            User userSaved = userServices.updateUser(user);

            return ResponseEntity.ok().body(new Response<User>(
                    HttpServletResponse.SC_OK,
                    "",
                    userSaved));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<User>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    null));
        }

    }
}
