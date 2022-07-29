package com.example.txtlserver.services;

import com.example.txtlserver.exception.CustomException;
import com.example.txtlserver.models.Brand;
import com.example.txtlserver.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    private BrandRepository brandRepository;

    @Override
    public Brand createBrand(Brand brand) {
        if (brandRepository.findBrandByNameOrderByCreateAtDesc(brand.getName()).isPresent()){
            throw new CustomException(HttpStatus.BAD_REQUEST,"Brand exist name "+brand.getName());
        }
        return brandRepository.save(brand);
    }

    @Override
    public Brand editBrand(Brand brand) {
        if (!brandRepository.existsById(brand.getId()))
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found brand with id = "+brand.getId());
        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAllByOrderByCreateAtDesc();
    }

    @Override
    public Brand findById(Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (!brand.isPresent()){
            throw new CustomException(HttpStatus.NOT_FOUND,"Not found brand with id = "+id);
        }
        return brand.get();
    }
}
