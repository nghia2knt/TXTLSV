package com.example.txtlserver.models;


import com.example.txtlserver.enumEntity.StatusInvoiceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Invoice{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double totalPrice;
    @Enumerated(EnumType.STRING)
    private StatusInvoiceType statusType;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime createAt;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime startTime;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime endTime;

    private String customerName;
    private String customerIDCard;
    private String customerPhone;
    private String customerEmail;

    private String carName;
    private String carBrand;
    private String carLicensePlate;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne()
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

}