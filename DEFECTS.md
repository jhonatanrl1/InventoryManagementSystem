# Defects

## Validation Returned 500 Instead of 400

**Area:**
Product API validation

**Initial Test:**
POST /products with invalid product data.

**Expected Result:**
400 Bad Request

**Actual Result:**
500 Internal Server Error

**Problem:**
The product validation rules correctly detected invalid values, but the validation exception was not being handled by the application's global exception handler. As a result, the API returned HTTP 500 instead of the appropriate HTTP 400 status.

**Fix:**
Added `MethodArgumentNotValidException` handling to `GlobalExceptionHandler` and configured it to return HTTP 400 Bad Request.

**Retest Result:**
PASS

**Final Result:**
Invalid product data now correctly returns:

```text
400 Bad Request
Validation failed
```

**Affected Validation Rules:**
- Product name cannot be blank.
- Selling price cannot be negative.
- Quantity in stock cannot be negative.
- Low-stock threshold cannot be negative.

