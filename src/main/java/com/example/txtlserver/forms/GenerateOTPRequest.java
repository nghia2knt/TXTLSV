package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class GenerateOTPRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Username không được để trống")
    private String email;
}
