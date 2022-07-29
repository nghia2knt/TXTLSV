package com.example.txtlserver.forms;


import com.example.txtlserver.enumEntity.StatusInvoiceType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class EditStatusRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "status cannot be null")
    private StatusInvoiceType status;



}
