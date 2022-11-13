package com.reunet.app.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.Message;
import com.reunet.app.repository.MessageRepository;

@Service
public class MessageServices {

    @Autowired
    MessageRepository messageRepository;

    public Message create(Message message) {
        message.setCreatedAt(new Date());
        return messageRepository.save(message);
    }

    public void delete(Long id) {
        messageRepository.deleteById(id);
    }

    public Message update(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAllMessages(Long groupId) {
        return messageRepository.findAllMessagesByGroupId(groupId);
    }
}
