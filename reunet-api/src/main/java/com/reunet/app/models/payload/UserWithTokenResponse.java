package com.reunet.app.models.payload;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserWithTokenResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String avatar;
    private String token;

    public UserWithTokenResponse(Long id, String firstname, String lastname, String email, String role, String avatar,
            String token) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.avatar = avatar;
        this.token = token;
    }
}
