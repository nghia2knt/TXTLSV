package com.example.txtlserver.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CreateVoteRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @Min(value=1,message = "point must >= 1")
    @Max(value=10,message = "point must <= 10")
    private int point;
    @NotBlank(message = "content cannot be null")
    private String content;
}
