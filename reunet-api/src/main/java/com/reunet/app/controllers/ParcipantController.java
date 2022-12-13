package com.reunet.app.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.Participant;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.security.jwt.JwtUtils;
import com.reunet.app.services.ParticipantServices;
import com.reunet.app.services.UserServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/participants")
public class ParcipantController {

    @Autowired
    ParticipantServices participantServices;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserServices userServices;

    @GetMapping("")
    public ResponseEntity<?> getAllParticipants(@RequestParam Long groupid) {
        try {
            List<Participant> participants = participantServices.findAllParticipants(groupid);

            return ResponseEntity.ok().body(new Response<List<Participant>>(
                    HttpServletResponse.SC_OK,
                    "",
                    participants));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error",
                    null));
        }
    }

    @PostMapping("")
    public ResponseEntity<Response<Participant>> postParticipant(@RequestBody Participant participant) {
        try {
            Participant particpantCreated = participantServices.create(participant);

            return ResponseEntity.ok().body(new Response<Participant>(
                    HttpServletResponse.SC_OK,
                    "",
                    particpantCreated));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<Participant>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error creating new participant",
                    null));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteParticipant(@PathVariable("id") Long groupid) {
        try {
            participantServices.delete(groupid);

            return ResponseEntity.ok().body(new Response<String>(
                    HttpServletResponse.SC_OK,
                    "",
                    null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "error deleting participant",
                    null));

        }
    }

}
