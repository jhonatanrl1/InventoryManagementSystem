# Product Backend Testing

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

### POST Product — Missing quantity in stock

**Endpoint:**
POST /products

**Test Data:**

```json
{
    "name": "Missing Quantity Test",
    "description": "Testing required quantity validation",
    "sellingPrice": 25.00,
    "lowStockThreshold": 3
}
```

**Expected Result:**
The API should reject the request because quantityInStock is required.

**Test Result:**
Pass

**Observed Response:**

```json
{
    "quantityInStock": "must not be null"
}
```

**HTTP Status:**
400 Bad Request

**Notes:**

This test confirms that quantityInStock cannot be omitted from a product request and that Spring Boot validation correctly returns a 400 Bad Request response.

### POST Product — Missing low-stock threshold

**Endpoint:**
POST /products

**Expected Result:**
The API should reject the request because lowStockThreshold is required.

**Test Result:**
Pass

**Request Body:**

```json
{
    "name": "Missing Threshold Test",
    "description": "Testing required threshold validation",
    "sellingPrice": 25.00,
    "quantityInStock": 10
}
```

**Observed Response:**

```json
{
    "lowStockThreshold": "must not be null"
}
```

**HTTP Status:**
400 Bad Request

**Notes:**

This test confirms that lowStockThreshold cannot be omitted from a product request and that Spring Boot validation correctly returns a 400 Bad Request response.


### POST Product — Negative quantity in stock

**Endpoint:**
POST /products

**Expected Result:**
The API should reject the request because quantityInStock cannot be negative.

**Test Result:**
Pass

**Request Body:**

```json
{
    "name": "Negative Quantity Test",
    "description": "Testing negative quantity validation",
    "sellingPrice": 25.00,
    "quantityInStock": -1,
    "lowStockThreshold": 3
}
```

**Observed Response:**

```json
{
    "quantityInStock": "must be greater than or equal to 0"
}
```

**HTTP Status:**
400 Bad Request

**Notes:**

This test confirms that quantityInStock cannot be less than 0 and that the @Min(0) validation rule correctly rejects negative values.

### POST Product — Negative low-stock threshold

**Endpoint:**
POST /products

**Expected Result:**
The API should reject the request because lowStockThreshold cannot be negative.

**Test Result:**
Pass

**Request Body:**

```json
{
    "name": "Negative Threshold Test",
    "description": "Testing negative threshold validation",
    "sellingPrice": 25.00,
    "quantityInStock": 10,
    "lowStockThreshold": -1
}
```

**Observed Response:**

```json
{
    "lowStockThreshold": "must be greater than or equal to 0"
}
```

**HTTP Status:**
400 Bad Request

**Notes:**

This test confirms that lowStockThreshold cannot be less than 0 and that the @Min(0) validation rule correctly rejects negative values.


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

### Test  — Product does not meet low-stock condition

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


### Test  — Multiple products

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

# Frontend Testing

The React frontend was manually tested to verify product and supplier functionality, including form validation, product creation, product updates, and communication with the Spring Boot API. These tests confirm that valid data is successfully sent from the React frontend through the Spring Boot API and stored in MySQL, while invalid input is blocked by frontend validation.

## Product Frontend Testing

### Test 1 — Blank quantity in stock

**Component:**
React product creation form

**Test Action:**
Leave the quantityInStock field blank and attempt to create a product.

**Expected Result:**
The frontend should prevent submission because quantity in stock is required. No product should be created.

**Test Result:**
Pass

**Verification:**

* The form did not submit.
* No product was created.
* DBeaver showed no new product record from this test.

**Notes:**

This test confirms that the React frontend prevents product creation when the required quantity in stock field is blank.

### Test 2 — Blank low-stock threshold

**Component:**
React product creation form

**Test Action:**
Leave the lowStockThreshold field blank and attempt to create a product.

**Expected Result:**
The frontend should prevent submission because the low-stock threshold is required. No product should be created.

**Test Result:**
Pass

**Verification:**

