package com.example.txtlserver.services;

import com.example.txtlserver.forms.FilterCarAdmin;
import com.example.txtlserver.forms.FilterFormCarsUser;
import com.example.txtlserver.models.Car;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CarService {
    Car createCar(Car car);
    Car editCar(Car car);
    List<Car> findAll();
    Car findById(Long id);
    List<Car> findCars(Pageable pageable, FilterFormCarsUser filter);
    List<Car> findCarsAdmin(Pageable pageable, FilterCarAdmin filter);

}
