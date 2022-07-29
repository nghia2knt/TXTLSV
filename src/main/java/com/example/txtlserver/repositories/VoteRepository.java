package com.example.txtlserver.repositories;

import com.example.txtlserver.models.Car;
import com.example.txtlserver.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    List<Vote> findVoteByCarOrderByCreateAtDesc(Car car);
    List<Vote> findAllByOrderByCreateAtDesc();
}
