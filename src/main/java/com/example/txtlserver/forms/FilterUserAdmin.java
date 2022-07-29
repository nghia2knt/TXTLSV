package com.example.txtlserver.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterUserAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String birthday;
    private String email;
    private String idCard;
    private String phoneNumber;
    private String fromDate;
    private String toDate;
    private int size;
    private int page;
    private Sort sort;
}
