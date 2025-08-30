package com.elnatal.promo_quoter.controller;

import com.elnatal.promo_quoter.dto.CartConfirmResponse;
import com.elnatal.promo_quoter.dto.CartQuoteResponse;
import com.elnatal.promo_quoter.dto.CartRequest;
import com.elnatal.promo_quoter.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/quote")
    public ResponseEntity<CartQuoteResponse> getQuote(@Valid @RequestBody CartRequest request) {
        CartQuoteResponse quote = cartService.calculateQuote(request);
        return ResponseEntity.ok(quote);
    }

    @PostMapping("/confirm")
    public ResponseEntity<CartConfirmResponse> confirmCart(
            @Valid @RequestBody CartRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        CartConfirmResponse response = cartService.confirmCart(request, idempotencyKey);
        return ResponseEntity.ok(response);
    }
}
