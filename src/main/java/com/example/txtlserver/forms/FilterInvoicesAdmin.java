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
public class FilterInvoicesAdmin implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String carId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String customeridcard;
    private String fromDate;
    private String toDate;
    private String startTime;
    private String endTime;
    private String carName;
    private String carBrand;
    private String carLicensePlate;
    private String status;
    private int size;
    private int page;
    private Sort sort;
}
