package com.reunet.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.reunet.app.models.Participant;
import com.reunet.app.models.payload.ParticipantUser;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
