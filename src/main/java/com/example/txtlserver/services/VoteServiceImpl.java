package com.example.txtlserver.services;

import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.models.Car;
import com.example.txtlserver.models.Invoice;
import com.example.txtlserver.models.User;
import com.example.txtlserver.models.Vote;
import com.example.txtlserver.repositories.CarRepository;
import com.example.txtlserver.repositories.UserRepository;
import com.example.txtlserver.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoteServiceImpl implements VoteService{
    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public Vote createVote(Long userId, Long carId, int point, String content) {
        Optional<Car> foundCar = carRepository.findById(carId);
        if (!foundCar.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found car with id "+carId);
        }
        Optional<User> foundUser = userRepository.findById(userId);
        if (!foundUser.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with id "+userId);
        }
        Vote vote = Vote.builder()
                .user(foundUser.get())
                .car(foundCar.get())
                .content(content)
                .point(point)
                .createAt(LocalDateTime.now())
                .build();
        Vote createdVote = voteRepository.save(vote);
        return createdVote;

    }
    public List<Vote> getVoteByCarId(Long carId) {
        Optional<Car> foundCar = carRepository.findById(carId);
        if (!foundCar.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found car with id "+carId);
        }
        List<Vote> votes = voteRepository.findVoteByCarOrderByCreateAtDesc(foundCar.get());
        return votes;
    }
    public List<Vote> getVote() {
        List<Vote> votes = voteRepository.findAllByOrderByCreateAtDesc();
        return votes;
    }
    public void deleteVote(Long id){
        Optional<Vote> foundVote = voteRepository.findById(id);
        if (!foundVote.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found vote with id "+id);
        }
        voteRepository.delete(foundVote.get());
    }
}
