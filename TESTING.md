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

## POST Suppliers — Create Supplier

**Endpoint:**  
POST /suppliers

**Expected Result:**  
The API should create a new supplier and return the supplier information with a generated supplier ID.

**Test Result:**  
Pass

**Observed Response:**

```json
{
    "contactName": "Maria Lopez",
    "email": "maria@abcelectronics.com",
    "name": "ABC Electronics",
    "phone": "555-123-4567",
    "supplierId": 1
}
```

**Notes:**

The supplier was successfully created and assigned supplier ID 1.


## Test 2 — Get All Suppliers
### GET Suppliers — Get all suppliers

**Endpoint:**  
GET /suppliers

**Expected Result:**
The API should return a list containing all suppliers stored in the database.

**Test Result:**  
Pass

**Observed Response:**

```json
[
    {
        "contactName": "Maria Lopez",
        "email": "maria@abcelectronics.com",
        "name": "ABC Electronics",
        "phone": "555-123-4567",
        "supplierId": 1
    }
]
```

**Notes:**

The API successfully retrieved the supplier that was previously created.

## GET Supplier — Get supplier by ID

**Endpoint:**  
GET /suppliers/1

**Expected Result:**  
The API should return the supplier with supplier ID 1.

**Test Result:**  
Pass

**Observed Response:**

```json
{
    "contactName": "Maria Lopez",
    "email": "maria@abcelectronics.com",
    "name": "ABC Electronics",
    "phone": "555-123-4567",
    "supplierId": 1
}
```
**Notes:**

The API successfully retrieved the supplier using its supplier ID.

## PUT Supplier — Update supplier

**Endpoint:**  
PUT /suppliers/1

**Expected Result:**  
The API should update the supplier's information while keeping the existing supplier ID unchanged.

**Test Result:**  
Pass

**Observed Response:**

```json
{
    "contactName": "Carlos Martinez",
    "email": "carlos@techsource.com",
    "name": "TechSource Distributors",
    "phone": "555-111-2222",
    "supplierId": 1
}
```

**Notes:**

All four supplier fields were successfully updated:

- Name: ABC Electronics → TechSource Distributors
- Contact name: Maria Lopez → Carlos Martinez
- Email: maria@abcelectronics.com → carlos@techsource.com
- Phone: 555-123-4567 → 555-111-2222

The supplier ID remained unchanged at 1.

## DELETE Supplier — Delete supplier

**Endpoint:**  
DELETE /suppliers/1

**Expected Result:**  
The API should delete the supplier with supplier ID 1.

**Test Result:**  
Pass

**Observed Response:**

```text
200 OK ,with a Response Body: Empty
```

**Notes:**

The supplier was successfully deleted. A follow-up GET request to /suppliers/1 returned 500 Internal Server Error, confirming that the supplier could no longer be found.

## GET Supplier — Non-existent Supplier

**Endpoint:**
GET /suppliers/1

**Expected Result:**
The API should return 404 Not Found when the requested supplier does not exist.

**Test Result:**
Pass

**Observed Response:**
```text
404 Not Found

Response Body:  Supplier not found

```

**Notes:**

Supplier ID 1 had been deleted. The API correctly returned 404 Not Found instead of a 500 Internal Server Error.


## POST ProductSupplier — Create Product-Supplier Relationship

**Endpoint:**
POST /product-suppliers

**Request:**
Product ID 2, Supplier ID 2, Purchase Price $47.00

**Expected Result:**
The API should successfully create the Product-Supplier relationship and return the composite ID containing both Product ID and Supplier ID.

**Test Result:**
Pass

**Observed Response:**
```json
id": {
        "product": 2,
        "supplier": 2
    }
```
**Notes:**

The @EmbeddedId and @MapsId mapping successfully created the relationship. The original Hibernate 500 error no longer occurs.

## POST ProductSupplier — Missing Product

**Endpoint:**  
POST /product-suppliers

**Request Body:**
```json
{
  "product": {
    "productId": 999
  },
  "supplier": {
    "supplierId": 2
  },
  "purchasePrice": 45.00
}
```
**Expected Result:**
The API should return a 404 error because Product 999 does not exist.

**Test Result:**
Pass

**Observed Response:**
404 Not Found
```text
Product not found
```
**Notes:**

The API correctly detected that the specified product does not exist and returned the appropriate Product not found message.


## POST ProductSupplier — Missing Supplier

**Endpoint:**
POST /product-suppliers

**Request Body:**
```json
{
    "product": {
        "productId": 2
    },
    "supplier": {
        "supplierId": 999
    },
    "purchasePrice": 45.00
}
```

**Expected Result:**
The API should return a 404 error because Supplier 999 does not exist.

**Test Result:**
Pass

**Observed Response:**
404 Not Found
```text
Supplier not found
```

**Notes:**

