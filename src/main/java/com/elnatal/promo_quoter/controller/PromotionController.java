package com.elnatal.promo_quoter.controller;

import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @Autowired
    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<List<Promotion>> createPromotions(@Valid @RequestBody List<Promotion> promotions) {
        List<Promotion> createdPromotions = promotionService.createPromotions(promotions);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromotions);
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }
}
