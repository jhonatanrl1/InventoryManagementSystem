markdown

# Inventory Management System

The Inventory Management System will allow a business to manage its
products and keep track of inventory levels.


## Requirements
* **Product Management**
    * Add products
    * Edit products
    * Delete products
    * View products
* **Search & Analytics**
    * Search for products
    * Filter products
    * Track how many units are in stock
    * Identify products with low stock
    * Calculate the total value of inventory
* **Supplier Management**
    * Add suppliers
    * Edit suppliers
    * Delete suppliers
    * View suppliers
    * Associate suppliers with products
* **Purchase Management**
    * Record purchases
    * View purchase history



## Tech Stack
* **Language:** Java (OpenJDK 26.0.2)
* **Framework:** Spring Boot
* **Database:** MySQL
* **Architecture:** REST API
* **Version Control:** Git / GitHub
* **IDE:** IntelliJ IDEA



## Design

### System Architecture

The application will use a layered architecture:

```text
Client
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL Database
```
- **Controller:** Receives REST API requests and returns responses.
- **Service:** Handles the application's business logic and rules.
- **Repository:** Handles communication with the MySQL database.
- **MySQL Database:** Stores products, suppliers, and inventory data.

## Database Design

The database will use a relational structure to manage products, suppliers, supplier pricing, and purchase history.


### Product


Stores information about the products being sold.
```text
Product
-----------------------------
product_id              PK
name
description
selling_price
quantity_in_stock
low_stock_threshold
```

### Supplier

Stores information about suppliers.

```text
Supplier
-----------------------------
supplier_id             PK
name
email
phone
```
### Product_Supplier

Connects products and suppliers using a many-to-many relationship. It also stores the current purchase price offered by each supplier.

```text
Product_Supplier
-----------------------------
product_id              PK, FK
supplier_id             PK, FK
purchase_price
```

product_id and supplier_id together form the composite primary key.



### Purchase

Represents an actual purchase made from a supplier.

```text
Purchase
-----------------------------
purchase_id             PK
supplier_id             FK
purchase_date
```
### Purchase_Item

Stores the individual products included in each purchase.

```text
Purchase_Item
-----------------------------
purchase_item_id        PK
purchase_id             FK
product_id              FK
quantity
purchase_price
```

The purchase price is stored here because the actual price paid for a product may differ from the supplier's current price.


## Database Relationships

```text

Supplier 1 ──────── * Product_Supplier * ──────── 1 Product

Supplier 1 ──────── * Purchase

Purchase 1 ──────── * Purchase_Item

Product 1 ────────── * Purchase_Item
```

The Product_Supplier table allows one product to have multiple suppliers and one supplier to provide multiple products.

The Purchase and Purchase_Item tables allow the system to maintain a history of actual purchases, including the quantity purchased and the price actually paid.




