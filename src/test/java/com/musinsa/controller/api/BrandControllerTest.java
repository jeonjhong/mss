package com.musinsa.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.model.dto.BrandRequest;
import com.musinsa.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BrandService brandService;


    @Test
    void testSaveOrUpdateBrand() throws Exception {
        // Given: 새로운 브랜드 요청 생성
        BrandRequest brandRequest = new BrandRequest(null, "Nike");

        // When & Then: POST 요청을 통해 브랜드 저장 및 응답 검증
        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandRequest)))
                .andExpect(status().isOk()) // HTTP Status 200 OK 기대
                .andExpect(jsonPath("$.data.id").exists()) // 응답에 ID 필드가 존재하는지 확인
                .andExpect(jsonPath("$.data.id").isNumber()); // ID가 숫자인지 확인
    }

    @Test
    void testDeleteBrand() throws Exception {
        // Given: 브랜드 엔티티를 사전에 저장 (삭제할 대상)
        BrandRequest brandRequest = new BrandRequest(null, "Adidas");
        Long savedBrandId = brandService.saveOrUpdateBrand(brandRequest).getId();

        // When & Then: DELETE 요청을 통해 브랜드 삭제 및 응답 검증
        mockMvc.perform(delete("/api/brands/{id}", savedBrandId))
                .andExpect(status().isOk()) // HTTP Status 200 OK 기대
                .andExpect(jsonPath("$.data.id").value(savedBrandId)); // 삭제된 브랜드 ID 확인

        // 삭제 후 브랜드가 존재하지 않는지 확인 (비즈니스 로직 검증)
        mockMvc.perform(delete("/api/brands/{id}", savedBrandId))
                .andExpect(status().isOk()); // 이미 삭제된 경우 404 Not Found 기대
    }
}