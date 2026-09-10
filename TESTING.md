# Testing

## Product API

### GET All Products — Empty Database

**Endpoint:**
GET /products

**Expected Result:**
Returns a list of all products.

**Test Result:**
PASS

**Observed Response:**
```json
[]
```

Notes:
The endpoint successfully connected the Controller, Service, Repository, and MySQL database. The empty array indicated that no products existed in the database at the time of the test.

## POST Create Product

**Endpoint:**
POST /products

**Expected Result:**
Creates a new product and returns the saved product, including its generated product ID.

**Test Result:**
PASS

Request Body:
```json


{
    "name": "Laptop",
    "description": "Business laptop",
    "sellingPrice": 899.99,
    "quantityInStock": 10,
    "lowStockThreshold": 3
}
```
**Observed Response:**
```json
{
    "description": "Business laptop",
    "lowStockThreshold": 3,
    "name": "Laptop",
    "productId": 1,
    "quantityInStock": 10,
    "sellingPrice": 899.99
}
```
Notes:
The product was successfully created and saved to the MySQL database. The database generated productId 1.

## GET All Products

**Endpoint:**
GET /products

**Expected Result:**
Returns a list of all products.

**Test Result:**
PASS

**Observed Response:**
```json
[
{
"description": "Business laptop",
"lowStockThreshold": 3,
"name": "Laptop",
"productId": 1,
"quantityInStock": 10,
"sellingPrice": 899.99
}
]
```
Notes:

The endpoint successfully retrieved the product from the database through the Controller, Service, and Repository layers.


---

### GET Product by ID

**Endpoint:**
GET  /products/1

**Expected Result:**
Returns the product with the specified ID.

**Test Result:**
PASS

**Observed Response:**
```json
{
    "description": "Business laptop",
    "lowStockThreshold": 3,
    "name": "Laptop",
    "productId": 1,
    "quantityInStock": 10,
    "sellingPrice": 899.99
}
```

### PUT Update Product

**Endpoint:**

PUT /products/1

**Expected Result:**
Updates the existing product with the specified ID and returns the updated product.

**Test Result:**
PASS

**Request Body:**

```json
{
    "name": "Laptop",
    "description": "Business laptop - updated",
    "sellingPrice": 949.99,
    "quantityInStock": 15,
    "lowStockThreshold": 5
}

```

### GET Product by ID — Verify Updated Product

**Endpoint:**
GET /products/1

**Expected Result:**
Returns the updated product with the new values stored in the database.

**Test Result:**
PASS

**Observed Response:**

```json
{
    "description": "Business laptop - updated",
    "lowStockThreshold": 5,
    "name": "Laptop",
    "productId": 1,
    "quantityInStock": 15,
    "sellingPrice": 949.99
}
```

### DELETE  Product

**Endpoint:**
DELETE  /products/1

**Expected Result:**
Returns a successful HTTP status code.

**Test Result:**
PASS

**Observed Response:**
200 OK

### GET Product by ID — Product Not Found

**Endpoint:**
GET /products/1

**Expected Result:**
Returns HTTP 404 Not Found when the requested product does not exist.

**Test Result:**
PASS

**Observed Response:**
404 Not Found and
Product not found

**Notes:**

The endpoint correctly returned HTTP 404 Not Found after the requested product had been deleted. The GlobalExceptionHandler handled the ProductNotFoundException and returned the appropriate HTTP status.

---

### POST Create Product — Validation

**Endpoint:**
POST /products

**Expected Result:**
Returns HTTP 400 Bad Request when the request contains invalid product data.

**Test Result:**
PASS

**Request Body:**

```json
{
    "name": "",
    "description": "Invalid product test",
    "sellingPrice": -50.00,
    "quantityInStock": -10,
    "lowStockThreshold": -3
}
```
**Observed Response:**
400 Bad Request and
Validation failed

**Notes:**

The request was rejected because the product contained invalid values. The validation rules correctly rejected the blank name, negative selling price, negative quantity in stock, and negative low-stock threshold.



### PUT Update Product — Validation

**Endpoint:**
PUT /products/{id}

**Expected Result:**
Returns HTTP 400 Bad Request when the request contains invalid product data.

**Test Result:**
PASS

**Request Body:**

```json
{
    "name": "",
    "description": "Invalid update test",
    "sellingPrice": -10.00,
    "quantityInStock": -5,
    "lowStockThreshold": -2
}
```
**Observed Response:**
400 Bad Request  and
Validation failed

**Notes:**

The request was rejected because the product contained invalid values. The validation rules correctly rejected the blank name, negative selling price, negative quantity in stock, and negative low-stock threshold.


### POST Product — Specific Validation Errors

**Endpoint:**
POST /products

**Expected Result:**
The API should reject invalid product data and return a specific validation error for each invalid field: name, selling price, quantity in stock, and low stock threshold.

**Test Result:**
Pass

**Observed Response:**

```json
{
    "quantityInStock": "must be greater than or equal to 0",
    "sellingPrice": "must be greater than or equal to 0.00",
    "lowStockThreshold": "must be greater than or equal to 0",
    "name": "must not be blank"
}
```

**Notes:**

The validation successfully rejected the invalid product and returned individual validation messages for each field that failed validation. The product was not created. 

