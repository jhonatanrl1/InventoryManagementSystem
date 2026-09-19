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

## DEF-002 — ProductSupplier Hibernate ID Mapping Error

**Status:** 
Resolved

**Problem:**
Creating a ProductSupplier relationship resulted in a 500 Internal Server Error.

**Error:**
Could not assign id from null association 'product'

**Cause:**
The original @IdClass mapping caused Hibernate to have difficulty
mapping the Product and Supplier associations to the composite ID.

**Final Result:**
Changed ProductSupplier to use @EmbeddedId with @MapsId for the
Product and Supplier relationships.

**Verification:**
POST /product-suppliers successfully created the relationship
with product ID 2, supplier ID 2, and purchase price 47.00.

##  Defects - Recursive JSON Response in Purchase API

**Affected Feature:**
Purchase Management

**Affected Endpoints:**
```text
POST /purchases
```

```text
GET /purchases
```

```text
GET /purchases/{id}
```


**Status:**
Resolved

**Problem:**

After implementing the Purchase and PurchaseItem entities, the Purchase API returned an extremely long JSON response containing repeated Purchase and PurchaseItem objects.

The response repeatedly followed the relationship:
```text
Purchase → PurchaseItem → Purchase → PurchaseItem → ...
```


This caused the same Purchase information to be serialized repeatedly instead of returning a single Purchase with its associated PurchaseItems.


**Cause:**

The problem was caused by the bidirectional relationship between Purchase and PurchaseItem.

In Purchase.java, a Purchase contains a list of PurchaseItems:
```java
@OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
private List<PurchaseItem> purchaseItems;
```

In PurchaseItem.java, each PurchaseItem contains a reference back to its Purchase:
```java
@ManyToOne
@JoinColumn(name = "purchase_id")
private Purchase purchase;
```

This creates the following object relationship:
```text
Purchase → PurchaseItems → Purchase
```


When Spring Boot converted the Purchase object into JSON, Jackson followed both sides of the relationship. It serialized the Purchase, then its PurchaseItems, then the Purchase inside each PurchaseItem, which contained the PurchaseItems again.


**Solution:**

The @JsonIgnore annotation was added to the purchase field in PurchaseItem.java.
```java
@ManyToOne
@JoinColumn(name = "purchase_id")
@JsonIgnore
private Purchase purchase;
```


The required import was also added:
```java
import com.fasterxml.jackson.annotation.JsonIgnore;
```


@JsonIgnore tells Jackson not to include the purchase field when converting a PurchaseItem into JSON.

The JPA relationship itself was not removed or changed. The PurchaseItem still maintains its relationship with the Purchase for database persistence and application logic.

The change only prevents the parent Purchase from being serialized again inside the PurchaseItem JSON response.


**Verification:**

After adding @JsonIgnore, the Purchase endpoints were tested again.
```text
POST /purchases
```


Returned a normal Purchase response containing:

- Purchase ID

- Purchase date

- Supplier

- Purchase items

- Product

- Quantity

- Purchase price

```text
GET /purchases
```


Returned all existing Purchase records without recursive Purchase/PurchaseItem nesting.
```text
GET /purchases/{id}
```


Returned the requested Purchase without recursively including the Purchase again inside its PurchaseItem.


**Result:**

The recursive JSON response was eliminated while preserving the underlying JPA relationship between Purchase and PurchaseItem.

The final API response now exposes the relevant PurchaseItem information without repeatedly serializing the parent Purchase.

