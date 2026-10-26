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


## Product Search — Name and Description

**Test 1: Search by product name**

- **Request:** `GET /products?search=keyboard`
- **Expected:** Returns products whose names contain `keyboard`.

**Test 2: Case-insensitive name search**

- **Request:** `GET /products?search=KEYBOARD`
- **Expected:** Returns the same matching products regardless of capitalization.

**Test 3: Search by description**

- **Request:** `GET /products?search=mechanical`
- **Expected:** Returns products whose descriptions contain `mechanical`, even if their names do not contain that term.

**Test 4: Case-insensitive description search**

- **Request:** `GET /products?search=MECHANICAL`
- **Expected:** Returns the same matching products regardless of capitalization.

**Test 5: No matching products**

- **Request:** `GET /products?search=zzzzzz`
- **Expected:** HTTP `200 OK` with an empty array (`[]`).

**Test 6: Blank search**

- **Request:** `GET /products?search=`
- **Expected:** HTTP `200 OK` with all products.

**Test 7: Product with a blank description**

- **Request:** `GET /products?search=Test%20Monitor`
- **Expected:** Returns product ID `4`, `Test Monitor`, even though its description is empty.

**Result:** All seven manual Postman tests passed.


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



# Frontend Testing — Product Pagination and CRUD Operations

## 1. Product Pagination — Navigate Between Pages

**Frontend File:** `src/App.jsx`  
**Backend Endpoint:** `GET /products?page={page}&size={size}`

### Test 1.1 — Display the First Page

**SETUP →** Start the Spring Boot backend and React frontend. Ensure that at least one product exists in the database.

**ACTION →** Open the product inventory page and inspect the first page.

**VERIFICATION →**
- The product list loads successfully.
- The frontend displays the products returned by the backend.
- The page indicator displays the current page and total number of pages.
- The Previous button is disabled on the first page.
- The Next button is disabled when there is only one page.

**Expected Result:** The first page displays the available products and correct pagination controls.

**Test Result:** Pass

### Test 1.2 — Navigate to the Next Page

**SETUP →** Temporarily configure the frontend page size to 3 products per page. Ensure that at least four products exist.

**ACTION →** Click the Next button.

**VERIFICATION →**
- The frontend requests the next page from the backend.
- The product list displays the products belonging to the next page.
- The page indicator updates.
- The Previous button becomes enabled.

**Expected Result:** The user can navigate forward without manually refreshing the browser.

**Test Result:** Pass

### Test 1.3 — Navigate Between Multiple Pages

**SETUP →** Keep the temporary page size set to 3 and ensure that enough products exist to produce three pages.

**ACTION →** Navigate from the first page to the second page and then to the third page. Navigate backward using Previous.

**VERIFICATION →**
- Each page displays the correct group of products.
- The page indicator updates correctly.
- Previous and Next navigate to the expected pages.
- Previous is disabled on the first page.
- Next is disabled on the last page.

**Expected Result:** The user can navigate forward and backward through the available pages without errors.

**Test Result:** Pass

### Test 1.4 — Handle a Page with No Results

**SETUP →** Use the backend endpoint `GET /products?page=1&size=10` when the database contains eight products.

**ACTION →** Request the second page.

**VERIFICATION →**
- The response contains an empty `content` array.
- `totalElements` remains 8.
- `totalPages` is 1.

**Expected Result:** The backend returns an empty page because all eight products fit on the first page of ten. The frontend prevents navigation to a nonexistent next page.

**Test Result:** Pass

### Test 1.5 — Pagination with Search and Low-Stock Filters

**SETUP →** Ensure that products exist whose names match a search term and that some products meet the low-stock condition.

**ACTION →** Search for `keyboard`, then test the low-stock filter.

**VERIFICATION →**
- Search returns matching products regardless of capitalization.
- The low-stock filter returns products meeting the backend's low-stock criteria.
- The frontend displays the returned products using the paginated response's `content` array.

**Expected Result:** Search and low-stock filtering continue to work with the paginated API response.

**Test Result:** Pass

---

## 2. Create Product — Automatic List Refresh

**Frontend File:** `src/App.jsx`  
**Frontend Function:** `handleAddProduct()`  
**Backend Endpoint:** `POST /products`

### Test 2.1 — Add a Product

**SETUP →** Start the backend and frontend. Open the Add Product form and enter valid product information.

**ACTION →** Submit the form to create a new product.

**VERIFICATION →**
- The backend accepts the request and creates the product.
- The form fields are cleared after successful creation.
- The frontend resets the current page to the first page and clears active search and low-stock filters.
- The `refreshTrigger` state changes, causing `useEffect` to fetch the updated product list.
- The new product appears without manually refreshing the browser.
- The browser console contains no errors.

**Expected Result:** The product is created successfully, and the frontend reloads the paginated list automatically.

**Test Result:** Pass

### Test 2.2 — Preserve the Product List Array

**SETUP →** Open the browser console and keep the product inventory page visible.

**ACTION →** Add a product and observe the list after the request finishes.

**VERIFICATION →**
- The frontend reads products from the paginated response's `data.content` property.
- The product list continues to render correctly.
- No `products.map is not a function` error appears.

**Expected Result:** The frontend handles the paginated response without replacing the product array with the entire response object.

**Test Result:** Pass

---

## 3. Update Product — Automatic List Refresh

**Frontend File:** `src/App.jsx`  
**Frontend Function:** `handleUpdateProduct()`  
**Backend Endpoint:** `PUT /products/{id}`

### Test 3.1 — Edit an Existing Product

**SETUP →** Select an existing product and open its Edit form.

**ACTION →** Change a product field, such as the description, to `Updated during pagination testing`. Save the changes.

