package com.reunet.app.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    @JsonProperty("participant_id")
    private Long participantId;

    @Column(name = "group_id")
    @JsonProperty("group_id")
    private Long groupId;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Long userId;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Date createdAt;

    @JsonIgnore
    @Column(name = "updated_at")
    private Date updatedAt;

}
