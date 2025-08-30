package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.Product;
import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.dto.AppliedPromotionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Component
public class PercentOffCategoryRule implements PromotionRule {

    @Override
    public boolean canApply(Promotion promotion, PromotionContext context) {
        return context.getProducts().values().stream()
                .anyMatch(product -> product.getCategory().equals(promotion.getTargetCategory()));
    }

    @Override
    public AppliedPromotionResponse apply(Promotion promotion, PromotionContext context) {
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (UUID productId : context.getProducts().keySet()) {
            Product product = context.getProducts().get(productId);

            if (product.getCategory().equals(promotion.getTargetCategory())) {
                BigDecimal lineTotal = context.getLineItemTotal(productId);
                BigDecimal discountAmount = lineTotal
                        .multiply(promotion.getDiscountPercentage())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                context.applyDiscount(productId, discountAmount);
                totalDiscount = totalDiscount.add(discountAmount);
            }
        }

        String description = String.format("%s%% off %s category",
                promotion.getDiscountPercentage(), promotion.getTargetCategory());

        return new AppliedPromotionResponse(
                promotion.getId(),
                promotion.getName(),
                description,
                totalDiscount
        );
    }
}