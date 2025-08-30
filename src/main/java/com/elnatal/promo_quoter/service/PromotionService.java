package com.elnatal.promo_quoter.service;

import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public List<Promotion> createPromotions(List<Promotion> promotions) {
        return promotionRepository.saveAll(promotions);
    }

    @Transactional(readOnly = true)
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
}
