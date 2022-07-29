package com.example.txtlserver.services;

import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.models.Message;
import com.example.txtlserver.models.User;
import com.example.txtlserver.repositories.MessageRepository;
import com.example.txtlserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Long userId, String content, Long toId){
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with id "+userId);
        }
        Optional<User> toUser = userRepository.findById(toId);
        if (!toUser.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with id "+toId);
        }
        Message message = Message.builder().fromUser(foundUser.get()).content(content).createAt(LocalDateTime.now()).toUser(toUser.get()).build();
        Message createMess = messageRepository.save(message);
        return createMess;
    }
    public List<Message> getMessages(){
        List<Message> messages = messageRepository.findAllByOrderByCreateAtDesc();
        return messages;
    }

    public List<Message> getMessagesByUser(Long userId){
        Optional<User> toUser = userRepository.findById(userId);
        if (!toUser.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with id "+userId);
        }
        List<Message> messages = messageRepository.findMessageByToUserOrderByCreateAtDesc(toUser.get());
        return messages;
    }
}
