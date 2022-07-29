package com.example.txtlserver.forms;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CreateInvoiceRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime startTime;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime endTime;
    @Min(value=0,message = "Id must > 1")
    @Max(value=100000000,message = "Id must < 100000000")
    private Long carId;
}
