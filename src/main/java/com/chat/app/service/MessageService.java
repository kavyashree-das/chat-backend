package com.chat.app.service;

import com.chat.app.entity.Message;
import com.chat.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        // repository.save() acts as an INSERT (if ID is null) or UPDATE (if ID exists)
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderByCreatedAtAsc();
    }
}