**VERIFICATION →**
- The backend updates the selected product.
- The edit form closes after a successful update.
- The edit fields are cleared.
- The `refreshTrigger` state changes, causing `useEffect` to fetch the updated paginated list.
- The updated information appears without manually refreshing the browser.
- The browser console contains no errors.

**Expected Result:** The product is updated, and the frontend displays the latest information automatically.

**Test Result:** Pass

### Test 3.2 — Update a Product with an Empty Description

**SETUP →** Open an existing product for editing.

**ACTION →** Clear the description field and save the changes.

**VERIFICATION →**
- The backend accepts the update.
- The description remains empty.
- The frontend refreshes the product list.
- No browser console errors occur.

**Expected Result:** An empty description can be saved successfully under the current validation rules.

**Test Result:** Pass

---

## 4. Delete Product — Automatic List Refresh

**Frontend File:** `src/App.jsx`  
**Frontend Function:** `handleDeleteProduct(productId)`  
**Backend Endpoint:** `DELETE /products/{id}`

### Test 4.1 — Confirm the Correct Product

**SETUP →** Select an existing test product.

**ACTION →** Click Delete to open the confirmation modal.

**VERIFICATION →**
- The modal appears.
- The modal displays the correct product name.

**Expected Result:** The user can identify which product will be deleted before confirming.

**Test Result:** Pass

### Test 4.2 — Delete the Product

**SETUP →** Keep the confirmation modal open for the selected test product.

**ACTION →** Click Delete in the modal to confirm deletion.

**VERIFICATION →**
- The backend deletes the selected product.
- The confirmation modal closes.
- The `refreshTrigger` state changes, causing `useEffect` to fetch the updated paginated list.
- The deleted product disappears without manually refreshing the browser.
- The browser console contains no errors.
- The product no longer exists in MySQL.

**Expected Result:** The product is deleted from the database and removed from the frontend list automatically.

**Test Result:** Pass

**Note:** A page size of 3 was used temporarily to verify multi-page navigation. The intended frontend page size is 10 products per page.


## Product Frontend Pagination Tests

### Test 1: Create a Product While Viewing a Later Page

**Setup:** The product list has multiple pages, and the user is viewing a page other than Page 1.

**Action:** Create a product using valid information.

**Expected Result:** The product is created successfully, the form fields clear, the current page is preserved, and the product list refreshes with a valid page label.

**Test Result:** Pass

### Test 2: Search Products While Viewing a Later Page

**Setup:** Multiple product pages exist, and the user is viewing a page other than Page 1.

**Action:** Search for an existing product by name.

**Expected Result:** The frontend resets to Page 1 and displays matching products instead of incorrectly showing "No products found."

**Test Result:** Pass

### Test 3: Clear Product Search

**Setup:** A product search is active.

**Action:** Clear the search input.

**Expected Result:** The complete product list returns, starting at Page 1, with valid pagination information.

**Test Result:** Pass

### Test 4: Delete the Last Product on the Last Page

**Setup:** The user is viewing the last page, which contains only one product.

**Action:** Delete that product and confirm the deletion.

**Expected Result:** The frontend automatically navigates to the previous valid page, displays the remaining products, and shows the correct page number and total page count.

**Test Result:** Pass


### Test 5: Search Products With Low-Stock Mode Disabled

**Setup:** Multiple product pages exist, and Show Low Stock is disabled.

**Action:** Search for an existing product by name or description.

**Expected Result:** The frontend displays products matching the search term and maintains valid pagination.

**Test Result:** Pass

### Test 6: Search Products With Low-Stock Mode Enabled

**Setup:** Show Low Stock is enabled, and at least one low-stock product matches the search term.

**Action:** Search for a product by name or description.

**Expected Result:** The frontend displays only products that match the search term and meet the low-stock condition.

**Test Result:** Pass

### Test 7: Clear Search While Low-Stock Mode Remains Enabled

**Setup:** Show Low Stock is enabled, and a search term is active.

**Action:** Clear the search input without disabling Show Low Stock.

**Expected Result:** The frontend displays all low-stock products, with valid pagination.

**Test Result:** Pass

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


### Test 2 — Prevent supplier creation with missing supplier name

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: [leave blank]
Contact Name: Frontend Validation Contact
Email: validation@example.com
Phone: 555-444-5555
```

**Test Action:**
Leave the Supplier Name field blank, enter the remaining supplier information, and click **Add Supplier**.

**Expected Result:**
The browser should prevent the form from being submitted because the Supplier Name field is required. No supplier should be created through the Spring Boot API or stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* A required-field validation message was displayed for Supplier Name.
* No new supplier was created.
* DBeaver confirmed that no new supplier record was added to the MySQL `supplier` table.

**Notes:**

This test confirms that the React supplier creation form prevents submission when the required Supplier Name field is missing.

### Test 3 — Prevent supplier creation with missing contact name

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Frontend Validation Supplier
Contact Name: [leave blank]
Email: validation2@example.com
Phone: 555-444-5556
```

**Test Action:**
Enter the supplier information while leaving the Contact Name field blank, then click **Add Supplier**.

**Expected Result:**
The browser should prevent the form from being submitted because the Contact Name field is required. No supplier should be created through the Spring Boot API or stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* A required-field validation message was displayed for Contact Name.
* No supplier was created.
* DBeaver confirmed that no new supplier record was added to the MySQL `supplier` table.

**Notes:**

This test confirms that the React supplier creation form prevents submission when the required Contact Name field is missing.

### Test 4 — Prevent supplier creation with missing email

**Component:**
React supplier creation form

**Test Data:**

