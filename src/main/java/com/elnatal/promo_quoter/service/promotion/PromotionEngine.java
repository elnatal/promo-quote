package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.dto.AppliedPromotionResponse;
import com.elnatal.promo_quoter.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionEngine {

    private final PromotionRepository promotionRepository;
    private final PromotionRuleFactory ruleFactory;

    @Autowired
    public PromotionEngine(PromotionRepository promotionRepository,
                           PromotionRuleFactory ruleFactory) {
        this.promotionRepository = promotionRepository;
        this.ruleFactory = ruleFactory;
    }

    public List<AppliedPromotionResponse> applyPromotions(PromotionContext context) {
        List<AppliedPromotionResponse> appliedPromotions = new ArrayList<>();
        List<Promotion> activePromotions = promotionRepository.findActivePromotionsOrderedByPriority();

        for (Promotion promotion : activePromotions) {
            try {
                PromotionRule rule = ruleFactory.getRule(promotion.getType());

                if (rule.canApply(promotion, context)) {
                    AppliedPromotionResponse appliedPromotion = rule.apply(promotion, context);
                    appliedPromotions.add(appliedPromotion);
                }
            } catch (Exception e) {
                // Log error but continue with other promotions
                System.err.println("Error applying promotion " + promotion.getId() + ": " + e.getMessage());
            }
        }

        return appliedPromotions;
    }
}