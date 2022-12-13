package com.reunet.app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    @JsonProperty("message_id")
    private Long messageId;

    @JsonProperty("user_id")
    @Column(name = "user_id")
    private Long userId;

    @JsonProperty("group_id")
    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "message_body")
    @JsonProperty("body")
    private String messageBody;

    @JsonProperty("created_at")
    @Column(name = "created_at")
    private Date createdAt;
}
