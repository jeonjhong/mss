package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.BrandRequest;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.repository.BrandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    @Test
    void testSaveOrUpdateBrand_Update() {
        Long id = 1L;
        String newName = "Updated Brand";
        BrandRequest brandRequest = new BrandRequest(id, newName);
        BrandEntity existingBrand = new BrandEntity();
        existingBrand.setId(id);
        existingBrand.setName("Old Brand");

        when(brandRepository.findById(id)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(existingBrand);

        CommonIdResponse response = brandService.saveOrUpdateBrand(brandRequest);

        assertNotNull(response);
        assertEquals(id, response.getId());
        verify(brandRepository, times(1)).findById(id);
        verify(brandRepository, times(1)).save(existingBrand);
        assertEquals(newName, existingBrand.getName());
    }

    @Test
    @DisplayName("brand create")
    void testSaveOrUpdateBrand_Create() {
        // Given
        String name = "New Brand";
        BrandRequest brandRequest = new BrandRequest(null, name);
        BrandEntity newBrand = new BrandEntity();
        newBrand.setName(name);

        // Mock the save method to return the newBrand entity
        when(brandRepository.save(any(BrandEntity.class))).thenReturn(newBrand);

        // When
        CommonIdResponse response = brandService.saveOrUpdateBrand(brandRequest);

        // Then
        assertNotNull(response);  // Ensure the response is not null
        verify(brandRepository, times(0)).findById(anyLong());  // No findById call for new entity
        verify(brandRepository, times(1)).save(any(BrandEntity.class));
        assertEquals(name, newBrand.getName());
    }

    @Test
    void testDeleteBrand_Success() {
        Long id = 1L;

        when(brandRepository.existsById(id)).thenReturn(true);

        brandService.deleteBrand(id);

        verify(brandRepository, times(1)).existsById(id);
        verify(brandRepository, times(1)).deleteById(id);
    }


    @Test
    void testDeleteBrand_BrandNotFound() {
        Long id = 1L;

        when(brandRepository.existsById(id)).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> brandService.deleteBrand(id));

        assertEquals("Brand not found", exception.getMessage());
        verify(brandRepository, times(1)).existsById(id);
        verify(brandRepository, times(0)).deleteById(id);
    }
}