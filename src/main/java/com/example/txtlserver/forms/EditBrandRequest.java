package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
public class EditBrandRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Brand name cannot be null")
    private String name;
}