The API correctly detected that the specified supplier does not exist and returned the appropriate Supplier not found message.

## GET ProductSupplier — Retrieve All Product-Supplier Relationships

**Endpoint:**
GET /product-suppliers

**Expected Result:**
The API should return all product-supplier relationships, including
the composite ID, product information, supplier information, and
supplier-specific purchase price.

**Test Result:**
Pass

**Observed Response:**
```json
[
    {
        "id": {
            "product": 2,
            "supplier": 2
        },
        "product": {
            "description": "Mechanical keyboard",
            "lowStockThreshold": 3,
            "name": "Keyboard",
            "productId": 2,
            "quantityInStock": 10,
            "sellingPrice": 79.99
        },
        "purchasePrice": 47.00,
        "supplier": {
            "contactName": "Carlos Martinez",
            "email": "carlos@techsource.com",
            "name": "TechSource Distributors",
            "phone": "555-111-2222",
            "supplierId": 2
        }
    }
]
```
**Notes:**

Product #2 is associated with Supplier #2 with a purchase price of 47.00. The response returned the complete Product and Supplier information for the relationship.


## GET ProductSupplier — Retrieve One Product-Supplier Relationship

**Endpoint:**
GET /product-suppliers/2/2

**Expected Result:**
The API should return the Product-Supplier relationship for Product 2 and Supplier 2, including the composite ID, product information, supplier information, and purchase price.

**Test Result:**
Pass

**Observed Response:**
```json
{
  "id": {
    "product": 2,
    "supplier": 2
  },
  "product": {
    "description": "Mechanical keyboard",
    "lowStockThreshold": 3,
    "name": "Keyboard",
    "productId": 2,
    "quantityInStock": 10,
    "sellingPrice": 79.99
  },
  "purchasePrice": 45.00,
  "supplier": {
    "contactName": "Carlos Martinez",
    "email": "carlos@techsource.com",
    "name": "TechSource Distributors",
    "phone": "555-111-2222",
    "supplierId": 2
  }
}
```
**Notes:**

The API successfully retrieved the Product 2 and Supplier 2 relationship using the composite ID. The response included the associated product, supplier, and supplier-specific purchase price.

## GET ProductSupplier — Relationship Not Found

**Endpoint:**
GET /product-suppliers/999/999

**Expected Result:**
The API should return a 404 error because the Product-Supplier relationship does not exist.

**Test Result:**
Pass

**Observed Response:**
404 Not Found
```text
ProductSupplier not found
```

**Notes:**

The API correctly detected that the requested Product-Supplier relationship does not exist and returned the appropriate ProductSupplier not found message.

## PUT ProductSupplier — Update Supplier-Specific Purchase Price

**Endpoint:**
PUT /product-suppliers/2/2

**Request Body:**
```json
{ 
  "purchasePrice": 45.00
}
```

**Expected Result:**
The API should update the purchase price for the Product #2 and Supplier #2 relationship while keeping the existing product-supplier association unchanged.

**Test Result:**
Pass

**Observed Response:**
```json
{
  "id": {
    "product": 2,
    "supplier": 2
  },
  "product": {
    "description": "Mechanical keyboard",
    "lowStockThreshold": 3,
    "name": "Keyboard",
    "productId": 2,
    "quantityInStock": 10,
    "sellingPrice": 79.99
  },
  "purchasePrice": 45.00,
  "supplier": {
    "contactName": "Carlos Martinez",
    "email": "carlos@techsource.com",
    "name": "TechSource Distributors",
    "phone": "555-111-2222",
    "supplierId": 2
  }
}
```
**Notes:**

The purchase price was successfully updated from 47.00 to 45.00. The Product #2 and Supplier #2 relationship remained intact.


## DELETE ProductSupplier — Remove Product-Supplier Relationship

**Endpoint:**
DELETE /product-suppliers/2/2

**Expected Result:**
The API should remove the relationship between Product #2 and Supplier #2 without deleting either the product or supplier.

**Test Result:**
Pass

**Observed Response:**
200 OK
```
 The response body was empty.
```

**Notes:**

The ProductSupplier relationship was successfully removed from the database. The row containing Product #2, Supplier #2, and purchase price 45.00 was no longer present.

Additional Verification:
A GET /product-suppliers request returned:
```json
[]
```

This confirmed that the ProductSupplier relationship was successfully deleted.


## POST Purchase

**Endpoint:**
POST /purchases

**Request Body:**
```json
{
    "supplier": {
        "supplierId": 2
    },
    "purchaseDate": "2026-09-18T13:00:00",
    "purchaseItems": [
        {
            "product": {
                "productId": 2
            },
            "quantity": 10,
            "purchasePrice": 45.00
        }
    ]
}
```

**Expected Result:**
The API should create a new purchase and associate it with an existing supplier and product. The purchase item should store the purchase quantity and historical purchase price.


