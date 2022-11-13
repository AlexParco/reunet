package com.reunet.app.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = "users")
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    @JsonIgnore
    private String password;

    private String role;

    private String avatar = "";

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Date CreatedAt = new Date();

    @JsonIgnore
    @Column(name = "updated_at")
    private Date UpdatedAt = new Date();
}
