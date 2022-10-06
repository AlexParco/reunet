package com.reunet.app.models.payload;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class RegisterRequest {

    private String lastname;
    private String firstname;
    private String email;
    private String role;
    private String password;
}
