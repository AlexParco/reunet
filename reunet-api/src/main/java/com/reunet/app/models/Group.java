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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Table(name = "groups")
@Entity
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @JsonAlias("user_id")
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    private String description;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;

}
