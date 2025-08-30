package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.dto.AppliedPromotionResponse;

public interface PromotionRule {
    boolean canApply(Promotion promotion, PromotionContext context);
    AppliedPromotionResponse apply(Promotion promotion, PromotionContext context);
}
