package com.example.txtlserver.controllers;

import com.example.txtlserver.forms.CreateBrandRequest;
import com.example.txtlserver.forms.EditBrandRequest;
import com.example.txtlserver.jwt.JwtUtils;
import com.example.txtlserver.models.Brand;
import com.example.txtlserver.models.ResponseObject;
import com.example.txtlserver.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("")
    public ResponseEntity<ResponseObject> findAll(){
        List<Brand> listBrand = brandService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query list brand success", listBrand)
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> findBrandById(@PathVariable("id") Long id){
        Brand brand = brandService.findById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query find brand success", brand)
        );
    }
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> createBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest){
        Brand brand = Brand.builder().name(createBrandRequest.getName()).createAt(LocalDateTime.now()).build();
        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Create brand success", brandService.createBrand(brand))
            );
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject> editBrand(@Valid @RequestBody EditBrandRequest editBrandRequest, @PathVariable("id") Long id){
        Brand brand = Brand.builder().name(editBrandRequest.getName()).id(id).build();
        return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Edit brand success", brandService.editBrand(brand))
           );
    }
}
