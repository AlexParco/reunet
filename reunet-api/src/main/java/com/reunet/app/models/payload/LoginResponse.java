package com.reunet.app.models.payload;

import com.reunet.app.models.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginResponse {
    private User user;
    private String token;

    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }
}
