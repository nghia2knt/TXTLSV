package com.example.txtlserver.services;


import com.example.txtlserver.models.Vote;

import java.util.List;

public interface VoteService {
    Vote createVote(Long userId, Long carId, int point, String content);
    List<Vote> getVoteByCarId(Long carId);
    void deleteVote(Long id);
    List<Vote> getVote();
}
