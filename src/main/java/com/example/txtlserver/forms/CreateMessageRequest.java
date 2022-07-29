package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CreateMessageRequest implements Serializable {
    @NotBlank(message = "content cannot be null")
    private String content;
}
