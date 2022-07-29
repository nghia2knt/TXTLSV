package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "Email not valid")
    @NotBlank(message = "Email not null")
    private String email;

    @NotBlank(message = "Password not null")
    @Size(min = 4, max = 16, message = "password must have 4 - 16 characters")
    private String password;

}
