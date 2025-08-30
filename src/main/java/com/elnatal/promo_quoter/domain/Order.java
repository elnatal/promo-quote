package com.elnatal.promo_quoter.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey;

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_segment", nullable = false)
    private CustomerSegment customerSegment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Constructors
    public Order() {
        this.createdAt = LocalDateTime.now();
    }

    public Order(String idempotencyKey, BigDecimal totalPrice, CustomerSegment customerSegment) {
        this();
        this.idempotencyKey = idempotencyKey;
        this.totalPrice = totalPrice;
        this.customerSegment = customerSegment;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public CustomerSegment getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(CustomerSegment customerSegment) { this.customerSegment = customerSegment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}