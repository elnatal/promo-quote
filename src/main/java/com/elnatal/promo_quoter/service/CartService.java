package com.elnatal.promo_quoter.service;

import com.elnatal.promo_quoter.domain.Order;
import com.elnatal.promo_quoter.domain.Product;
import com.elnatal.promo_quoter.dto.*;
import com.elnatal.promo_quoter.exception.InsufficientStockException;
import com.elnatal.promo_quoter.exception.ProductNotFoundException;
import com.elnatal.promo_quoter.repository.OrderRepository;
import com.elnatal.promo_quoter.repository.ProductRepository;
import com.elnatal.promo_quoter.service.promotion.PromotionContext;
import com.elnatal.promo_quoter.service.promotion.PromotionEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PromotionEngine promotionEngine;

    @Autowired
    public CartService(ProductRepository productRepository,
                       OrderRepository orderRepository,
                       PromotionEngine promotionEngine) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.promotionEngine = promotionEngine;
    }

    @Transactional(readOnly = true)
    public CartQuoteResponse calculateQuote(CartRequest request) {
        // Validate and fetch products
        List<UUID> productIds = request.getItems().stream()
                .map(CartItemRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findAllById(productIds);

        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("One or more products not found");
        }

        // Create promotion context
        PromotionContext context = new PromotionContext(request.getCustomerSegment());

        // Add line items to context
        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (CartItemRequest item : request.getItems()) {
            Product product = productMap.get(item.getProductId());
            context.addLineItem(product, item.getQty());
        }

        // Apply promotions
        List<AppliedPromotionResponse> appliedPromotions = promotionEngine.applyPromotions(context);

        // Build response
        return buildQuoteResponse(context, appliedPromotions);
    }

    @Transactional
    public CartConfirmResponse confirmCart(CartRequest request, String idempotencyKey) {
        // Check for existing order with same idempotency key
        if (idempotencyKey != null) {
            var existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
            if (existingOrder.isPresent()) {
                Order order = existingOrder.get();
                return new CartConfirmResponse(
                        order.getId(),
                        order.getTotalPrice(),
                        "Order already processed"
                );
            }
        }

        // Fetch products with pessimistic lock
        List<UUID> productIds = request.getItems().stream()
                .map(CartItemRequest::getProductId)
                .collect(Collectors.toList());

        List<Product> products = productRepository.findByIdsWithLock(productIds);

        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("One or more products not found");
        }

        // Validate stock availability
        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        for (CartItemRequest item : request.getItems()) {
            Product product = productMap.get(item.getProductId());
            if (product.getStock() < item.getQty()) {
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product %s. Available: %d, Requested: %d",
                                product.getName(), product.getStock(), item.getQty())
                );
            }
        }

        // Calculate final price
        CartQuoteResponse quote = calculateQuote(request);

        // Reserve inventory
        for (CartItemRequest item : request.getItems()) {
            Product product = productMap.get(item.getProductId());
            product.decrementStock(item.getQty());
            productRepository.save(product);
        }

        // Create order
        Order order = new Order(idempotencyKey, quote.getFinalTotal(), request.getCustomerSegment());
        order = orderRepository.save(order);

        return new CartConfirmResponse(
                order.getId(),
                order.getTotalPrice(),
                "Order confirmed successfully"
        );
    }

    private CartQuoteResponse buildQuoteResponse(PromotionContext context,
                                                 List<AppliedPromotionResponse> appliedPromotions) {
        List<LineItemResponse> lineItems = new ArrayList<>();
        BigDecimal originalTotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (UUID productId : context.getProducts().keySet()) {
            Product product = context.getProducts().get(productId);
            Integer quantity = context.getQuantities().get(productId);
            BigDecimal lineTotal = context.getLineItemTotal(productId);
            BigDecimal lineDiscount = context.getLineItemDiscount(productId);
            BigDecimal lineFinal = context.getLineItemFinalTotal(productId);

            lineItems.add(new LineItemResponse(
                    productId,
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    lineTotal,
                    lineDiscount,
                    lineFinal
            ));

            originalTotal = originalTotal.add(lineTotal);
            totalDiscount = totalDiscount.add(lineDiscount);
        }

        BigDecimal finalTotal = originalTotal.subtract(totalDiscount);

        return new CartQuoteResponse(
                lineItems,
                appliedPromotions,
                originalTotal,
                totalDiscount,
                finalTotal
        );
    }
}