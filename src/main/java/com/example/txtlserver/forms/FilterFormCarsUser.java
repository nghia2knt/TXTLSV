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
public class FilterFormCarsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String brand;
    private String transmission;
    private String seats;
    private String engine;
    private String fromDate;
    private String toDate;
    private String name;
    private String model;
    private int size;
    private int page;
    private Sort sort;
}