* The form did not submit.
* No product was created.
* DBeaver showed no new product record from this test.

**Notes:**

This test confirms that the React frontend prevents product creation when the required low-stock threshold field is blank.

### Test 3 — Create product with valid data

**Component:**
React product creation form

**Test Data:**

```text
Name: Validation Success Test
Description: Testing valid product creation
Selling Price: 35.00
Quantity in Stock: 10
Low-Stock Threshold: 3
```

**Test Action:**
Enter valid product information into the React form and submit it.

**Expected Result:**
The frontend should submit the product to the Spring Boot API. The API should create the product in MySQL and return the saved product information. The newly created product should automatically appear in the React product list without requiring a manual browser refresh.

**Test Result:**
Pass

**Verification:**

* The product was created successfully through the React frontend.
* The newly created product appeared automatically in the React product list.
* A manual browser refresh was not required.
* The product appeared in DBeaver.
* The product had a quantity in stock of 10 and a low-stock threshold of 3.
* The product did not meet the low-stock condition because 10 is greater than 3.

**Notes:**

This test confirms that valid product data can travel from the React frontend through the Spring Boot API and be stored in MySQL. It also confirms that the React frontend automatically reloads the product list after successful product creation so the newly created product is displayed without requiring the user to refresh the browser.


### Test 4 — Update product price and quantity

**Component:**
React product edit form

**Test Data:**

```text
Product: Keyboard
Product ID: 2
Selling Price: 80.99 → 81.99
Quantity in Stock: 9 → 8
```

**Test Action:**
Click **Edit** for the Keyboard product, change the selling price and quantity in stock, and click **Update Product**.

**Expected Result:**
The frontend should send a PUT request to the Spring Boot API. The API should update the product in MySQL. The updated values should appear in the React product list automatically without requiring a manual browser refresh.

**Test Result:**
Pass

**Verification:**

* The product was successfully updated through the React frontend.
* The React product list automatically displayed the updated values.
* A manual browser refresh was not required.
* DBeaver showed the updated selling price of 81.99.
* DBeaver showed the updated quantity in stock of 8.

**Notes:**

This test confirms that product updates can travel from the React frontend through the Spring Boot API and be stored in MySQL. It also confirms that the React frontend refreshes the product data automatically after a successful update.

### Test 5 — Update product text fields

**Component:**
React product edit form

**Test Data:**

```text
Product: Keyboard
Name: Keyboard → Updated Keyboard
Description: Mechanical keyboard → Updated mechanical keyboard
```

**Test Action:**
Click **Edit** for the product, change the product name and description, and click **Update Product**.

**Expected Result:**
The frontend should submit the updated product information to the Spring Boot API. The API should update the product in MySQL.

**Test Result:**
Pass

**Verification:**

* The product name was successfully updated.
* The product description was successfully updated.
* The updated values appeared in the React product list.
* DBeaver showed the updated product information.

**Notes:**

This test confirms that the React frontend can update the product's text fields through the PUT endpoint.

### Test 6 — Update low-stock threshold

**Component:**
React product edit form

**Test Data:**

```text
Product: Updated Keyboard
Quantity in Stock: 8
Low-Stock Threshold: 3 → 10
```

**Test Action:**
Click **Edit** for the product, change the low-stock threshold from 3 to 10, and click **Update Product**.

**Expected Result:**
The frontend should submit the updated threshold to the Spring Boot API. The product should become low stock because the quantity in stock is 8 and the low-stock threshold is 10.

**Test Result:**
Pass

**Verification:**

* The low-stock threshold was successfully updated.
* The updated value appeared in DBeaver.
* The product became classified as low stock.
* The low-stock functionality recognized that 8 is less than or equal to 10.

**Notes:**

This test confirms that changes made through the React edit form affect the product's low-stock status.

### Test 7 — Prevent negative quantity during product update

**Component:**
React product edit form

**Test Action:**
Click **Edit** for the product and attempt to change Quantity in Stock from 8 to -1.

