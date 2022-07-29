package com.example.txtlserver.services;

import com.example.txtlserver.enumEntity.EngineType;
import com.example.txtlserver.enumEntity.TransmissionType;
import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.forms.FilterCarAdmin;
import com.example.txtlserver.forms.FilterFormCarsUser;
import com.example.txtlserver.models.Brand;
import com.example.txtlserver.models.Car;
import com.example.txtlserver.repositories.BrandRepository;
import com.example.txtlserver.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService{
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public Car editCar(Car car) {
        if (!carRepository.existsById(car.getId())){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found car with id = "+car.getId());
        }
        return carRepository.save(car);
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car findById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (!car.isPresent()) {
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found car with id = "+id);
        }
        return car.get();
    }

    @Override
    public List<Car> findCars(Pageable pageable, FilterFormCarsUser filter) {
        if (!filter.getTransmission().equalsIgnoreCase("")){
            TransmissionType transmission = TransmissionType.valueOf(filter.getTransmission());
        }else{
            filter.setTransmission(null);
        }
        if (!filter.getSeats().equalsIgnoreCase("")){
            int seats = Integer.parseInt(filter.getSeats());
        }else{
            filter.setSeats(null);
        }
        if ((!filter.getFromDate().equalsIgnoreCase("")) && (!filter.getToDate().equalsIgnoreCase(""))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime fromDate = LocalDateTime.parse(filter.getFromDate(), formatter);
            LocalDateTime toDate = LocalDateTime.parse(filter.getToDate(), formatter);
        }
        if (!filter.getBrand().equalsIgnoreCase("")){
            Optional<Brand> brand = brandRepository.findById((long) Integer.parseInt(filter.getBrand()));
            if (!brand.isPresent()){
                throw new CustomException(HttpStatus.NOT_FOUND,"Not found brand = "+filter.getBrand());
            }
        }else{
            filter.setBrand(null);
        }
        if (!filter.getName().equalsIgnoreCase("")){

        }else{
            filter.setName(null);
        }

        if (!filter.getEngine().equalsIgnoreCase("")){
            EngineType engineType = EngineType.valueOf(filter.getEngine());
        }else{
            filter.setEngine(null);
        }
        if (!filter.getModel().equalsIgnoreCase("")){

        }else{
            filter.setModel(null);
        }
        List<Car> cars = carRepository.findCarsFilter(filter.getSize(),filter.getPage()-1,filter.getBrand(),filter.getTransmission(), filter.getSeats(), filter.getName(), filter.getEngine(),filter.getFromDate(),filter.getToDate(),filter.getModel());
        return cars;
    }

    @Override
    public List<Car> findCarsAdmin(Pageable pageable, FilterCarAdmin filter) {
        if (!filter.getTransmission().equalsIgnoreCase("")){
            TransmissionType transmission = TransmissionType.valueOf(filter.getTransmission());
        }else{
            filter.setTransmission(null);
        }
        if (!filter.getSeats().equalsIgnoreCase("")){
            int seats = Integer.parseInt(filter.getSeats());
        }else{
            filter.setSeats(null);
        }
        if (!filter.getBrand().equalsIgnoreCase("")){
            Optional<Brand> brand = brandRepository.findById((long) Integer.parseInt(filter.getBrand()));
            if (!brand.isPresent()){
                throw new CustomException(HttpStatus.NOT_FOUND,"Not found brand = "+filter.getBrand());
            }
        }else{
            filter.setBrand(null);
        }
        if (filter.getName().equalsIgnoreCase("")){
            filter.setName(null);
        }
        if (filter.getFromDate().equalsIgnoreCase("")){
            filter.setFromDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime fromDate = LocalDateTime.parse(filter.getFromDate() + " 00:00:00", formatter);
        }
        if (filter.getToDate().equalsIgnoreCase("")){
            filter.setToDate(null);
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            LocalDateTime toDate = LocalDateTime.parse(filter.getToDate() + " 00:00:00", formatter);
        }
        if (filter.getId().equalsIgnoreCase("")){
            filter.setId(null);
        }
        if (!filter.getEngine().equalsIgnoreCase("")){
            EngineType engineType = EngineType.valueOf(filter.getEngine());
        }else{
            filter.setEngine(null);
        }
        if (filter.getModel().equalsIgnoreCase("")){
            filter.setModel(null);
        }
        List<Car> cars = carRepository.findCarsAdmin(filter.getId(), filter.getSize(),filter.getPage()-1,filter.getBrand(),filter.getTransmission(), filter.getSeats(), filter.getName(), filter.getEngine(),filter.getFromDate(),filter.getToDate(),filter.getModel());
        return cars;
    }
}
