package com.elnatal.promo_quoter.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AppliedPromotionResponse {
    private UUID promotionId;
    private String promotionName;
    private String description;
    private BigDecimal discountAmount;

    // Constructors
    public AppliedPromotionResponse() {}

    public AppliedPromotionResponse(UUID promotionId, String promotionName,
                                    String description, BigDecimal discountAmount) {
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.description = description;
        this.discountAmount = discountAmount;
    }

    // Getters and Setters
    public UUID getPromotionId() { return promotionId; }
    public void setPromotionId(UUID promotionId) { this.promotionId = promotionId; }

    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
}
