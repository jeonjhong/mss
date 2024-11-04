package com.musinsa.service;

import com.musinsa.exception.ApiException;
import com.musinsa.model.dto.CheapestBrandResponse;
import com.musinsa.model.dto.CheapestProductsResponse;
import com.musinsa.model.dto.CommonIdResponse;
import com.musinsa.model.dto.ProductRequest;
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
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MinMaxPriceRepository minMaxPriceRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Spy
    @InjectMocks
    private ProductService productService;


    @Test
    void testGetCheapestPriceOfAllCategory_Success() {
        List<CategoryEntity> categories = Arrays.asList(new CategoryEntity(), new CategoryEntity());

        BrandEntity brand = new BrandEntity();
        brand.setName("Test Brand");

        CategoryEntity category = new CategoryEntity();
        ReflectionTestUtils.setField(category, "name", Category.HAT);

        MinMaxPriceEntity minMaxPrice1 = new MinMaxPriceEntity();
        minMaxPrice1.setBrand(brand);
        minMaxPrice1.setCategory(category);

        MinMaxPriceEntity minMaxPrice2 = new MinMaxPriceEntity();
        minMaxPrice2.setBrand(brand);
        minMaxPrice2.setCategory(category);

        List<MinMaxPriceEntity> minMaxPrices = Arrays.asList(minMaxPrice1, minMaxPrice2);

        when(categoryRepository.findAll()).thenReturn(categories);
        when(minMaxPriceRepository.findByCategoryIn(categories)).thenReturn(minMaxPrices);

        CheapestProductsResponse response = productService.getCheapestPriceOfAllCategory();

        assertNotNull(response);
        verify(categoryRepository).findAll();
        verify(minMaxPriceRepository).findByCategoryIn(categories);
    }

    @Test
    void testGetCheapestBrandForAllCategories_Success() {
        List<CategoryEntity> categories = Arrays.asList(new CategoryEntity(), new CategoryEntity());
        List<BrandEntity> brands = Arrays.asList(new BrandEntity(), new BrandEntity());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(productRepository.findAllBrands()).thenReturn(brands);


        doReturn(Optional.of(new CheapestBrandResponse("brand", Collections.emptyList()))).when(productService).calculateBrandTotalPrice(anyList(), any(BrandEntity.class));

        CheapestBrandResponse response = productService.getCheapestBrandForAllCategories();

        assertNotNull(response);
        verify(categoryRepository).findAll();
        verify(productRepository).findAllBrands();
    }

    @Test
    void testSaveOrUpdateProduct_Create() {
        ProductRequest productRequest = new ProductRequest();
        ReflectionTestUtils.setField(productRequest, "brandId", 1L);
        ReflectionTestUtils.setField(productRequest, "categoryId", 1L);

        BrandEntity brand = new BrandEntity();
        CategoryEntity category = new CategoryEntity();

        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(category));

        ProductEntity productEntity = new ProductEntity();
        ReflectionTestUtils.setField(productEntity, "price", 1);

        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        CommonIdResponse response = productService.saveOrUpdateProduct(productRequest);

        assertNotNull(response);
        verify(brandRepository).findById(productRequest.getBrandId());
        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void testSaveOrUpdateProduct_Update() {
        ProductRequest productRequest = new ProductRequest();
        ReflectionTestUtils.setField(productRequest, "id", 1L);
        ReflectionTestUtils.setField(productRequest, "brandId", 2L);
        ReflectionTestUtils.setField(productRequest, "categoryId", 3L);

        BrandEntity brand = new BrandEntity();
        CategoryEntity category = new CategoryEntity();

        when(brandRepository.findById(productRequest.getBrandId())).thenReturn(Optional.of(brand));
        when(categoryRepository.findById(productRequest.getCategoryId())).thenReturn(Optional.of(category));

        ProductEntity existingProduct = mock(ProductEntity.class);

        when(productRepository.findById(productRequest.getId())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(existingProduct);

        CommonIdResponse commonIdResponse = CommonIdResponse.of(existingProduct);
        when(existingProduct.toResponse()).thenReturn(commonIdResponse);

        CommonIdResponse response = productService.saveOrUpdateProduct(productRequest);

        assertNotNull(response);

        // Verify that the correct repository methods were called
        verify(brandRepository).findById(productRequest.getBrandId());
        verify(categoryRepository).findById(productRequest.getCategoryId());
        verify(productRepository).save(any(ProductEntity.class));
    }

    @Test
    void testDeleteProduct_Success() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(true);

        productService.deleteProduct(productId);

        verify(productRepository).existsById(productId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        Long productId = 1L;

        when(productRepository.existsById(productId)).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> {
            productService.deleteProduct(productId);
        });

        assertEquals("Product not found", exception.getMessage());

        // Verify that the delete method was not called on the repository
        verify(productRepository).existsById(productId);
        verify(productRepository, never()).deleteById(anyLong());
    }
}