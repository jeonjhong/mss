package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.CategoryPriceResponse;
import com.musinsa.model.entity.BrandEntity;
import com.musinsa.model.entity.CategoryEntity;
import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.model.entity.ProductEntity;
import com.musinsa.model.enums.Category;
import com.musinsa.repository.BrandRepository;
import com.musinsa.repository.CategoryRepository;
import com.musinsa.repository.MinMaxPriceRepository;
import com.musinsa.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PriceServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MinMaxPriceRepository minMaxPriceRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private PriceService priceService;

    @Test
    void testGetCategoryPriceComparison_Success() {
        Category category = Category.ACCESSORY;
        MinMaxPriceEntity lowestPriceEntity = new MinMaxPriceEntity();
        BrandEntity brandEntity = new BrandEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        brandEntity.setName("brand");
        lowestPriceEntity.setMinPriceProductId(1L);  // 최소 가격 상품 ID 설정
        lowestPriceEntity.setBrand(brandEntity);
        lowestPriceEntity.setCategory(categoryEntity);

        MinMaxPriceEntity highestPriceEntity = new MinMaxPriceEntity();
        highestPriceEntity.setMaxPriceProductId(2L);  // 최대 가격 상품 ID 설정
        highestPriceEntity.setMinPriceProductId(3L);
        highestPriceEntity.setBrand(brandEntity);
        highestPriceEntity.setCategory(categoryEntity);

        when(minMaxPriceRepository.findLowestPriceByCategory(category)).thenReturn(Optional.of(lowestPriceEntity));
        when(minMaxPriceRepository.findHighestPriceByCategory(category)).thenReturn(Optional.of(highestPriceEntity));

        CategoryPriceResponse response = priceService.getCategoryPriceComparison(category);

        assertNotNull(response);
        verify(minMaxPriceRepository).findLowestPriceByCategory(category);
        verify(minMaxPriceRepository).findHighestPriceByCategory(category);
    }

    @Test
    void testGetCategoryPriceComparison_NoLowestPrice() {
        Category category = Category.ACCESSORY;

        when(minMaxPriceRepository.findLowestPriceByCategory(category)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            priceService.getCategoryPriceComparison(category);
        });

        assertEquals("No lowest price found for this category.", exception.getMessage());
        verify(minMaxPriceRepository).findLowestPriceByCategory(category);
        verify(minMaxPriceRepository).findHighestPriceByCategory(category);
    }

    @Test
    void testGetCategoryPriceComparison_NoHighestPrice() {
        // Given
        Category category = Category.ACCESSORY;

        MinMaxPriceEntity lowestPriceEntity = new MinMaxPriceEntity();
        BrandEntity brandEntity = new BrandEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        lowestPriceEntity.setMinPriceProductId(1L);  // Set a valid product ID
        lowestPriceEntity.setBrand(brandEntity);
        lowestPriceEntity.setCategory(categoryEntity);

        // Mock the repository calls
        when(minMaxPriceRepository.findLowestPriceByCategory(category)).thenReturn(Optional.of(lowestPriceEntity));
        when(minMaxPriceRepository.findHighestPriceByCategory(category)).thenReturn(Optional.empty());

        // When & Then
        ApiException exception = assertThrows(ApiException.class, () -> {
            priceService.getCategoryPriceComparison(category);
        });

        // Assert that the correct exception message is thrown
        assertEquals("No highest price found for this category.", exception.getMessage());

        // Verify that the repository methods were called
        verify(minMaxPriceRepository).findLowestPriceByCategory(category);
        verify(minMaxPriceRepository).findHighestPriceByCategory(category);
    }


    // Test for getMinMaxPrices method
    @Test
    void testGetMinMaxPrices_Success() {
        BrandEntity brand1 = new BrandEntity();
        BrandEntity brand2 = new BrandEntity();

        CategoryEntity category1 = new CategoryEntity();
        CategoryEntity category2 = new CategoryEntity();

        List<BrandEntity> brands = Arrays.asList(brand1, brand2);
        List<CategoryEntity> categories = Arrays.asList(category1, category2);

        ProductEntity minProduct1 = new ProductEntity();
        ReflectionTestUtils.setField(minProduct1, "id", 1L);
        ReflectionTestUtils.setField(minProduct1, "price", 100);

        ProductEntity maxProduct1 = new ProductEntity();
        ReflectionTestUtils.setField(maxProduct1, "id", 2L);
        ReflectionTestUtils.setField(maxProduct1, "price", 200);

        ProductEntity minProduct2 = new ProductEntity();
        ReflectionTestUtils.setField(minProduct2, "id", 3L);
        ReflectionTestUtils.setField(minProduct2, "price", 300);

        ProductEntity maxProduct2 = new ProductEntity();
        ReflectionTestUtils.setField(maxProduct2, "id", 4L);
        ReflectionTestUtils.setField(maxProduct2, "price", 400);

        when(brandRepository.findAll()).thenReturn(brands);
        when(categoryRepository.findAll()).thenReturn(categories);

        when(productRepository.findTopByBrandAndCategoryOrderByPriceAsc(any(), any())).thenReturn(Optional.of(minProduct1), Optional.of(minProduct2));
        when(productRepository.findTopByBrandAndCategoryOrderByPriceDesc(any(), any())).thenReturn(Optional.of(maxProduct1), Optional.of(maxProduct2));

        List<MinMaxPriceEntity> result = priceService.getMinMaxPrices();

        assertNotNull(result);

        // Verify that the correct number of calls were made to the repositories
        verify(productRepository).findByBrandsAndCategories(any(), any());
    }
}
