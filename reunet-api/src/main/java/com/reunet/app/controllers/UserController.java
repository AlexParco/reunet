package com.reunet.app.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        try {
            User user = userServices.findById(id);

            return ResponseEntity.ok().body(new Response<User>(
                    HttpServletResponse.SC_OK,
                    "",
                    user));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    null));
        }
    }
}