**Test Result:**
Pass

**Observed Response:**
```json
{
    "purchaseDate": "2026-09-18T13:00:00",
    "purchaseId": 3,
    "purchaseItems": [
        {
            "product": {
                "description": "Mechanical keyboard",
                "lowStockThreshold": 3,
                "name": "Keyboard",
                "productId": 2,
                "quantityInStock": 10,
                "sellingPrice": 79.99
            },
            "purchaseItemId": 3,
            "purchasePrice": 45.00,
            "quantity": 10
        }
    ],
    "supplier": {
        "contactName": "Carlos Martinez",
        "email": "carlos@techsource.com",
        "name": "TechSource Distributors",
        "phone": "555-111-2222",
        "supplierId": 2
    }
}
```

**Notes:**

The API created Purchase ID 3 and Purchase Item ID 3.

The response correctly returned the existing Supplier ID 2 (TechSource Distributors) and Product ID 2 (Keyboard) with their database information.

The purchase item returned:

- Quantity: 10

- Purchase Price: 45.00


## GET All Purchases

**Endpoint:**
GET /purchases

**Expected Result:**
The API should return all purchases stored in the database, including their associated purchase items, products, and suppliers.

**Test Result:**
Pass

**Observed Response:**
```json
[
    {
        "purchaseDate": "2026-09-18T13:00:00",
        "purchaseId": 1,
        "purchaseItems": [
            {
                "product": {
                    "description": "Mechanical keyboard",
                    "lowStockThreshold": 3,
                    "name": "Keyboard",
                    "productId": 2,
                    "quantityInStock": 10,
                    "sellingPrice": 79.99
                },
                "purchaseItemId": 1,
                "purchasePrice": 45.00,
                "quantity": 10
            }
        ],
        "supplier": {
            "contactName": "Carlos Martinez",
            "email": "carlos@techsource.com",
            "name": "TechSource Distributors",
            "phone": "555-111-2222",
            "supplierId": 2
        }
    },
    {
        "purchaseDate": "2026-09-18T13:00:00",
        "purchaseId": 2,
        "purchaseItems": [
            {
                "product": {
                    "description": "Mechanical keyboard",
                    "lowStockThreshold": 3,
                    "name": "Keyboard",
                    "productId": 2,
                    "quantityInStock": 10,
                    "sellingPrice": 79.99
                },
                "purchaseItemId": 2,
                "purchasePrice": 45.00,
                "quantity": 10
            }
        ],
        "supplier": {
            "contactName": "Carlos Martinez",
            "email": "carlos@techsource.com",
            "name": "TechSource Distributors",
            "phone": "555-111-2222",
            "supplierId": 2
        }
    },
    {
        "purchaseDate": "2026-09-18T13:00:00",
        "purchaseId": 3,
        "purchaseItems": [
            {
                "product": {
                    "description": "Mechanical keyboard",
                    "lowStockThreshold": 3,
                    "name": "Keyboard",
                    "productId": 2,
                    "quantityInStock": 10,
                    "sellingPrice": 79.99
                },
                "purchaseItemId": 3,
                "purchasePrice": 45.00,
                "quantity": 10
            }
        ],
        "supplier": {
            "contactName": "Carlos Martinez",
            "email": "carlos@techsource.com",
            "name": "TechSource Distributors",
            "phone": "555-111-2222",
            "supplierId": 2
        }
    }
]
```
**Notes:**

The API returned all three existing purchase records:

- Purchase ID 1

- Purchase ID 2

- Purchase ID 3

Each purchase correctly returned: 

- Purchase date

- Supplier information

- Purchase item information

- Product information

- Quantity

- Historical purchase price


## GET Purchase by ID

**Endpoint:**
GET /purchases/3

**Expected Result:**
The API should return the purchase matching the specified purchase ID.

**Test Result:**
Pass

**Observed Response:**
```json
{
    "purchaseDate": "2026-09-18T13:00:00",
    "purchaseId": 3,
    "purchaseItems": [
        {
            "product": {
                "description": "Mechanical keyboard",
                "lowStockThreshold": 3,
                "name": "Keyboard",
                "productId": 2,
                "quantityInStock": 10,
                "sellingPrice": 79.99
            },
            "purchaseItemId": 3,
            "purchasePrice": 45.00,
            "quantity": 10
        }
    ],
    "supplier": {
        "contactName": "Carlos Martinez",
        "email": "carlos@techsource.com",
        "name": "TechSource Distributors",
        "phone": "555-111-2222",
        "supplierId": 2
    }
}
```

**Notes:**

The API correctly returned Purchase ID 3.

The response included:

- Purchase ID: 3

- Purchase date: 2026-09-18T13:00:00

- Supplier: TechSource Distributors (Supplier ID 2)

- Product: Keyboard (Product ID 2)

- Quantity: 10

- Purchase Price: 45.00





