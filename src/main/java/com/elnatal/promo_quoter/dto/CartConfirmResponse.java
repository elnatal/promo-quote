package com.elnatal.promo_quoter.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class CartConfirmResponse {
    private UUID orderId;
    private BigDecimal finalPrice;
    private String message;

    // Constructors
    public CartConfirmResponse() {}

    public CartConfirmResponse(UUID orderId, BigDecimal finalPrice, String message) {
        this.orderId = orderId;
        this.finalPrice = finalPrice;
        this.message = message;
    }

    // Getters and Setters
    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
