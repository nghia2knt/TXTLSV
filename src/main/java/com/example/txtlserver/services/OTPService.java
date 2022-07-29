package com.example.txtlserver.services;

import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.exception.CustomExceptionHandler;
import com.example.txtlserver.models.User;
import com.example.txtlserver.repositories.UserRepository;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {
    private static final Integer EXPIRE_MINS = 4;
    private LoadingCache<String, Integer> otpCache;

    @Autowired
    private UserRepository userRepository;


    public OTPService(){
        super();
        otpCache = CacheBuilder.newBuilder().
                expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }


    public int generateOTP(String key){
        Optional<User> user = userRepository.findByEmail(key);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found User with email "+key);
        }
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        otpCache.put(key, otp);
        return otp;
    }

    public int getOtp(String key){
        try{
            return otpCache.get(key);
        }catch (Exception e){
            return 0;
        }
    }

    public void clearOTP(String key){
        otpCache.invalidate(key);
    }
}