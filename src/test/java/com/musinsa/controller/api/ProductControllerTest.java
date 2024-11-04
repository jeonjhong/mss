package com.musinsa.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.model.dto.ProductRequest;
import com.musinsa.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;


    @Test
    void testSaveOrUpdateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        ReflectionTestUtils.setField(productRequest, "name", "Air Max");
        ReflectionTestUtils.setField(productRequest, "price", 150000);
        ReflectionTestUtils.setField(productRequest, "brandId", 1L);
        ReflectionTestUtils.setField(productRequest, "categoryId", 1L);
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").exists());
    }

    @Test
    void testDeleteProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        ReflectionTestUtils.setField(productRequest, "name", "Air Max");
        ReflectionTestUtils.setField(productRequest, "price", 150000);
        ReflectionTestUtils.setField(productRequest, "brandId", 1L);
        ReflectionTestUtils.setField(productRequest, "categoryId", 1L);
        Long savedProductId = productService.saveOrUpdateProduct(productRequest).getId();

        mockMvc.perform(delete("/api/products/{id}", savedProductId))
                .andExpect(status().isOk());

    }
}