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
@ToString
@Table(name = "activities")
@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    private String name;

    @JsonAlias("type")
    @JsonProperty("type")
    @Column(name = "type_activity")
    private String typeActivity;

    @JsonAlias("closed_at")
    @JsonProperty("closed_at")
    @Column(name = "closed_at")
    private Date closedAt;

    @Column(name = "created_at")
    @JsonIgnore
    private Date createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
    private Date updatedAt;
}
