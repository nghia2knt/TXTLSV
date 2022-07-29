package com.example.txtlserver.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class EditProfileRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Name cannot be null")
    @Size(min = 4, max = 64,  message = "Name must have 4 - 64 characters")
    private String name;

    @NotBlank(message = "Phone number cannot be null")
    @Size(min = 10, max = 10,message = "Phone number must have 10 characters")
    private String phoneNumber;

    @NotBlank(message = "ID Card cannot be null")
    private String idCard;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDate birthDay;

    @NotBlank(message = "Address cannot be null")
    @Size(min = 4, max = 64,  message = "Address must have 4 - 64 characters")
    private String address;
}
