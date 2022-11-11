package com.reunet.app.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.Participant;
import com.reunet.app.models.payload.ParticipantUser;
import com.reunet.app.repository.ParticipantRepository;

@Service
public class ParticipantServices {

    @Autowired
    ParticipantRepository participantRepository;

    public void create(Participant participant) {
        participant.setCreatedAt(new Date());
        participant.setUpdatedAt(new Date());
        participantRepository.save(participant);
    }

    public void delete(Long id) {
        participantRepository.deleteById(id);
    }

    public List<ParticipantUser> findAllParticipantsByGroupId(Long groupId) {
        return participantRepository.findAllParticipantsByGroupId(groupId);
    }

}
