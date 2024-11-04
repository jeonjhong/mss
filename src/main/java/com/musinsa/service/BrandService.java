package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.BrandRequest;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.repository.BrandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    // Add or Update Brand
    @Transactional
    public CommonIdResponse saveOrUpdateBrand(BrandRequest brandRequest) {
        BrandEntity brandEntity;
        if (Objects.nonNull(brandRequest.getId())) {  // Only update if ID is present
            // Update existing brandEntity
            brandEntity = brandRepository.findById(brandRequest.getId())
                    .orElseThrow(() -> new ApiException("Brand not found"));
            brandEntity.setName(brandRequest.getName());
        } else {
            // Create new brandEntity
            brandEntity = new BrandEntity();
            brandEntity.setName(brandRequest.getName());
        }

        return brandRepository.save(brandEntity).toResponse();
    }

    // Delete Brand by ID
    @Transactional
    public void deleteBrand(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ApiException("Brand not found");
        }
        brandRepository.deleteById(id);
    }
}
