package com.example.txtlserver.services;

import com.example.txtlserver.models.Brand;

import java.util.List;

public interface BrandService {
    Brand createBrand(Brand brand);
    Brand editBrand(Brand brand);
    List<Brand> findAll();
    Brand findById(Long id);
}
