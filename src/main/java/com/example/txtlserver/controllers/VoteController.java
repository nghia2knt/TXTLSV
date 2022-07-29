package com.example.txtlserver.controllers;

import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.models.Vote;
import com.example.txtlserver.services.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> deleteVote(@PathVariable("id") Long voteId){
        voteService.deleteVote(voteId);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete vote success","ok")
        );
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getVotes(){
        List<Vote> votes = voteService.getVote();
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get vote success",votes)
        );
    }
}
