package com.elnatal.promo_quoter.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartQuoteResponse {
    private List<LineItemResponse> lineItems;
    private List<AppliedPromotionResponse> appliedPromotions;
    private BigDecimal originalTotal;
    private BigDecimal totalDiscount;
    private BigDecimal finalTotal;

    // Constructors
    public CartQuoteResponse() {}

    public CartQuoteResponse(List<LineItemResponse> lineItems,
                             List<AppliedPromotionResponse> appliedPromotions,
                             BigDecimal originalTotal, BigDecimal totalDiscount,
                             BigDecimal finalTotal) {
        this.lineItems = lineItems;
        this.appliedPromotions = appliedPromotions;
        this.originalTotal = originalTotal;
        this.totalDiscount = totalDiscount;
        this.finalTotal = finalTotal;
    }

    // Getters and Setters
    public List<LineItemResponse> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItemResponse> lineItems) { this.lineItems = lineItems; }

    public List<AppliedPromotionResponse> getAppliedPromotions() { return appliedPromotions; }
    public void setAppliedPromotions(List<AppliedPromotionResponse> appliedPromotions) {
        this.appliedPromotions = appliedPromotions;
    }

    public BigDecimal getOriginalTotal() { return originalTotal; }
    public void setOriginalTotal(BigDecimal originalTotal) { this.originalTotal = originalTotal; }

    public BigDecimal getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(BigDecimal totalDiscount) { this.totalDiscount = totalDiscount; }

    public BigDecimal getFinalTotal() { return finalTotal; }
    public void setFinalTotal(BigDecimal finalTotal) { this.finalTotal = finalTotal; }
}