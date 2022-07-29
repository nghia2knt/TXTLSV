package com.example.txtlserver.repositories;

import com.example.txtlserver.models.Message;
import com.example.txtlserver.models.User;
import com.example.txtlserver.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findMessageByToUserOrderByCreateAtDesc(User toUser);
    List<Message> findAllByOrderByCreateAtDesc();
}
