# Promo Quoter

A Spring Boot REST service that calculates cart pricing with pluggable promotion rules and handles inventory reservation with proper concurrency control.

## Features

- **Product Management**: Create and manage products with categories, pricing, and stock levels
- **Promotion Engine**: Pluggable promotion rules using Strategy and Chain of Responsibility patterns
- **Cart Pricing**: Calculate itemized pricing with applied promotions
- **Inventory Reservation**: Atomic stock reservation with optimistic locking
- **Concurrency Safety**: Pessimistic locking for cart confirmation to prevent overselling
- **Idempotency**: Prevent duplicate orders using idempotency keys

## Technology Stack

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Maven**

## Design Patterns Implemented

### Strategy Pattern
Each promotion type (PERCENT_OFF_CATEGORY, BUY_X_GET_Y) implements the `PromotionRule` interface with its own calculation logic.

### Factory Pattern
`PromotionRuleFactory` creates appropriate rule instances based on promotion type.

### Chain of Responsibility
`PromotionEngine` applies promotions in priority order, allowing composition of multiple rules.

## API Endpoints

### Products
- `POST /products` - Create product(s)
- `GET /products` - List all products

### Promotions
- `POST /promotions` - Create promotion(s)
- `GET /promotions` - List all promotions

### Cart Operations
- `POST /cart/quote` - Calculate cart pricing with promotions
- `POST /cart/confirm` - Reserve inventory and create order

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Start the Application
```bash
mvn spring-boot:run
`````

The application will start on `http://localhost:8080`

### Access H2 Console (Development)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Sample API Requests

### 1. Create Products
```bash
curl -X POST http://localhost:8080/products \
-H "Content-Type: application/json" \
-d '[
        {
            "name": "iPhone 15",
            "category": "ELECTRONICS",
            "price": 999.99,
            "stock": 50
        },
        {
            "name": "Nike Shoes",
            "category": "CLOTHING",
            "price": 129.99,
            "stock": 100
        }
    ]'
````

### 2. Create Promotions
```bash
curl -X POST http://localhost:8080/promotions \
-H "Content-Type: application/json" \
-d '[
        {
            "name": "15% off Electronics",
            "type": "PERCENT_OFF_CATEGORY",
            "targetCategory": "ELECTRONICS",
            "discountPercentage": 15.00,
            "active": true,
            "priorityOrder": 1
        }
    ]'
```

### 3. Get Cart Quote
```bash
curl -X POST http://localhost:8080/cart/quote \
-H "Content-Type: application/json" \
-d '{
        "items": [
            {
                "productId": "550e8400-e29b-41d4-a716-446655440001",
                "qty": 2
            }
        ],
        "customerSegment": "REGULAR"
    }'
```

### 4. Confirm Cart (with Idempotency)
```bash
curl -X POST http://localhost:8080/cart/confirm \
-H "Content-Type: application/json" \
-H "Idempotency-Key: order-123-456" \
-d '{
        "items": [
            {
                "productId": "550e8400-e29b-41d4-a716-446655440001",
                "qty": 2
            }
        ],
        "customerSegment": "REGULAR"
    }'
```

## Sample Response - Cart Quote
```json
{
    "lineItems": [
        {
            "productId": "550e8400-e29b-41d4-a716-446655440001",
            "productName": "iPhone 15",
            "quantity": 2,
            "unitPrice": 999.99,
            "originalTotal": 1999.98,
            "discountAmount": 299.997,
            "finalTotal": 1699.983
        }
    ], 
    "appliedPromotions": [
        {
            "promotionId": "660e8400-e29b-41d4-a716-446655440001",
            "promotionName": "15% off Electronics",
            "description": "15% off ELECTRONICS category",
            "discountAmount": 299.997
        }
    ],
    "originalTotal": 1999.98,
    "totalDiscount": 299.997,
    "finalTotal": 1699.983
}
```

## Concurrency & Data Consistency

### Stock Management
- Uses pessimistic locking (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) during cart confirmation
- Prevents overselling under concurrent access
- Returns `409 CONFLICT` when insufficient stock

### Idempotency
- Optional `Idempotency-Key` header prevents duplicate order processing
- The same key returns existing order details without re-processing

### Optimistic Locking
- Product entity uses `@Version` for optimistic locking
- Handles concurrent updates gracefully

## Error Handling

The application provides structured error responses:

- `400 Bad Request` - Validation errors
- `404 Not Found` - Product not found
- `409 Conflict` - Insufficient stock
- `500 Internal Server Error` - Unexpected errors