**Expected Result:**
The browser should prevent submission because the quantity in stock field has a minimum value of 0. No invalid update should be sent to the API.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* The invalid quantity was not sent to the API.
* The product remained unchanged.

**Notes:**

This test confirms that the React frontend prevents negative quantity values during product updates.

### Test 8 — Prevent negative low-stock threshold during product update

**Component:**
React product edit form

**Test Action:**
Click **Edit** for the product and attempt to change Low-Stock Threshold from 10 to -1.

**Expected Result:**
The browser should prevent submission because the low-stock threshold field has a minimum value of 0. No invalid update should be sent to the API.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* The invalid threshold was not sent to the API.
* The product remained unchanged.

**Notes:**

This test confirms that the React frontend prevents negative low-stock threshold values during product updates.

### Test 9 — Cancel product deletion

**Component:**
React product delete confirmation modal

**Test Data:**

```text
Product: test
Description: test description
Selling Price: 12.00
Quantity in Stock: 2
Low-Stock Threshold: 1
```

**Test Action:**
Click **Delete** next to the product. When the confirmation modal appears, verify the product name and click **Cancel**.

**Expected Result:**
The custom confirmation modal should appear and display the correct product name. Clicking **Cancel** should close the modal without deleting the product.

**Test Result:**
Pass

**Verification:**

* The custom confirmation modal appeared successfully.
* The product name was displayed correctly in the modal.
* Clicking **Cancel** closed the modal.
* The product was not deleted.
* The product remained available in the React product list.

**Notes:**

This test confirms that the custom confirmation modal protects against accidental product deletion and that selecting **Cancel** does not send a delete request.

### Test 10 — Confirm product deletion

**Component:**
React product delete confirmation modal

**Test Data:**

```text
Product: test
Description: test description
Selling Price: 12.00
Quantity in Stock: 2
Low-Stock Threshold: 1
```

**Test Action:**
Click **Delete** next to the product. When the confirmation modal appears, verify the product name and click the **Delete** button inside the modal.

**Expected Result:**
The custom confirmation modal should appear and display the correct product name. Clicking **Delete** should send a DELETE request to the Spring Boot API. The product should be removed from the React product list without requiring a manual browser refresh, and the confirmation modal should close after the deletion succeeds.

**Test Result:**
Pass

**Verification:**

* The custom confirmation modal appeared successfully.
* The product name was displayed correctly in the modal.
* Clicking **Delete** successfully deleted the product.
* The product immediately disappeared from the React product list.
* A manual browser refresh was not required.
* The confirmation modal closed after the deletion succeeded.
* DBeaver showed that the product record was deleted from the MySQL `product` table.

**Notes:**

This test confirms the complete product deletion workflow from the React frontend through the Spring Boot API to MySQL. It also confirms that the custom confirmation modal closes after a successful deletion and that the React product list updates automatically.



## Supplier Frontend Testing

### Test 1 — Create supplier with valid data

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Frontend Test Supplier
Contact Name: Frontend Test Contact
Email: frontendtest@example.com
Phone: 555-333-4444
```

**Test Action:**
Enter valid supplier information into the React form and submit it.

**Expected Result:**
The frontend should submit the supplier information to the Spring Boot API. The API should create the supplier in MySQL and return the saved supplier information.

**Test Result:**
Pass

**Verification:**

* The supplier was created successfully through the React frontend.
* The supplier appeared in DBeaver.
* The supplier information matched the values entered into the React form.
* No frontend error was displayed after the CORS issue was resolved.

**Notes:**

This test confirms that valid supplier data can travel from the React frontend through the Spring Boot API and be stored in MySQL.

During initial testing, the request was blocked by CORS because the React frontend runs on `http://localhost:5173`. The issue was resolved by adding `@CrossOrigin(origins = "http://localhost:5173")` to `SupplierController.java`.


# Supplier Backend Testing

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


# ProductSupplier Backend Testing

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


## DELETE ProductSupplier — Invalid Relationship ID

**Endpoint:**
DELETE /product-suppliers/999/999