```text id="q4k7ny"
Supplier Name: Frontend Validation Supplier
Contact Name: Frontend Validation Contact
Email: [leave blank]
Phone: 555-444-5557
```

**Test Action:**
Enter the supplier information while leaving the Email field blank, then click **Add Supplier**.

**Expected Result:**
The browser should prevent the form from being submitted because the Email field is required. No supplier should be created through the Spring Boot API or stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* A required-field validation message was displayed for Email.
* No supplier was created.
* DBeaver confirmed that no new supplier record was added to the MySQL `supplier` table.

**Notes:**

This test confirms that the React supplier creation form prevents submission when the required Email field is missing.

### Test 5 — Prevent supplier creation with missing phone

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Frontend Validation Supplier
Contact Name: Frontend Validation Contact
Email: validation3@example.com
Phone: [leave blank]
```

**Test Action:**
Enter the supplier information while leaving the Phone field blank, then click **Add Supplier**.

**Expected Result:**
The browser should prevent the form from being submitted because the Phone field is required. No supplier should be created through the Spring Boot API or stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* A required-field validation message was displayed for Phone.
* No supplier was created.
* DBeaver confirmed that no new supplier record was added to the MySQL `supplier` table.

**Notes:**

This test confirms that the React supplier creation form prevents submission when the required Phone field is missing.

### Test 6 — Prevent supplier creation with invalid email format

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Email Validation Supplier
Contact Name: Email Validation Contact
Email: validation.example.com
Phone: 555-555-5555
```

**Test Action:**
Enter the supplier information using an invalid email format that does not contain an `@` symbol, then click **Add Supplier**.

**Expected Result:**
The browser should prevent the form from being submitted because the Email field contains an invalid email format. No supplier should be created through the Spring Boot API or stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser prevented the form from being submitted.
* An email-format validation message was displayed.
* No supplier was created.
* DBeaver confirmed that no new supplier record was added to the MySQL `supplier` table.

**Notes:**

This test confirms that the React supplier creation form uses the HTML email input validation to prevent submission when an invalid email format is entered.


### Test 7 — Create supplier with valid email format

**Component:**
React supplier creation form

**Test Data:**

```text id="4zq8hc"
Supplier Name: Email Validation Supplier
Contact Name: Email Validation Contact
Email: validation@example.com
Phone: 555-555-5555
```

**Test Action:**
Enter the supplier information using a valid email format and click **Add Supplier**.

**Expected Result:**
The browser should allow the form to be submitted. The supplier should be created successfully through the Spring Boot API and stored in MySQL.

**Test Result:**
Pass

**Verification:**

* The browser allowed the form to be submitted.
* The supplier was created successfully.
* DBeaver confirmed that the supplier was stored in the MySQL `supplier` table.
* The stored email matched `validation@example.com`.

**Notes:**

This test confirms that the React supplier creation form accepts a valid email format and successfully sends the supplier information to the Spring Boot API for storage in MySQL.



### Test 8 — Create supplier with valid phone number

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Phone Validation Supplier
Contact Name: Phone Validation Contact
Email: phonevalidation@example.com
Phone: 555-555-5555
```

**Test Action:**
Enter valid supplier information into the React form using a standard phone number and click **Add Supplier**.

**Expected Result:**
The React frontend should accept the valid phone number and submit the supplier information to the Spring Boot API. The API should create the supplier in MySQL without displaying a frontend error.

**Test Result:**
Pass

**Verification:**

* React allowed the supplier form to be submitted.
* The supplier was created successfully.
* No frontend error was displayed.
* DBeaver showed the `Phone Validation Supplier` record.
* The supplier information was stored correctly in MySQL.
* The phone number `555-555-5555` was stored correctly.

**Notes:**

The valid phone number was accepted successfully, confirming that the new validation does not prevent valid phone numbers from being submitted.


### Test 9 — Reject invalid phone input

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Invalid Phone Supplier
Contact Name: Invalid Phone Contact
Email: invalidphone@example.com
Phone: ABCDEFG
```

**Test Action:**
Enter the supplier information into the React form using an invalid phone value containing letters and click **Add Supplier**.

**Expected Result:**
The React frontend should reject the invalid phone input and prevent the supplier from being submitted to the Spring Boot API. The message **`Please enter a valid phone number.`** should appear underneath the Supplier form. The Product list should remain visible and should not be replaced by the Supplier validation message.

**Test Result:**
Pass

**Verification:**

* React prevented the supplier form from being submitted.
* The message **`Please enter a valid phone number.`** appeared underneath the Supplier form.
* The Product list remained visible.
* No new supplier was created in MySQL.
* DBeaver confirmed that no `Invalid Phone Supplier` record was created.

**Notes:**

This test confirms that the React frontend validates supplier phone input before sending the request to the Spring Boot API. The validation rejects phone input that contains fewer than seven numeric digits. The Supplier validation error is displayed separately from the Product error state so that the Product list remains visible when Supplier validation fails.

