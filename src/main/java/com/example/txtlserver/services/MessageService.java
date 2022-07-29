package com.example.txtlserver.services;

import com.example.txtlserver.models.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(Long userId, String content, Long toId);
    List<Message> getMessages();
    List<Message> getMessagesByUser(Long userId);
}
