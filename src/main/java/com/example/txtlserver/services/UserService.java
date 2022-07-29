package com.example.txtlserver.services;

import com.example.txtlserver.forms.*;
import com.example.txtlserver.models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User getUserByID(Long id);
    User getUserByEmail(String email);
    UserResponse getUserProfileByEmail(String email);
    boolean signUp(SignupRequest signupRequest);
    boolean active(String email);
    boolean resetPassword(String email, String newPassword);
    UserResponse editUserProfile(String email, EditProfileRequest editProfileRequest);
    UserResponse changePassword(String email, ChangePasswordRequest changePasswordRequest);
    UserResponse changeAvatar(String email,String url);
    List<User> findUsers(Pageable pageable, FilterUserAdmin filter);
}
