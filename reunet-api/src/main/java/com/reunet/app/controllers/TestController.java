package com.reunet.app.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String AdminHome(HttpServletRequest request) {
        return "Welcome to Admin route";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String UserHome() {
        return "Welcome to User route";
    }

    @GetMapping("/test")
    public String TestHome() {
        return "Welcome to test route";
    }

}
