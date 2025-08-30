package com.elnatal.promo_quoter.service.promotion;

import com.elnatal.promo_quoter.domain.CustomerSegment;
import com.elnatal.promo_quoter.domain.Product;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PromotionContext {
    private final Map<UUID, Product> products;
    private final Map<UUID, Integer> quantities;
    private final Map<UUID, BigDecimal> lineItemTotals;
    private final Map<UUID, BigDecimal> discounts;
    private final CustomerSegment customerSegment;

    public PromotionContext(CustomerSegment customerSegment) {
        this.customerSegment = customerSegment;
        this.products = new HashMap<>();
        this.quantities = new HashMap<>();
        this.lineItemTotals = new HashMap<>();
        this.discounts = new HashMap<>();
    }

    public void addLineItem(Product product, Integer quantity) {
        products.put(product.getId(), product);
        quantities.put(product.getId(), quantity);
        BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
        lineItemTotals.put(product.getId(), lineTotal);
        discounts.put(product.getId(), BigDecimal.ZERO);
    }

    public void applyDiscount(UUID productId, BigDecimal discountAmount) {
        BigDecimal currentDiscount = discounts.getOrDefault(productId, BigDecimal.ZERO);
        discounts.put(productId, currentDiscount.add(discountAmount));
    }

    public BigDecimal getLineItemTotal(UUID productId) {
        return lineItemTotals.getOrDefault(productId, BigDecimal.ZERO);
    }

    public BigDecimal getLineItemDiscount(UUID productId) {
        return discounts.getOrDefault(productId, BigDecimal.ZERO);
    }

    public BigDecimal getLineItemFinalTotal(UUID productId) {
        BigDecimal original = getLineItemTotal(productId);
        BigDecimal discount = getLineItemDiscount(productId);
        return original.subtract(discount).max(BigDecimal.ZERO);
    }

    // Getters
    public Map<UUID, Product> getProducts() { return products; }
    public Map<UUID, Integer> getQuantities() { return quantities; }
    public Map<UUID, BigDecimal> getLineItemTotals() { return lineItemTotals; }
    public Map<UUID, BigDecimal> getDiscounts() { return discounts; }
    public CustomerSegment getCustomerSegment() { return customerSegment; }
}