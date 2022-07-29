package com.example.txtlserver.services;
import com.example.txtlserver.enumEntity.RoleType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.*;
import com.example.txtlserver.models.User;
import com.example.txtlserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with id "+id);
        }
        return user.get();
    }

    @Override
    public boolean signUp(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail()))
            throw new CustomException(HttpStatus.BAD_REQUEST, "This email address is already being used");
        User user = User.builder()
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .roles(RoleType.ROLE_USER)
                .isActive(false)
                .isNonLock(true)
                .name(signupRequest.getName())
                .idCard(signupRequest.getIdCard())
                .birthDay(signupRequest.getBirthDay())
                .phoneNumber(signupRequest.getPhoneNumber())
                .createAt(LocalDateTime.now())
                .address(signupRequest.getAddress())
                .build();
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean active(String email){
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        if (user.get().isActive()){
            throw new CustomException(HttpStatus.BAD_REQUEST,"User is activated");
        }
        user.get().setActive(true);
        userRepository.save(user.get());
        return true;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        user.get().setPassword(encoder.encode(newPassword));
        userRepository.save(user.get());
        return true;
    }

    @Override
    public UserResponse editUserProfile(String email, EditProfileRequest editProfileRequest) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        User newUser = user.get();
        if (editProfileRequest.getBirthDay()!=null) {
            newUser.setBirthDay(editProfileRequest.getBirthDay());
        }
        newUser.setIdCard(editProfileRequest.getIdCard());
        newUser.setPhoneNumber(editProfileRequest.getPhoneNumber());
        newUser.setName(editProfileRequest.getName());
        newUser.setAddress(editProfileRequest.getAddress());
        userRepository.save(newUser);
        UserResponse userRes = UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .roles(newUser.getRoles().toString())
                .isActive(newUser.isActive())
                .name(newUser.getName())
                .idCard(newUser.getIdCard())
                .birthDay(newUser.getBirthDay())
                .phoneNumber(newUser.getPhoneNumber())
                .avatar(newUser.getAvatar())
                .address(newUser.getAddress())
                .build();
        return userRes;
    }
    @Override
    public UserResponse changePassword(String email, ChangePasswordRequest changePasswordRequest) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        User newUser = user.get();
        String oldPass = changePasswordRequest.getOldPassword();
        if (!encoder.matches(oldPass,newUser.getPassword())){
            throw new CustomException(HttpStatus.BAD_REQUEST,"Old password is not correct or something...");
        }
        newUser.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(newUser);
        UserResponse userRes = UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .roles(newUser.getRoles().toString())
                .isActive(newUser.isActive())
                .name(newUser.getName())
                .idCard(newUser.getIdCard())
                .birthDay(newUser.getBirthDay())
                .phoneNumber(newUser.getPhoneNumber())
                .avatar(newUser.getAvatar())
                .address(newUser.getAddress())
                .build();
        return userRes;
    }

    @Override
    public UserResponse changeAvatar(String email, String url) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        User newUser = user.get();
        newUser.setAvatar(url);
        userRepository.save(newUser);
        UserResponse userRes = UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .roles(newUser.getRoles().toString())
                .isActive(newUser.isActive())
                .name(newUser.getName())
                .idCard(newUser.getIdCard())
                .birthDay(newUser.getBirthDay())
                .phoneNumber(newUser.getPhoneNumber())
                .avatar(newUser.getAvatar())
                .address(newUser.getAddress())
                .build();
        return userRes;
    }

    @Override
    public List<User> findUsers(Pageable pageable, FilterUserAdmin filter) {
        if (filter.getFromDate().equalsIgnoreCase("")){
            filter.setFromDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime fromDate = LocalDateTime.parse(filter.getFromDate() , formatter);
        }
        if (filter.getToDate().equalsIgnoreCase("")){
            filter.setToDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime toDate = LocalDateTime.parse(filter.getToDate() , formatter);
        }
        if (filter.getName().equalsIgnoreCase("")){
            filter.setName(null);
        }
        if (filter.getId().equalsIgnoreCase("")) {
            filter.setId(null);
        }
        if (filter.getBirthday().equalsIgnoreCase("")) {
            filter.setBirthday(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime birthDay = LocalDateTime.parse(filter.getBirthday() , formatter);
        }
        if (filter.getEmail().equalsIgnoreCase("")) {
            filter.setEmail(null);
        }
        if (filter.getIdCard().equalsIgnoreCase("")) {
            filter.setIdCard(null);
        }
        if (filter.getPhoneNumber().equalsIgnoreCase("")) {
            filter.setPhoneNumber(null);
        }

        List<User> users = userRepository.findUsersAdmin(filter.getId(), filter.getSize(),filter.getPage()-1,filter.getName(), filter.getBirthday(), filter.getEmail(), filter.getIdCard(),filter.getPhoneNumber(),filter.getFromDate(),filter.getToDate());
        users.forEach((user)->{
            user.setPassword("");
        });
        return users;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        return user.get();
    }

    @Override
    public UserResponse getUserProfileByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found user with email "+email);
        }
        UserResponse userRes = UserResponse.builder()
                .id(user.get().getId())
                .email(user.get().getEmail())
                .roles(user.get().getRoles().toString())
                .isActive(user.get().isActive())
                .name(user.get().getName())
                .idCard(user.get().getIdCard())
                .birthDay(user.get().getBirthDay())
                .phoneNumber(user.get().getPhoneNumber())
                .avatar(user.get().getAvatar())
                .address(user.get().getAddress())
                .build();
        return userRes;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Not found user with email = " + email));
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .isActive(user.isActive())
                .isNonLock(user.isNonLock())
                .build();
    }



}
