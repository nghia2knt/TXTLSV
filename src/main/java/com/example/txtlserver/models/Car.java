package com.example.txtlserver.models;

import com.example.txtlserver.enumEntity.EngineType;
import com.example.txtlserver.enumEntity.TransmissionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Car{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
    private String model;
    private String licensePlate;
    @Enumerated(EnumType.STRING)
    private TransmissionType transmission;
    @Enumerated(EnumType.STRING)
    private EngineType engineType;
    private int seats;
    private double price;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime createAt;
    @JsonFormat(pattern="HH:mm dd-MM-yyyy")
    private LocalDateTime deleteAt;
    private String image;
    private boolean isLock;


}
