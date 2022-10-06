package com.reunet.app.models;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String role;
    private String avatar = "";

    @Column(name = "created_at")
    private Date CreatedAt = new Date();

    @Column(name = "updated_at")
    private Date UpdatedAt = new Date();

}
