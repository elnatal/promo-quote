package com.elnatal.promo_quoter.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Promotion name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Promotion type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionType type;

    @Column(name = "target_category")
    @Enumerated(EnumType.STRING)
    private ProductCategory targetCategory;

    @Column(name = "target_product_id")
    private UUID targetProductId;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "buy_quantity")
    private Integer buyQuantity;

    @Column(name = "get_quantity")
    private Integer getQuantity;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "priority_order")
    private Integer priorityOrder = 0;

    // Constructors
    public Promotion() {}

    // Factory methods for different promotion types
    public static Promotion createPercentOffCategory(String name, ProductCategory category, BigDecimal percentage, Integer priority) {
        Promotion promotion = new Promotion();
        promotion.name = name;
        promotion.type = PromotionType.PERCENT_OFF_CATEGORY;
        promotion.targetCategory = category;
        promotion.discountPercentage = percentage;
        promotion.priorityOrder = priority;
        return promotion;
    }

    public static Promotion createBuyXGetY(String name, UUID productId, Integer buyQty, Integer getQty, Integer priority) {
        Promotion promotion = new Promotion();
        promotion.name = name;
        promotion.type = PromotionType.BUY_X_GET_Y;
        promotion.targetProductId = productId;
        promotion.buyQuantity = buyQty;
        promotion.getQuantity = getQty;
        promotion.priorityOrder = priority;
        return promotion;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public PromotionType getType() { return type; }
    public void setType(PromotionType type) { this.type = type; }

    public ProductCategory getTargetCategory() { return targetCategory; }
    public void setTargetCategory(ProductCategory targetCategory) { this.targetCategory = targetCategory; }

    public UUID getTargetProductId() { return targetProductId; }
    public void setTargetProductId(UUID targetProductId) { this.targetProductId = targetProductId; }

    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }

    public Integer getBuyQuantity() { return buyQuantity; }
    public void setBuyQuantity(Integer buyQuantity) { this.buyQuantity = buyQuantity; }

    public Integer getGetQuantity() { return getQuantity; }
    public void setGetQuantity(Integer getQuantity) { this.getQuantity = getQuantity; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getPriorityOrder() { return priorityOrder; }
    public void setPriorityOrder(Integer priorityOrder) { this.priorityOrder = priorityOrder; }
}