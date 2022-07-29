package com.example.txtlserver.controllers;

import com.example.txtlserver.enumEntity.EngineType;
import com.example.txtlserver.enumEntity.TransmissionType;
import com.example.txtlserver.forms.*;
import com.example.txtlserver.models.Brand;
import com.example.txtlserver.models.Car;
import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.models.User;
import com.example.txtlserver.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarService carService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> findCars(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(name = "transmission", required = false, defaultValue = "") String transmission,
            @RequestParam(name = "seats", required = false, defaultValue = "") String  seats,
            @RequestParam(name = "fromDate", required = false, defaultValue = "") String  fromDate,
            @RequestParam(name = "toDate", required = false, defaultValue = "") String  toDate,
            @RequestParam(name = "name", required = false, defaultValue = "") String  name,
            @RequestParam(name = "engine", required = false, defaultValue = "") String  engine,
            @RequestParam(name = "model", required = false, defaultValue = "") String  model


    ){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("c.id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("c.id").descending();
        }
        Pageable pageable = PageRequest.of(page-1, size, sortable);
        FilterFormCarsUser filter = FilterFormCarsUser.builder()
                .brand(brand)
                .seats(seats)
                .transmission(transmission)
                .fromDate(fromDate)
                .toDate(toDate)
                .name(name)
                .engine(engine)
                .page(page)
                .size(size)
                .sort(sortable)
                .model(model)
                .build();
        List<Car> carList = carService.findCars(pageable,filter);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query find all car list success", carList)
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> findCarsAdmin(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "DESC") String sort,
            @RequestParam(name = "id", required = false, defaultValue = "") String id,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(name = "transmission", required = false, defaultValue = "") String transmission,
            @RequestParam(name = "seats", required = false, defaultValue = "") String  seats,
            @RequestParam(name = "fromDate", required = false, defaultValue = "") String  fromDate,
            @RequestParam(name = "toDate", required = false, defaultValue = "") String  toDate,
            @RequestParam(name = "name", required = false, defaultValue = "") String  name,
            @RequestParam(name = "engine", required = false, defaultValue = "") String  engine,
            @RequestParam(name = "model", required = false, defaultValue = "") String  model

    ){
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("c.id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("c.id").descending();
        }
        Pageable pageable = PageRequest.of(page-1, size, sortable);
        FilterCarAdmin filter = FilterCarAdmin.builder()
                .id(id)
                .brand(brand)
                .seats(seats)
                .transmission(transmission)
                .fromDate(fromDate)
                .toDate(toDate)
                .name(name)
                .engine(engine)
                .page(page)
                .size(size)
                .sort(sortable)
                .model(model)
                .build();
        List<Car> carList = carService.findCarsAdmin(pageable,filter);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query find all car list success", carList)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findCarById(@PathVariable("id") Long id){
        Car car = carService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query find car success", car)
        );
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createCar(@Valid @RequestBody CreateCarRequest createCarRequest){
        Brand brand = brandService.findById(createCarRequest.getBrandId());
        Car car = Car.builder()
                .name(createCarRequest.getName())
                .model(createCarRequest.getModel())
                .licensePlate(createCarRequest.getLicensePlate())
                .transmission(TransmissionType.valueOf(createCarRequest.getTransmission()))
                .engineType(EngineType.valueOf(createCarRequest.getEngineType()))
                .seats(createCarRequest.getSeats())
                .price(createCarRequest.getPrice())
                .brand(brand)
                .image(createCarRequest.getImage())
                .createAt(LocalDateTime.now())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Create car success", carService.createCar(car))
        );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editCar(@Valid @RequestBody EditCarRequest editCarRequest, @PathVariable("id") Long id){
        Brand brand = brandService.findById(editCarRequest.getBrandId());
        Car car = Car.builder()
                .id(id)
                .name(editCarRequest.getName())
                .model(editCarRequest.getModel())
                .licensePlate(editCarRequest.getLicensePlate())
                .transmission(TransmissionType.valueOf(editCarRequest.getTransmission()))
                .engineType(EngineType.valueOf(editCarRequest.getEngineType()))
                .seats(editCarRequest.getSeats())
                .price(editCarRequest.getPrice())
                .brand(brand)
                .image(editCarRequest.getImage())
                .build();
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Edit car success", carService.editCar(car))
        );
    }
    @PostMapping("/{id}/vote")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createVote(@Valid @RequestBody CreateVoteRequest createVoteRequest, @PathVariable("id") Long carId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getEmail();
        User foundUser = userService.getUserByEmail(email);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Create vote success", voteService.createVote(foundUser.getId(),carId, createVoteRequest.getPoint(),createVoteRequest.getContent()))
        );
    }
    @GetMapping("/{id}/vote")
    public ResponseEntity<ResponseObject> getCarVote(@PathVariable("id") Long carId){
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Get vote success",voteService.getVoteByCarId(carId))
        );
    }

}