### Test 10 — Reject phone number with fewer than seven digits

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Short Phone Supplier
Contact Name: Short Phone Contact
Email: shortphone@example.com
Phone: 123456
```

**Test Action:**
Enter the supplier information into the React form using a phone number containing six digits and click **Add Supplier**.

**Expected Result:**
The React frontend should reject the phone number because it contains fewer than seven numeric digits. The message **`Please enter a valid phone number.`** should appear underneath the Supplier form. The Product list should remain visible, and the supplier should not be submitted to the Spring Boot API.

**Test Result:**
Pass

**Verification:**

* React prevented the supplier form from being submitted.
* The message **`Please enter a valid phone number.`** appeared underneath the Supplier form.
* The Product list remained visible.
* No POST request was sent to the Spring Boot API.
* DBeaver confirmed that no `Short Phone Supplier` record was created in MySQL.

**Notes:**

This test confirms that the React phone validation rejects phone numbers containing fewer than seven numeric digits before the request reaches the Spring Boot API.

### Test 11 — Accept phone number with exactly seven digits

**Component:**
React supplier creation form

**Test Data:**

```text
Supplier Name: Boundary Phone Supplier
Contact Name: Boundary Phone Contact
Email: boundaryphone@example.com
Phone: 1234567
```

**Test Action:**
Enter the supplier information into the React form using a phone number containing exactly seven numeric digits and click **Add Supplier**.

**Expected Result:**
The React frontend should accept the phone number because it contains the minimum required seven numeric digits. The supplier should be submitted to the Spring Boot API and created in MySQL.

**Test Result:**
Pass

**Verification:**

* React allowed the supplier form to be submitted.
* The supplier was created successfully.
* DBeaver showed the `Boundary Phone Supplier` record.
* The supplier information was stored correctly in MySQL.
* The phone number `1234567` was stored successfully.

**Notes:**

This test verifies the minimum valid boundary for the React phone validation. Phone numbers containing fewer than seven numeric digits are rejected, while a phone number containing exactly seven numeric digits is accepted.


### Test 12 — Create supplier and automatically refresh the list

**Component:**
React supplier creation form and supplier list

**Test Data:**

```text
Supplier Name: Auto Refresh Supplier
Contact Name: Auto Refresh Contact
Email: autorefresh@example.com
Phone: 555-222-3333
```

**Test Action:**
Open the Suppliers view, enter the supplier information into the React form, and click **Add Supplier**.

**Expected Result:**
The frontend should submit the supplier information to the Spring Boot API. After the supplier is successfully created in MySQL, the form should clear and the supplier list should automatically refresh to display the new supplier without requiring a manual browser refresh or switching views.

**Test Result:**
Pass

**Verification:**

* The supplier was created successfully through the React frontend.
* The Supplier form cleared after successful creation.
* The new `Auto Refresh Supplier` card appeared automatically.
* A manual browser refresh was not required.
* Switching views was not required to see the new supplier.
* DBeaver confirmed that the supplier was stored in MySQL with the correct information.

**Notes:**

This test confirms that the React frontend automatically reloads the supplier list after successful creation. It verifies the complete workflow from the supplier form through the Spring Boot API to MySQL and back to the updated React supplier list.



### Test 13 — Open and Cancel Supplier Editing

**SETUP →** Navigate to the Suppliers view and locate an existing supplier.

**ACTION →** Click Edit, verify that the form contains the supplier's current information, and click Cancel.

**VERIFICATION →** The edit form closes, the supplier details reappear, and other supplier cards remain visible.

**Result:** Pass

### Test 14 — Update Supplier Contact Name

**SETUP →** Select `Auto Refresh Supplier` for editing.

**ACTION →** Change the contact name to `Updated Test Contact` and click Update Supplier.

**VERIFICATION →** The updated contact name appears in the supplier card and is persisted in MySQL.

**Result:** Pass

### Test 15 — Update Multiple Supplier Fields

**SETUP →** Open `Auto Refresh Supplier` in edit mode.

**ACTION →** Update the supplier name to `Updated Auto Refresh Supplier`, the email to `updatedsupplier@example.com`, and the phone to `555-444-3333`. Keep the contact name as `Updated Test Contact`, then click Update Supplier.

**VERIFICATION →** All updated values appear in the supplier card and are persisted in MySQL without errors.

**Result:** Pass

### Test 16 — Cancel Unsaved Supplier Changes

**SETUP →** Open `Updated Auto Refresh Supplier` in edit mode.

**ACTION →** Change the supplier name to `Cancel Test Supplier` and the contact name to `Cancel Test Contact`, then click Cancel without submitting the form.

**VERIFICATION →** The original supplier name and contact name remain unchanged in the frontend and MySQL.

**Result:** pass

### Test 17 — Reject an Invalid Phone Number During Editing

**SETUP →** Open `Updated Auto Refresh Supplier` in edit mode.

**ACTION →** Enter `123456` in the Phone field and click Update Supplier.

**VERIFICATION →** The update is rejected, the message `Please enter a valid phone number.` appears, the edit form remains open, and the existing phone number in MySQL remains unchanged.

**Result:** pass

### Test 18 — Reject an Invalid Email During Editing

**SETUP →** Open `Updated Auto Refresh Supplier` in edit mode.

**ACTION →** Enter `invalid-email` in the Email field and click Update Supplier.

**VERIFICATION →** Browser email validation prevents submission, displays a validation message, keeps the edit form open, and leaves the existing email in MySQL unchanged.

**Result:** pass

### Test 19 — Preserve Supplier ID During Editing

**SETUP →** Record the existing ID of `Updated Auto Refresh Supplier`.

**ACTION →** Change the contact name to `Final Test Contact` and click Update Supplier.

**VERIFICATION →** The contact name is updated in the frontend and MySQL, the original supplier ID remains unchanged, and no duplicate supplier is created.

**Result:** Pass


### Test 20 — Delete Supplier with Product Associations

- **Setup:** Supplier #2 (TechSource Distributors) is associated with Product #2 in the `product_supplier` table.
- **Action:** Click Delete on TechSource Distributors and confirm the deletion.
- **Expected Result:** The API rejects the request with HTTP `409 Conflict`. The frontend displays the error message: `Cannot delete supplier because it is associated with products. Remove the product associations first.`
- **Actual Result:** Passed. The error message appeared, and the supplier remained in the database.

### Test 21 — Delete Supplier without Associations or Purchase History

- **Setup:** Create a temporary supplier with no product associations or purchase history.
- **Action:** Click Delete and confirm the deletion.
- **Expected Result:** The API deletes the supplier successfully. The supplier disappears from the frontend list and is removed from the database.
- **Actual Result:** Passed. The temporary supplier disappeared from the frontend and was removed from the database.



## Refactor Frontend Testing

### Supplier Tests
- Products view loads and displays the product list.
- Suppliers view loads and displays the supplier list.
- Add a test supplier and verify it appears in the list.
- Edit a test supplier and verify the changes persist.
- Open the Supplier delete confirmation, then cancel.
- Verify deletion is blocked when a supplier is associated with a product.
- Delete a supplier without associations.

### Product Tests
- Verify the frontend inventory value matches `GET /inventory/value`.
- Verify the displayed product count matches the expected count.
- Add a test product and verify it appears in the list.
- Edit a test product and verify the changes persist.
- Open the Product delete confirmation, then cancel.
- Delete a product.
- Test Product search using uppercase and lowercase terms.

**Result:**  14 frontend functional tests passed.


## Supplier Search — Frontend Tests

**Feature:** Search suppliers by name, contact name, email, and phone number.

**Endpoint:** `GET /suppliers?search={term}`

####Test 1: Search by Supplier Name

**Setup:** Supplier records exist in the database.

**Action:** Enter a supplier name into the Supplier Search input.

**Expected Result:** The frontend displays matching suppliers.

**Test Result:** Pass

### Test 2: Search by Contact Name

**Setup:** Supplier records include contact names.

**Action:** Search using a contact name.

**Expected Result:** The frontend displays suppliers matching the contact name.

**Test Result:** Pass

### Test 3: Search by Email

**Setup:** Supplier records include email addresses.

**Action:** Search using an email address or part of an email address.

**Expected Result:** The frontend displays matching suppliers.

**Test Result:** Pass

### Test 4: Search by Phone Number

**Setup:** Supplier records include phone numbers.

**Action:** Search using a phone number or part of a phone number.

**Expected Result:** The frontend displays matching suppliers.

**Test Result:** Pass

### Test 5: Search With No Matching Results

**Setup:** Supplier records exist in the database.

**Action:** Enter a search term that does not match any supplier.

**Expected Result:** No supplier cards are displayed, and the frontend remains functional.

**Test Result:** Pass

### Test 6: Preserve Search After Adding a Supplier

**Setup:** A supplier search is active.

**Action:** Add a supplier while the search is active.

**Expected Result:** The supplier list refreshes and displays only suppliers matching the current search.

**Test Result:** Pass

### Test 7: Preserve Search After Editing a Supplier

**Setup:** A supplier search is active and a matching supplier is available to edit.

**Action:** Edit the supplier and save the changes.

**Expected Result:** The supplier list refreshes while preserving the active search.

**Test Result:** Pass

### Test 8: Refresh Search After Deleting a Supplier

**Setup:** A supplier search is active and a matching supplier is available to delete.

**Action:** Delete the supplier.

**Expected Result:** The supplier list refreshes, preserving the active search and excluding the deleted supplier.

**Test Result:** Pass

### Test 9: Clear Supplier Search

**Setup:** A supplier search has filtered the supplier list.

**Action:** Clear the Supplier Search input.

**Expected Result:** All remaining suppliers are displayed.

**Test Result:** Pass

### Test 10: Case-Insensitive Supplier Search

**Setup:** A supplier exists with a known name, contact name, or email address.

**Action:** Search using different capitalization from the stored value.

**Expected Result:** The matching supplier is returned regardless of capitalization.

**Test Result:** Pass



## Supplier Pagination Frontend Tests

### Test 1: Display Suppliers With Pagination

**Setup:** Supplier records exist in the database.

**Action:** Open the Suppliers section.

**Expected Result:** The frontend displays the suppliers for the current page and shows the correct page number and total page count.

**Test Result:** Pass

### Test 2: Search Suppliers From a Later Page

**Setup:** Multiple supplier pages exist, and the user is viewing a page other than the first page.

**Action:** Search for `TechSource`.

**Expected Result:** The frontend resets to Page 1 and displays the matching supplier without requiring the user to navigate backward.

**Test Result:** Pass

### Test 3: Clear Supplier Search

**Setup:** A supplier search is active.

**Action:** Clear the search input.

**Expected Result:** The complete supplier list returns, beginning on Page 1, with the correct pagination information.

**Test Result:** Pass

### Test 4: Add Supplier While Viewing a Later Page

**Setup:** The user is viewing Page 2 with a page size of 2.

**Action:** Add a supplier using valid information.

**Expected Result:** The supplier is created, the form fields clear, and the list refreshes using the configured page size. The page label remains valid.

**Test Result:** Pass

### Test 5: Update Supplier While Viewing a Later Page

**Setup:** The user is viewing Page 2 with a page size of 2.

**Action:** Edit a supplier and save the changes.

**Expected Result:** The supplier information updates, the edit form closes, and the list refreshes with the correct page contents and pagination information.

**Test Result:** Pass

### Test 6: Delete Supplier While Viewing a Later Page

**Setup:** The user is viewing Page 2 with a page size of 2.

**Action:** Delete a supplier and confirm the deletion.

**Expected Result:** The confirmation modal closes, the supplier disappears, and the list refreshes with valid pagination information.

**Test Result:** Pass

### Test 7: Delete the Last Supplier on the Last Page

**Setup:** The user is viewing the last page, which contains only one supplier.

**Action:** Delete that supplier and confirm the deletion.

**Expected Result:** The frontend automatically navigates to the previous valid page when the current page no longer exists. The page label and supplier list remain correct.

**Test Result:** Pass

### Test 8: Previous and Next Pagination Controls

**Setup:** Multiple supplier pages exist.

**Action:** Navigate forward and backward through the supplier pages.

**Expected Result:** The correct suppliers appear on each page. The Previous button is disabled on the first page, and the Next button is disabled on the last page.

**Test Result:** Pass

### Test 9: Add, Update, and Delete Without Frontend Errors

**Setup:** The supplier list is displayed.

**Action:** Add, update, and delete suppliers.

**Expected Result:** Each operation refreshes the supplier list without a `suppliers.map is not a function` error or browser console errors.

**Test Result:** Pass



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


## Supplier Search Tests

### GET Suppliers — Search by Supplier Name

**Endpoint:**
- `GET /suppliers?search=TechSource`
- `GET /suppliers?search=TECHSOURCE`

**Expected Result:**
- Status: `200 OK`
- The response includes TechSource Distributors (supplier ID 2).
- Search is case-insensitive.

**Test Result:** Pass

### GET Suppliers — Search by Contact Name

**Endpoint:**
- `GET /suppliers?search=Carlos`

**Expected Result:**
- Status: `200 OK`
- The response includes Carlos Martinez at TechSource Distributors (supplier ID 2).

**Test Result:** Pass

### GET Suppliers — Search by Email

**Endpoint:**
- `GET /suppliers?search=techsource.com`
- `GET /suppliers?search=TECHSOURCE.COM`

**Expected Result:**
- Status: `200 OK`
- The response includes `carlos@techsource.com`.
- Search is case-insensitive.

**Test Result:** Pass

### GET Suppliers — Search by Phone Number

**Endpoint:**
- `GET /suppliers?search=5551112222`
- `GET /suppliers?search=555-111-2222`
- `GET /suppliers?search=2222`
- `GET /suppliers?search=1112222`
- `GET /suppliers?search=555%20111%202222`

**Expected Result:**
- Status: `200 OK`
- The response includes TechSource Distributors (supplier ID 2).
- Phone searches ignore formatting characters such as hyphens and spaces.
- Partial phone-number searches can match the last digits or a middle portion of the number.

**Test Result:** Pass

### GET Suppliers — Search with No Matches

**Endpoint:**
- `GET /suppliers?search=zzzzzz`

**Expected Result:**
- Status: `200 OK`
- The response is an empty JSON array: `[]`.

**Test Result:** Pass

### Automated Tests — Maven

**Command / Procedure:**
- IntelliJ Maven → Lifecycle → `clean`
- IntelliJ Maven → Lifecycle → `test`

**Expected Result:**
- All automated tests pass with zero failures and zero errors.

**Test Result:**
- 62 tests run.
- 0 failures.
- 0 errors.
- 0 skipped.
- Build successful.



## Supplier Pagination Tests

### Test 1 — Retrieve the First Page of Suppliers

**Endpoint:** `GET /suppliers?page=0&size=10`

**Setup:** The database contains 5 suppliers.

**Action:** Request the first page with a page size of 10.

**Expected Result:**
- Status: `200 OK`
- All 5 suppliers are returned.
- `totalElements` is 5.
- `totalPages` is 1.
- `number` is 0.

**Test Result:** Pass

### Test 2 — Request a Page Beyond the Available Results

**Endpoint:** `GET /suppliers?page=1&size=10`

**Action:** Request the second page when only one page of results exists.

**Expected Result:**
- Status: `200 OK`
- `content` is an empty array.
- `totalElements` remains 5.
- `totalPages` remains 1.

**Test Result:** Pass

### Test 3 — Search Suppliers with Pagination

**Endpoint:** `GET /suppliers?search=TechSource&page=0&size=10`

**Action:** Search for suppliers by name.

**Expected Result:**
- Status: `200 OK`
- Only TechSource Distributors is returned.
- `totalElements` is 1.

**Test Result:** Pass

### Test 4 — Search Suppliers by Normalized Phone Number

**Endpoint:** `GET /suppliers?search=5551112222&page=0&size=10`

**Action:** Search using a phone number without formatting characters.

**Expected Result:**
- Status: `200 OK`
- TechSource Distributors is returned with phone number `555-111-2222`.
- The supplier appears only once.

**Test Result:** Pass

### Test 5 — Reject a Negative Page Index

**Endpoint:** `GET /suppliers?page=-1&size=10`

**Expected Result:**
- Status: `400 Bad Request`
- Response message: `Page index cannot be negative`.

**Test Result:** Pass

### Test 6 — Reject a Page Size of Zero

**Endpoint:** `GET /suppliers?page=0&size=0`

**Expected Result:**
- Status: `400 Bad Request`
- Response message: `Page size must be between 1 and 100`.

**Test Result:** Pass

### Test 7 — Reject a Page Size Above the Maximum

**Endpoint:** `GET /suppliers?page=0&size=101`

**Expected Result:**
- Status: `400 Bad Request`
- Response message: `Page size must be between 1 and 100`.

**Test Result:** Pass



## Supplier Pagination and Search

### Test 1: Retrieve the First Page of Suppliers

**Setup →** Spring Boot application running with 5 suppliers in the database.

**Action →** Send `GET /suppliers?page=0&size=2`.

**Verification →** HTTP `200 OK`. The response contains 2 suppliers, `totalElements: 5`, and `totalPages: 3`.

**Result:** Pass

### Test 2: Search by Supplier Name

**Setup →** TechSource Distributors exists in the database.

**Action →** Send `GET /suppliers?search=TechSource&page=0&size=2`.

**Verification →** HTTP `200 OK`. The response contains TechSource Distributors and reports `totalElements: 1` and `totalPages: 1`.

**Result:** Pass

### Test 3: Search by Phone Number

**Setup →** TechSource Distributors has phone number `555-111-2222` and its normalized phone number is stored.

**Action →** Send `GET /suppliers?search=555-111-2222&page=0&size=2`.

**Verification →** HTTP `200 OK`. The response contains the matching supplier.

**Result:** Pass

### Test 4: Search by Contact Name

**Setup →** Carlos Martinez is the contact for TechSource Distributors.

**Action →** Send `GET /suppliers?search=Carlos&page=0&size=2`.

**Verification →** HTTP `200 OK`. The response contains the matching supplier.

**Result:** Pass

### Test 5: Search by Email

**Setup →** TechSource Distributors has email `carlos@techsource.com`.

**Action →** Send `GET /suppliers?search=carlos@techsource.com&page=0&size=2`.

**Verification →** HTTP `200 OK`. The response contains the matching supplier.

**Result:** Pass



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





## Product-Supplier Frontend Testing

### Test 1: Search Products by Name

**SETUP →** Open the Product-Supplier section and locate the product search field.

**ACTION →** Enter `Keyboard` in the product search field.

**VERIFICATION →**
- Matching products are displayed.
- Products that do not match the search term are excluded.

**TEST RESULT:** Pass

### Test 2: Search Products by Description

**SETUP →** Open the Product-Supplier section.

**ACTION →** Enter a search term that appears in a product description.

**VERIFICATION →**
- Products matching the description search are displayed.

**TEST RESULT:** Pass

### Test 3: Search Suppliers by Name

**SETUP →** Open the Product-Supplier section and locate the supplier search field.

**ACTION →** Enter `TechSource` in the supplier search field.

**VERIFICATION →**
- TechSource Distributors appears in the results.
- Suppliers that do not match the search term are excluded.

**TEST RESULT:** Pass

### Test 4: Search Suppliers by Contact Name

**SETUP →** Open the Product-Supplier section.

**ACTION →** Search for a supplier using the contact name `Carlos Martinez`.

**VERIFICATION →**
- TechSource Distributors appears in the results.

**TEST RESULT:** Pass

### Test 5: Search Suppliers by Phone Number

**SETUP →** Open the Product-Supplier section.

**ACTION →** Search for a supplier using its phone number.

**VERIFICATION →**
- The matching supplier appears in the results.

**TEST RESULT:** Pass

### Test 6: Search Suppliers by Email

**SETUP →** Open the Product-Supplier section.

**ACTION →** Search for a supplier using its email address.

**VERIFICATION →**
- The matching supplier appears in the results.

**TEST RESULT:** Pass

### Test 7: Clear Product Search

**SETUP →** Enter a product search term that returns matching results.

**ACTION →** Clear the product search field.

**VERIFICATION →**
- The product results reload without the previous search filter.
- Pagination returns to the first page.

**TEST RESULT:** Pass

### Test 8: Clear Supplier Search

**SETUP →** Enter a supplier search term that returns matching results.

**ACTION →** Clear the supplier search field.

**VERIFICATION →**
- The supplier results reload without the previous search filter.
- Pagination returns to the first page.

**TEST RESULT:** Pass

### Test 9: Search with No Matching Products

**SETUP →** Open the Product-Supplier section.

**ACTION →** Enter a product search term that does not match any product.

**VERIFICATION →**
- A no-products-found message is displayed.
- No unrelated products are displayed.

**TEST RESULT:** Pass

### Test 10: Search with No Matching Suppliers

**SETUP →** Open the Product-Supplier section.

**ACTION →** Enter a supplier search term that does not match any supplier.

**VERIFICATION →**
- A no-suppliers-found message is displayed.
- No unrelated suppliers are displayed.

**TEST RESULT:** Pass

### Test 11: Product Selector Pagination — Next Page

**SETUP →** Temporarily set `PAGE_SIZE` to `2` in `ProductSupplierSection.jsx` and ensure enough products exist to fill multiple pages.

**ACTION →** Click the Next button in the product selector.

**VERIFICATION →**
- The next page of products is displayed.
- Each page displays up to two products.

**TEST RESULT:** Pass

### Test 12: Product Selector Pagination — Previous Page

**SETUP →** Navigate to a product selector page after the first page.

**ACTION →** Click the Previous button.

**VERIFICATION →**
- The previous page of products is displayed.

**TEST RESULT:** Pass

### Test 13: Supplier Selector Pagination — Next Page

**SETUP →** Temporarily set `PAGE_SIZE` to `2` in `ProductSupplierSection.jsx` and ensure enough suppliers exist to fill multiple pages.

**ACTION →** Click the Next button in the supplier selector.

**VERIFICATION →**
- The next page of suppliers is displayed.
- Each page displays up to two suppliers.

**TEST RESULT:** Pass

### Test 14: Supplier Selector Pagination — Previous Page

**SETUP →** Navigate to a supplier selector page after the first page.

**ACTION →** Click the Previous button.

**VERIFICATION →**
- The previous page of suppliers is displayed.

**TEST RESULT:** Pass

### Test 15: Select a Product and Supplier

**SETUP →** Open the create-association form.

**ACTION →** Select a product and a supplier from their respective selectors.

**VERIFICATION →**
- Both selections can be made.
- The selected product and supplier are used when submitting the form.

**TEST RESULT:** Pass

### Test 16: Create a Product-Supplier Association

**SETUP →** Select a valid product and supplier that are not already associated.

**ACTION →** Enter a purchase price of `47.00` and submit the form.

**VERIFICATION →**
- The association is created successfully.
- The frontend displays `Product-supplier association created successfully.`
- The new association is present in the MySQL database, verified using DBeaver.

**TEST RESULT:** Pass

### Test 17: Validate Missing Product or Supplier

**SETUP →** Open the create-association form.

**ACTION →** Attempt to submit the form without selecting both a product and a supplier.

**VERIFICATION →**
- The association is not created.
- The frontend displays `Please select both a product and a supplier.`

**TEST RESULT:** Pass

### Test 18: Validate Negative Purchase Price

**SETUP →** Select a valid product and supplier.

**ACTION →** Attempt to enter a negative purchase price.

**VERIFICATION →**
- The negative value is rejected by the form's number-input validation.
- The association is not created.

**TEST RESULT:** Pass

### Test 19: Reject Duplicate Product-Supplier Association

**SETUP →** An association already exists for product ID `3` and supplier ID `3`.

**ACTION →** Submit the same product-supplier pair again using `POST /product-suppliers`.

**VERIFICATION →**
- HTTP status: `409 Conflict`.
- Response message: `This product is already associated with this supplier.`
- DBeaver confirms that only one row exists for the duplicate product-supplier pair.

**TEST RESULT:** Pass

### Test 20: Create a Different Valid Association After a Duplicate Attempt

**SETUP →** An association already exists for product ID `3` and supplier ID `3`.

**ACTION →** Submit a different, valid product-supplier pair.

**VERIFICATION →**
- The new association is created successfully.
- The duplicate-association validation does not prevent valid associations from being created.

**TEST RESULT:** Pass



### Product-Supplier Association List

**Test 21 — Display Existing Associations**

**Setup →** At least one product-supplier association exists in the database.

**Action →** Open the Product-Suppliers section.

**Verification →** The association list displays the product ID, product name, supplier ID, supplier name, and purchase price.

**Test Result:** Pass

**Test 22 — Create Association and Refresh List**

**Setup →** Select a valid product and supplier that are not already associated.

**Action →** Create the association with a purchase price of `47.00`.

**Verification →** The success message appears, and the newly created association appears in the list without refreshing the browser. The purchase price matches the database.

**Test Result:** Pass


**Test 23 — Update Purchase Price**

**SETUP →** An existing product-supplier association has a purchase price of `47.00`.

**ACTION →** Click **Edit Price**, change the price to `45.00`, and click **Save Price**.

**VERIFICATION →** The success message appears, the association table displays `45.00`, and DBeaver confirms that the stored purchase price is `45.00`.

**Test Result:** Pass

**Test 24 — Cancel Editing**

**SETUP →** Open Edit Price for an existing association.

**ACTION →** Change the purchase price, then click **Cancel**.

**VERIFICATION →** The editing controls disappear, the original price remains displayed, and the database remains unchanged.

**Test Result:** Pass

**Test 25 — Reject a Negative Purchase Price**

**SETUP →** Open Edit Price for an existing association.

**ACTION →** Attempt to enter a negative price and save.

**VERIFICATION →** Browser validation prevents submission, and the stored purchase price remains unchanged.

**Test Result:** Pass

**Test 26 — Cancel Association Deletion**

**SETUP →** An existing product-supplier association is displayed in the association table.

**ACTION →** Click **Delete** to open the custom confirmation modal, then click **Cancel**.

**VERIFICATION →** The modal closes, the association remains in the table, and the association record remains unchanged in the database.

**Test Result:** Pass

**Test 27 — Confirm Association Deletion**

**SETUP →** An existing product-supplier association is displayed in the association table.

**ACTION →** Click **Delete**, verify the modal displays the correct product and supplier names, and confirm deletion by clicking **Delete** in the modal.

**VERIFICATION →** The modal closes, the success message appears, the association disappears from the table, and the association is deleted through `DELETE /product-suppliers/{productId}/{supplierId}`. The associated product and supplier themselves remain intact.

**Test Result:** Pass

**Test 28 — Empty Association List**

**SETUP →** All product-supplier associations have been deleted.

**ACTION →** Open or refresh the Product-Supplier Association section.

**VERIFICATION →** The message `No product-supplier associations found.` appears, the association table is hidden, and no loading or error message remains.

**Test Result:** Pass



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

✅ deleteSupplierRemovesSupplierWhenNoAssociationsOrPurchaseHistoryExist()
- Verifies that a supplier is deleted when it has no product associations or purchase history.

✅ deleteSupplierThrowsExceptionWhenSupplierHasProductAssociations()
- Verifies that an IllegalStateException is thrown when a supplier is associated with products.
- Verifies that the supplier is not deleted.

✅ deleteSupplierThrowsExceptionWhenSupplierHasPurchaseHistory()
- Verifies that an IllegalStateException is thrown when a supplier has purchase history.
- Verifies that the supplier is not deleted.

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


### DELETE Supplier — Block Deletion When Product Associations Exist

**Test:** deleteSupplierReturnsConflictWhenSupplierHasProductAssociations()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `DELETE /suppliers/2`

**Expected Result:**
- HTTP 409 Conflict
- Response explains that the supplier is associated with products
- Supplier deletion is blocked
- `deleteSupplier(2L)` is called on the SupplierService

**Test Result:**
Pass


### DELETE Supplier — Block Deletion When Purchase History Exists

**Test:** deleteSupplierReturnsConflictWhenSupplierHasPurchaseHistory()

**Test Class:** SupplierControllerTest.java

**Endpoint:** `DELETE /suppliers/2`

**Expected Result:**
- HTTP 409 Conflict
- Response explains that the supplier has purchase history
- Supplier deletion is blocked
- `deleteSupplier(2L)` is called on the SupplierService

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


