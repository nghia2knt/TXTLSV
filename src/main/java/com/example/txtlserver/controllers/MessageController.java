package com.example.txtlserver.controllers;

import com.example.txtlserver.forms.CreateMessageRequest;
import com.example.txtlserver.forms.CreateVoteRequest;
import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.models.User;
import com.example.txtlserver.services.MessageService;
import com.example.txtlserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createMessage(@Valid @RequestBody CreateMessageRequest createMessageRequest, @PathVariable("id") Long userId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Create message success", messageService.createMessage(foundUser.getId(), createMessageRequest.getContent(), userId))
        );
    }
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getMessage(){
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get message success",messageService.getMessages())
        );
    }

    @GetMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getMessageSelf(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get message success", messageService.getMessagesByUser(foundUser.getId()))
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getMessageByUser(@PathVariable("id") Long toId){
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get message success",messageService.getMessagesByUser(toId))
        );
    }
}
