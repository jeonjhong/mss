package com.musinsa.batch;

import com.musinsa.model.entity.MinMaxPriceEntity;
import com.musinsa.repository.MinMaxPriceRepository;
import com.musinsa.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MinMaxPriceBatchService {
    private final MinMaxPriceRepository minMaxPriceRepository;
    private final PriceService priceService;

    @Transactional
    @Scheduled(cron = "0 * * * * ?")  // 매 분
    public void updateMinMaxPrice() {
        List<MinMaxPriceEntity> minMaxPrices = priceService.getMinMaxPrices();
        minMaxPriceRepository.saveAll(minMaxPrices);
    }
}
