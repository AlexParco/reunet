package com.reunet.app.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.Participant;
import com.reunet.app.repository.ParticipantRepository;
import com.reunet.app.repository.UserRepository;

@Service
public class ParticipantServices {

    @Autowired
    ParticipantRepository participantRepository;
    UserRepository userRepository;

    public void create(Participant participant) {
        participant.setCreatedAt(new Date());
        participant.setUpdatedAt(new Date());
        participantRepository.save(participant);
    }

    public void delete(Long groupId) {
        participantRepository.deleteById(groupId);
    }

    public List<Participant> findAllParticipants(Long groupId) {
        return participantRepository.findAllByGroupId(groupId);
    }

}
