package com.elnatal.promo_quoter.dto;

import com.elnatal.promo_quoter.domain.CustomerSegment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class CartRequest {

    @NotEmpty(message = "Cart items cannot be empty")
    @Valid
    private List<CartItemRequest> items;

    @NotNull(message = "Customer segment is required")
    private CustomerSegment customerSegment;

    // Constructors
    public CartRequest() {}

    public CartRequest(List<CartItemRequest> items, CustomerSegment customerSegment) {
        this.items = items;
        this.customerSegment = customerSegment;
    }

    // Getters and Setters
    public List<CartItemRequest> getItems() { return items; }
    public void setItems(List<CartItemRequest> items) { this.items = items; }

    public CustomerSegment getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(CustomerSegment customerSegment) { this.customerSegment = customerSegment; }
}
