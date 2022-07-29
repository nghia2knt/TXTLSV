package com.example.txtlserver.controllers;

import com.example.txtlserver.forms.ChangePasswordRequest;
import com.example.txtlserver.forms.EditProfileRequest;
import com.example.txtlserver.forms.FilterUserAdmin;
import com.example.txtlserver.forms.UserResponse;
import com.example.txtlserver.jwt.JwtUtils;
import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.models.User;
import com.example.txtlserver.services.AmazonClient;
import com.example.txtlserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AmazonClient amazonClient;



    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findUsers(
            @RequestParam(name = "id", required = false, defaultValue = "") String  id,
            @RequestParam(name = "name", required = false, defaultValue = "") String  name,
            @RequestParam(name = "birthday", required = false, defaultValue = "") String  birthday,
            @RequestParam(name = "email", required = false, defaultValue = "") String  email,
            @RequestParam(name = "idCard", required = false, defaultValue = "") String idCard,
            @RequestParam(name = "phoneNumber", required = false, defaultValue = "") String phoneNumber,
            @RequestParam(name = "fromDay", required = false, defaultValue = "") String  fromDate,
            @RequestParam(name = "toDay", required = false, defaultValue = "") String  toDate,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort
            ){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page-1, size, sortable);
        FilterUserAdmin filter = FilterUserAdmin.builder()
                .id(id)
                .birthday(birthday)
                .fromDate(fromDate)
                .idCard(idCard)
                .email(email)
                .name(name)
                .page(page)
                .phoneNumber(phoneNumber)
                .size(size)
                .sort(sortable)
                .toDate(toDate)
                .build();
        List<User> listUser = userService.findUsers(pageable, filter);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query list user success", listUser)
        );
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getUserById(@PathVariable("id") Long id){
        User foundUser = userService.getUserByID(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "List User", foundUser)
        );
    }

    @GetMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> getUserByJWT(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        UserResponse foundUser = userService.getUserProfileByEmail(email);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get user success", foundUser)
        );
    }
    @PutMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editProfileUser(@Valid @RequestBody EditProfileRequest editProfileRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        UserResponse foundUser = userService.editUserProfile(email,editProfileRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update user success", foundUser)
        );
    }
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editProfileUserAdmin(@PathVariable("id") Long id, @Valid @RequestBody EditProfileRequest editProfileRequest){
        User user = userService.getUserByID(id);
        String email = user.getEmail();
        UserResponse foundUser = userService.editUserProfile(email,editProfileRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update user success", foundUser)
        );
    }
    @PutMapping("/password")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        UserResponse foundUser = userService.changePassword(email,changePasswordRequest);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change password user success", foundUser)
        );
    }

    @PostMapping("/avatar")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> changeAvatar(@RequestPart(value = "file") MultipartFile file) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        String url = this.amazonClient.uploadFile(file);
        UserResponse foundUser = userService.changeAvatar(email,url);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change avatar user success", foundUser)
        );
    }

    @PostMapping("/{id}/avatar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> changeAvatarAdmin(@PathVariable("id") Long id, @RequestPart(value = "file") MultipartFile file) {
        User user = userService.getUserByID(id);
        String email = user.getEmail();
        String url = this.amazonClient.uploadFile(file);
        UserResponse foundUser = userService.changeAvatar(email,url);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change avatar user success", foundUser)
        );
    }

    @PostMapping("/active-account/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> activeUserAdmin(@PathVariable("id") Long id) {
        User foundUser = userService.getUserByID(id);
        String email = foundUser.getEmail();
        userService.active(email);
        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Active success", "")
        );
    }
}
