package com.example.txtlserver.forms;

import com.example.txtlserver.models.Issue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateIssueInvoiceRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Issue> issues;
    private Long userId;
}
