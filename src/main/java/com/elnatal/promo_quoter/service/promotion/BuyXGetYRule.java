package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.Product;
import com.elnatal.promo_quoter.domain.Promotion;
import com.elnatal.promo_quoter.dto.AppliedPromotionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuyXGetYRule implements PromotionRule {

    @Override
    public boolean canApply(Promotion promotion, PromotionContext context) {
        Integer quantity = context.getQuantities().get(promotion.getTargetProductId());
        return quantity != null && quantity >= promotion.getBuyQuantity();
    }

    @Override
    public AppliedPromotionResponse apply(Promotion promotion, PromotionContext context) {
        Integer quantity = context.getQuantities().get(promotion.getTargetProductId());
        Product product = context.getProducts().get(promotion.getTargetProductId());

        // Calculate how many free items the customer gets
        int freeItems = (quantity / promotion.getBuyQuantity()) * promotion.getGetQuantity();

        // Calculate discount (price of free items)
        BigDecimal discountAmount = product.getPrice().multiply(BigDecimal.valueOf(freeItems));

        context.applyDiscount(promotion.getTargetProductId(), discountAmount);

        String description = String.format("Buy %d get %d free on %s",
                promotion.getBuyQuantity(), promotion.getGetQuantity(), product.getName());

        return new AppliedPromotionResponse(
                promotion.getId(),
                promotion.getName(),
                description,
                discountAmount
        );
    }
}