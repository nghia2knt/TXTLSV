
package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class ChangePasswordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 4, max = 16, message = "Password must have 4 - 16 characters")
    private String oldPassword;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 4, max = 16, message = "Password must have 4 - 16 characters")
    private String newPassword;
}
