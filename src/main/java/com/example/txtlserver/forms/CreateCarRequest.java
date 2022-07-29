package com.example.txtlserver.forms;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CreateCarRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Car name cannot be null")
    private String name;
    @Min(value=0,message = "Brand must > 0")
    private Long brandId;
    @NotBlank(message = "Model cannot be null")
    private String model;
    @NotBlank(message = "License plate cannot be null")
    private String licensePlate;
    @NotBlank(message = "Transmission cannot be null")
    private String transmission;
    @NotBlank(message = "Engine type cannot be null")
    private String engineType;
    @Min(value=1,message = "Seats must > 1")
    @Max(value=100,message = "Seats must <100")
    private int seats;
    @Min(value=1000,message = "Price must > 1")
    @Max(value=100000000,message = "Price must < 100000000")
    private double price;
    @NotBlank(message = "Image cannot be null")
    private String image;
}