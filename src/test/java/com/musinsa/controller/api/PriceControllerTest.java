package com.musinsa.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.model.dto.ProductRequest;
import com.musinsa.service.PriceService;
import com.musinsa.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private PriceService priceService;


    @Test
    void testGetCheapestPrice() throws Exception {
        // Given: 제품 생성 및 저장 (최저가 제품 조회 대상)
        ProductRequest product1 = new ProductRequest();
        ReflectionTestUtils.setField(product1, "name", "Air Max");
        ReflectionTestUtils.setField(product1, "price", 150000);
        ReflectionTestUtils.setField(product1, "brandId", 1L);
        ReflectionTestUtils.setField(product1, "categoryId", 1L);
        ProductRequest product2 = new ProductRequest();
        ReflectionTestUtils.setField(product2, "name", "Superstar");
        ReflectionTestUtils.setField(product2, "brandId", 2L);
        ReflectionTestUtils.setField(product2, "price", 150000);
        ReflectionTestUtils.setField(product2, "categoryId", 1L);

        productService.saveOrUpdateProduct(product1);
        productService.saveOrUpdateProduct(product2);

        // When & Then: GET 요청을 통해 최저가 제품 목록 조회 및 응답 검증
        mockMvc.perform(get("/api/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.products").isArray())
                .andExpect(jsonPath("$.data.totalPrice").value(150000));
    }

    @Test
    void testGetCheapestBrand() throws Exception {
        // Given: 브랜드별 최저가 제품 생성 및 저장 (최저가 브랜드 조회 대상)

        ProductRequest nikeProduct = new ProductRequest();
        ReflectionTestUtils.setField(nikeProduct, "name", "Air Max");
        ReflectionTestUtils.setField(nikeProduct, "brandId", 1L);
        ReflectionTestUtils.setField(nikeProduct, "price", 150000);
        ReflectionTestUtils.setField(nikeProduct, "categoryId", 1L);

        ProductRequest adidasProduct = new ProductRequest();
        ReflectionTestUtils.setField(adidasProduct, "name", "Superstar");
        ReflectionTestUtils.setField(adidasProduct, "brandId", 2L);
        ReflectionTestUtils.setField(adidasProduct, "price", 150000);
        ReflectionTestUtils.setField(adidasProduct, "categoryId", 1L);

        productService.saveOrUpdateProduct(nikeProduct);
        productService.saveOrUpdateProduct(adidasProduct);

        // When & Then: GET 요청을 통해 최저가 브랜드 조회 및 응답 검증
        mockMvc.perform(get("/api/prices/cheapest-brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPrice").value(36100))
                .andExpect(jsonPath("$.data.products[0].name").value("Superstar"));

    }
}