**Expected Result:**
The API should return 404 Not Found because the Product-Supplier relationship does not exist.

**Test Result:**
Pass

**Observed Response:**
404 Not Found
```text
ProductSupplier not found
```

**Notes:**

The API correctly handled the invalid Product-Supplier relationship using ProductSupplierNotFoundException and returned a 404 Not Found response.

# Purchase Backend Testing

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


## GET Purchase by ID — Invalid ID

**Endpoint:**
GET /purchases/999

**Expected Result:**
The API should return a 404 Not Found response when the requested purchase does not exist.

**Test Result:**
Pass

**Observed Response:**
404 Not Found
```text
Purchase not found
```

**Notes:**

The API correctly handled the invalid purchase ID using PurchaseNotFoundException and returned a 404 Not Found response.

## POST Purchase — Invalid Supplier ID

**Endpoint:**
POST /purchases

**Request Body:**
```json
{
    "supplier": {
        "supplierId": 999
    },
    "purchaseDate": "2026-09-19T13:00:00",
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
The API should return a 404 Not Found response when the specified supplier does not exist.

**Test Result:**
Pass

**Observed Response:**
```text
Supplier not found
```

**Notes:**
The API correctly detected that Supplier ID 999 does not exist and returned a 404 Not Found response using SupplierNotFoundException.

## POST Purchase — Invalid Product ID

**Endpoint:**
POST /purchases

**Request Body:**
```json
{
    "supplier": {
        "supplierId": 2
    },
    "purchaseDate": "2026-09-19T13:00:00",
    "purchaseItems": [
        {
            "product": {
                "productId": 999
            },
            "quantity": 10,
            "purchasePrice": 45.00
        }
    ]
}
```

**Expected Result:**
The API should return a 404 Not Found response when the specified product does not exist.

**Test Result:**
Pass

**Observed Response:**
```text
Supplier not found
```

**Notes:**

The API correctly detected that Product ID 999 does not exist and returned a 404 Not Found response using ProductNotFoundException.

# Automated Tests

## ProductServiceTest

✅ getProductByIdReturnsProduct()
- Verifies an existing product is returned.

✅ getProductByIdThrowsExceptionWhenProductDoesNotExist()
- Verifies ProductNotFoundException is thrown when the product does not exist.

✅ createProductReturnsSavedProduct()
- Verifies that a product is successfully created and returned by the service.

✅ updateProductReturnsUpdatedProduct()
- Verifies that an existing product is updated and the updated product is returned.

✅ getAllProductsReturnsAllProductsWhenNoSearchIsProvided()
- Verifies that all products are returned when no search term is provided.

# Search & Filtering

## ProductServiceTest

✅ getAllProductsReturnsMatchingProductsForSearch()
- Verifies that products are returned when a search term is provided, including case-insensitive searches.

✅ getAllProductsReturnsEmptyListWhenSearchHasNoMatches()
- Verifies that a search with no matching products returns an empty list.

✅ getLowStockProductsReturnsLowStockProducts()
- Verifies that products at or below their low-stock threshold are returned.


## InventoryServiceTest

✅ calculateInventoryValueReturnsCorrectTotal()
- Verifies that inventory value is calculated by multiplying selling price by quantity in stock.

✅ calculateInventoryValueReturnsCorrectTotalForMultipleProducts()
- Verifies that inventory value is correctly calculated across multiple products.

## SupplierServiceTest

✅ getSupplierByIdReturnsSupplier()
- Verifies that an existing supplier can be retrieved by supplier ID.

✅ getSupplierByIdThrowsExceptionWhenSupplierDoesNotExist()
- Verifies SupplierNotFoundException is thrown when the supplier does not exist.

✅ createSupplierReturnsSavedSupplier()
- Verifies that a supplier is successfully created and returned by the service.

✅ updateSupplierReturnsUpdatedSupplier()
- Verifies that an existing supplier is updated and the updated supplier is returned.

✅ getAllSuppliersReturnsAllSuppliers()
- Verifies that all suppliers are returned by the service.

✅ deleteSupplierRemovesSupplier()
- Verifies that an existing supplier is deleted by the service.

## ProductSupplierServiceTest

✅ getProductSupplierReturnsProductSupplier()
- Verifies that an existing ProductSupplier relationship is returned with the correct product, supplier, and purchase price.

✅ getProductSupplierThrowsExceptionWhenNotFound()
- Verifies that ProductSupplierNotFoundException is thrown when the requested ProductSupplier relationship does not exist.

✅ createProductSupplierReturnsSavedProductSupplier()
- Verifies that a ProductSupplier relationship is successfully created when the referenced Product and Supplier exist.
- Verifies the Product ID, Supplier ID, Product name, Supplier name, and purchase price.

✅ createProductSupplierThrowsExceptionWhenProductDoesNotExist()
- Verifies that ProductNotFoundException is thrown when the Product does not exist.

✅ createProductSupplierThrowsExceptionWhenSupplierDoesNotExist()
- Verifies that SupplierNotFoundException is thrown when the Supplier does not exist.

✅ getAllProductSuppliersReturnsAllProductSuppliers()
- Verifies that all ProductSupplier relationships are returned with the correct products, suppliers, and purchase prices.

✅ updateProductSupplierReturnsUpdatedProductSupplier()
- Verifies that an existing ProductSupplier relationship can be updated and the new purchase price is returned.
- Verifies that the Product and Supplier relationship remains unchanged.

✅ updateProductSupplierThrowsExceptionWhenNotFound()
- Verifies that ProductSupplierNotFoundException is thrown when the ProductSupplier relationship does not exist.

✅ deleteProductSupplierRemovesProductSupplier()
- Verifies that an existing ProductSupplier relationship is deleted.

✅ deleteProductSupplierThrowsExceptionWhenNotFound()
- Verifies that ProductSupplierNotFoundException is thrown when attempting to delete a ProductSupplier relationship that does not exist.

## PurchaseServiceTest

✅ createPurchaseReturnsSavedPurchase()
- Verifies that a Purchase is successfully created with the correct supplier and purchase item details.
- Verifies that the product, quantity, and historical purchase price are retained.

✅ createPurchaseThrowsExceptionWhenSupplierDoesNotExist()
- Verifies that creating a Purchase with a non-existent supplier throws SupplierNotFoundException.

✅ createPurchaseThrowsExceptionWhenProductDoesNotExist()
- Verifies that creating a Purchase with a non-existent product throws ProductNotFoundException.

✅ getAllPurchasesReturnsAllPurchases()
- Verifies that all existing Purchases are returned.
- Verifies that the returned list contains the expected Purchase IDs.

✅ getPurchaseReturnsPurchase()
- Verifies that an existing Purchase is returned when searched by its ID.

✅ getPurchaseThrowsExceptionWhenNotFound()
- Verifies that PurchaseNotFoundException is thrown when the requested Purchase does not exist.

## PurchaseItemServiceTest

✅ createPurchaseItemReturnsSavedPurchaseItem()
- Verifies that a PurchaseItem is successfully saved and returned.
- Verifies the PurchaseItem ID, quantity, and purchase price.

✅ getAllPurchaseItemsReturnsAllPurchaseItems()
- Verifies that all existing PurchaseItems are returned.
- Verifies that the returned list contains the expected PurchaseItem IDs.

✅ getPurchaseItemReturnsPurchaseItem()
- Verifies that an existing PurchaseItem is returned when searched by its ID.
- Verifies the PurchaseItem ID, quantity, and purchase price.

✅ getPurchaseItemThrowsExceptionWhenNotFound()
- Verifies that a RuntimeException is thrown when the requested PurchaseItem does not exist.

## Automated Controller Tests

### GET Products — Retrieve All Products

**Test:** getAllProductsReturnsProducts()

**Test Class:** ProductControllerTest.java

**Endpoint:** `GET /products`

**Expected Result:**
- HTTP 200 OK
- Response contains 2 products
- Product #1 ID and name are correct
- Product #2 ID and name are correct

**Test Result:** 
Pass

### GET Product — Retrieve Product by ID

**Test:** getProductByIdReturnsProduct()

**Test Class:** ProductControllerTest.java

**Endpoint:** `GET /products/{id}`

**Expected Result:**
- HTTP 200 OK
- Product ID is 2
- Product name is "Keyboard"
- Selling price is 79.99
- Quantity in stock is 10
- Low-stock threshold is 3

**Test Result:** 
Pass

### POST Products — Create Product

**Test:** createProductReturnsCreatedProduct()

**Test Class:** ProductControllerTest.java

**Endpoint:** `POST /products`

**Expected Result:**
- HTTP 200 OK
- A product is created and returned in the response
- Product ID is 3
- Product name is "Monitor"
- Selling price is 199.99
- Quantity in stock is 5
- Low-stock threshold is 2

**Test Result:**
Pass

### PUT Product — Update Product

**Test:** updateProductReturnsUpdatedProduct()

**Test Class:** ProductControllerTest.java

**Endpoint:** `PUT /products/{id}`

**Expected Result:**
- HTTP 200 OK
- Product ID is 2
- Product name is "Updated Keyboard"
- Selling price is 89.99
- Quantity in stock is 15
- Low-stock threshold is 4

**Test Result:**
Pass

### DELETE Product — Delete Product by ID

**Test:** deleteProductReturnsOk()

**Test Class:** ProductControllerTest.java

**Endpoint:** `DELETE /products/{id}`

**Expected Result:**
- HTTP 200 OK
- Product ID 2 is passed to the service
- deleteProduct(2L) is called

**Test Result:**
Pass


### GET Products — Search by Product Name

**Test:** getAllProductsReturnsMatchingProductsForSearch()

**Test Class:** ProductControllerTest.java

**Endpoint:** `GET /products?search={name}`

**Expected Result:**
- HTTP 200 OK
- Response contains 1 matching product
- Product ID is 2
- Product name is "Keyboard"

**Test Result:**
Pass

### GET Products — Retrieve Low-Stock Products

**Test:** getAllProductsReturnsLowStockProducts()

**Test Class:** ProductControllerTest.java

**Endpoint:** `GET /products?lowStock=true`

**Expected Result:**
- HTTP 200 OK
- Response contains 1 low-stock product
- Product ID is 2
- Product name is "Keyboard"
- Quantity in stock is 2
- Low-stock threshold is 3

**Test Result:**
Pass

### GET Inventory Value — Calculate Total Inventory Value

**Test:** getInventoryValueReturnsValue()

**Test Class:** InventoryControllerTest.java

**Endpoint:** `GET /inventory/value`

**Expected Result:**
- HTTP 200 OK
- Inventory value is 949.85

**Test Result:**
Pass

### GET Suppliers — Retrieve All Suppliers

**Test:** getAllSuppliersReturnsSuppliers()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `GET /suppliers`

**Expected Result:**
- HTTP 200 OK
- Response contains 2 suppliers
- Supplier ID is 1
- Supplier name is "ABC Electronics"
- Supplier ID is 2
- Supplier name is "TechSource Distributors"

**Test Result:**
Pass

### GET Supplier — Retrieve Supplier by ID

**Test:** getSupplierByIdReturnsSupplier()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `GET /suppliers/{id}`

**Expected Result:**
- HTTP 200 OK
- Supplier ID is 2
- Supplier name is "TechSource Distributors"
- Contact name is "Carlos Martinez"
- Email is "carlos@techsource.com"
- Phone is "555-111-2222"

**Test Result:**
Pass

### POST Supplier — Create Supplier

**Test:** createSupplierReturnsCreatedSupplier()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `POST /suppliers`

**Expected Result:**
- HTTP 200 OK
- Supplier ID is 3
- Supplier name is "New Supplier"
- Contact name is "John Smith"
- Email is "john@newsupplier.com"
- Phone is "555-333-4444"

**Test Result:**
Pass

### PUT Supplier — Update Supplier

**Test:** updateSupplierReturnsUpdatedSupplier()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `PUT /suppliers/{id}`

**Expected Result:**
- HTTP 200 OK
- Supplier ID is 2
- Supplier name is "Updated TechSource"
- Contact name is "Carlos Martinez"
- Email is "updated@techsource.com"
- Phone is "555-999-8888"

**Test Result:**
Pass


### DELETE Supplier — Delete Supplier

**Test:** deleteSupplierCallsService()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `DELETE /suppliers/{id}`

**Expected Result:**
- HTTP 200 OK
- deleteSupplier(2L) is called on the SupplierService

**Test Result:**
Pass

### POST ProductSupplier — Create ProductSupplier

**Test:** createProductSupplierReturnsCreatedProductSupplier()

**Test Class:** ProductSupplierControllerTest.java

**Endpoint:** `POST /product-suppliers`

**Expected Result:**
- HTTP 200 OK
- Product ID is 2
- Supplier ID is 2
- Purchase price is 45.00

**Test Result:**
Pass


### GET ProductSupplier — Retrieve All ProductSupplier Relationships

**Test:** getAllProductSuppliersReturnsProductSuppliers()

**Test Class:** ProductSupplierControllerTest.java

**Endpoint:** `GET /product-suppliers`

**Expected Result:**
- HTTP 200 OK
- Response contains 1 product-supplier relationship
- Product ID is 2
- Supplier ID is 2
- Purchase price is 45.00

**Test Result:**
Pass

### GET ProductSupplier — Retrieve ProductSupplier by Product and Supplier

**Test:** getProductSupplierReturnsProductSupplier()

**Test Class:** ProductSupplierControllerTest.java

**Endpoint:** `GET /product-suppliers/{productId}/{supplierId}`

**Expected Result:**
- HTTP 200 OK
- Product ID is 2
- Supplier ID is 2
- Purchase price is 45.00

**Test Result:**
Pass

### PUT ProductSupplier — Update ProductSupplier

**Test:** updateProductSupplierReturnsUpdatedProductSupplier()

**Test Class:** ProductSupplierControllerTest.java

**Endpoint:** `PUT /product-suppliers/{productId}/{supplierId}`

**Expected Result:**
- HTTP 200 OK
- Product ID is 2
- Supplier ID is 2
- Purchase price is updated to 42.00

**Test Result:**
Pass

### DELETE ProductSupplier — Delete ProductSupplier

**Test:** deleteProductSupplierCallsService()

**Test Class:** ProductSupplierControllerTest.java

**Endpoint:** `DELETE /product-suppliers/{productId}/{supplierId}`

**Expected Result:**
- HTTP 200 OK
- deleteProductSupplier() is called on the ProductSupplierService

**Test Result:**
Pass

### POST Purchase — Create Purchase

**Test:** createPurchaseReturnsCreatedPurchase()

**Test Class:** PurchaseControllerTest.java

**Endpoint:** `POST /purchases`

**Expected Result:**
- HTTP 200 OK
- Purchase ID is 1
- Supplier ID is 2
- Product ID is 2
- Quantity is 5
- Purchase price is 45.00

**Test Result:**
Pass

### GET Purchases — Retrieve All Purchases

**Test:** getAllPurchasesReturnsPurchases()

**Test Class:** PurchaseControllerTest.java

**Endpoint:** `GET /purchases`

**Expected Result:**
- HTTP 200 OK
- Response contains 1 purchase
- Purchase ID is 1
- Supplier ID is 2
- Supplier name is "TechSource Distributors"

**Test Result:**
Pass

### GET Purchase — Retrieve Purchase by ID

**Test:** getPurchaseReturnsPurchase()

**Test Class:** PurchaseControllerTest.java

**Endpoint:** `GET /purchases/{id}`

**Expected Result:**
- HTTP 200 OK
- Purchase ID is 1
- Supplier ID is 2
- Supplier name is "TechSource Distributors"

**Test Result:**
Pass


