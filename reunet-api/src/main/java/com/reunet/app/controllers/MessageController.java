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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.Message;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.security.jwt.JwtUtils;
import com.reunet.app.services.MessageServices;
import com.reunet.app.services.UserServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/message")
public class MessageController {

    @Autowired
    UserServices userServices;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MessageServices messageServices;

    @PostMapping("")
    public ResponseEntity<Response<Message>> postMessage(@RequestBody Message message,
            @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtils.getEmailFromToken(token.substring(7));
            Long userId = userServices.getIdFromEmail(email);
            message.setUserId(userId);

            Message messageCreated = messageServices.create(message);

            return ResponseEntity.ok().body(new Response<Message>(
                    HttpServletResponse.SC_OK,
                    "",
                    messageCreated));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<Message>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("")
    public ResponseEntity<Response<List<Message>>> getAllMessagesByGroupId(@RequestParam Long groupid) {
        try {

            List<Message> messages = messageServices.findAllMessages(groupid);

            return ResponseEntity.ok().body(new Response<List<Message>>(
                    HttpServletResponse.SC_OK,
                    "",
                    messages));

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new Response<List<Message>>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<List<Message>>> deleteMensajeById(@PathVariable("id") Long id) {
        try {

            messageServices.delete(id);

            return ResponseEntity.ok().body(new Response<List<Message>>(
                    HttpServletResponse.SC_OK,
                    "",
                    null));

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new Response<List<Message>>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }
}
