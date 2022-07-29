package com.example.txtlserver.forms;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String idCard;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate birthDay;
    private String avatar;
    private String roles;
    private boolean isActive;
    private String address;
}
