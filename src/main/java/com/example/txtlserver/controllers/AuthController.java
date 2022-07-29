package com.example.txtlserver.controllers;

import com.example.txtlserver.enumEntity.RoleType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.*;
import com.example.txtlserver.models.*;
import com.example.txtlserver.jwt.JwtUtils;
import com.example.txtlserver.services.EmailService;
import com.example.txtlserver.services.OTPService;
import com.example.txtlserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    public OTPService otpService;

    @Autowired
    public EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> register(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRoles(RoleType.ROLE_USER);
        user.setAddress(signUpRequest.getAddress());
        userService.signUp(signUpRequest);
        int otp = otpService.generateOTP(signUpRequest.getEmail());
        System.out.println(otp);
        emailService.sendOtpMessage(signUpRequest.getEmail(),"OTP IN TXTL","This is OTP: "+otp);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Signup success", "")
        );
    }

    @PostMapping("/otp")
    public ResponseEntity<ResponseObject> generateOtp(@Valid @RequestBody GenerateOTPRequest generateOTPRequest) throws MessagingException {
        String email = generateOTPRequest.getEmail();
        int otp = otpService.generateOTP(email);
        System.out.println(otp);
        emailService.sendOtpMessage(email,"OTP IN TXTL","This is OTP: "+otp);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Generate and send to email success", "")
        );
    }

    @PutMapping("/active")
    public ResponseEntity<ResponseObject> activateUser(@Valid @RequestBody ActivateUserRequest activateUserRequest) {
        String email = activateUserRequest.getEmail();
        int serverOtp = otpService.getOtp(email);
        int requestOtp = activateUserRequest.getOtp();
        if (requestOtp == serverOtp) {
            if (userService.active(email)) {
                otpService.clearOTP(email);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Active success", "")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "OTP is not correct", "")
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Login Fail: "+e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        return ResponseEntity.ok(new ResponseObject("ok", "Login success",jwt));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResponseObject> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        String email = resetPasswordRequest.getEmail();
        int serverOtp = otpService.getOtp(email);
        int requestOtp = resetPasswordRequest.getOtp();
        if (requestOtp == serverOtp) {
            if (userService.resetPassword(email,resetPasswordRequest.getNewPassword())) {
                otpService.clearOTP(email);
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Change password success", "")
                );
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseObject("failed", "OTP is not correct", "")
        );
    }
}




