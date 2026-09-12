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

### GET Products — Search by Product Name

**Endpoint:** 

GET /products?search=keyboard  
GET /products?search=KEYBOARD

**Expected Result:** 
The API should return products whose names contain the search term, regardless of capitalization.

**Test Result:** 
Pass

**Observed Response:**

```json
[
    {
        "description": "Mechanical keyboard",
        "lowStockThreshold": 3,
        "name": "Keyboard",
        "productId": 2,
        "quantityInStock": 10,
        "sellingPrice": 79.99
    }
]
```

**Notes:**

Notes: The search successfully returned the product matching the search term "keyboard" and "KEYBOARD" . The search is case-insensitive.


## GET Products — Search with no matching products

**Endpoint:**
GET /products?search=zzzzzz  


**Expected Result:**
The API should return 200 OK with an empty array [].

**Test Result:**
Pass

**Observed Response:**

```json
[]
```
**Notes:**

The empty array indicates that no product matched the search term zzzzzz.

## GET Products — Low-stock products

**Endpoint:**
GET /products/low-stock


**Expected Result:**
The API should return products where quantityInStock is less than or equal to lowStockThreshold.

**Test Result:**
Pass

**Observed Response:**

```json
[
  {
    "description": "Mechanical keyboard",
    "lowStockThreshold": 3,
    "name": "Keyboard",
    "productId": 2,
    "quantityInStock": 2,
    "sellingPrice": 79.99
  }
]
```

**Notes:**

This test confirms that the repository → service → controller → database low-stock functionality works correctly when quantityInStock <= lowStockThreshold.


## GET Products — Low-stock products

**Endpoint:**
GET /products?lowStock=true

**Expected Result:**
The API should return products where quantityInStock is less than or equal to lowStockThreshold.

**Test Result:**
Pass

**Observed Response:**

```json
[
  {
    "description": "Mechanical keyboard",
    "lowStockThreshold": 3,
    "name": "Keyboard",
    "productId": 2,
    "quantityInStock": 2,
    "sellingPrice": 79.99
  }
]
```

**Notes:**

This test confirms that products meeting the condition quantityInStock <= lowStockThreshold are returned by the low-stock filter.

---

### Test 2 — Product does not meet low-stock condition

**Endpoint:**
GET /products?lowStock=true

**Test Data:**

```text
quantityInStock = 10
lowStockThreshold = 3
```

**Expected Result:**
The API should exclude products where quantityInStock is greater than lowStockThreshold.

**Test Result:**
Pass

**Observed Response:**

```json
[]
```

**Notes:**
The Keyboard was excluded because quantityInStock (10) is greater than lowStockThreshold (3).

## GET Inventory — Calculate inventory value

**Endpoint:**  
GET /inventory/value

**Expected Result:**  
The API should return the total inventory value by multiplying each product's sellingPrice by its quantityInStock and adding the values together.

**Test Result:**  
Pass

**Observed Response:**

```text
799.90
```

**Notes:**

The Keyboard has a selling price of $79.99 and a quantity in stock of 10.

79.99 × 10 = 799.90

The API returned the expected total inventory value.


### Test 2 — Multiple products

**Endpoint:**  
GET /inventory/value

**Test Data:**

```text
Keyboard: $79.99 × 10 = $799.90
Mouse: $29.99 × 5 = $149.95
```
**Expected Result:**
The API should calculate the value of each product and return the combined inventory value.

**Test Result:**
Pass

**Observed Response:**
```
949.85
```

**Notes:**

The API correctly calculated the inventory value for multiple products.
```
$799.90 + $149.95 = $949.85
```

