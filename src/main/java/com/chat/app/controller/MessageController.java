package com.chat.app.controller;

import com.chat.app.entity.Message;
import com.chat.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Local directory to store uploaded files
    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/message")
    public ResponseEntity<Message> postMessage(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        String fileUrl = null;

        if (file != null && !file.isEmpty()) {
            try {
                // Ensure directory exists
                File directory = new File(UPLOAD_DIR);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Create a unique filename
                String originalFilename = file.getOriginalFilename();
                String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + extension;

                // Save file locally
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(file.getInputStream(), path);

                fileUrl = "/uploads/" + fileName;
            } catch (IOException e) {
                return ResponseEntity.status(500).build();
            }
        }

        Message message = new Message(text, fileUrl);
        Message savedMessage = messageService.saveMessage(message);

        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }
}
