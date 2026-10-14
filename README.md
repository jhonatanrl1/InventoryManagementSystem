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
* **Backend Language:** Java (OpenJDK 26.0.2)
* **Backend Framework:** Spring Boot
* **Frontend:** React
* **Frontend Build Tool:** Vite
* **Frontend Language:** JavaScript
* **Database:** MySQL
* **Architecture:** REST API
* **Version Control:** Git / GitHub
* **IDE:** IntelliJ IDEA


## Design

### System Architecture

The application uses a layered architecture with a React frontend communicating with the Spring Boot REST API:

```text
Browser
   ↓
React Frontend
   ↓ HTTP / JSON
Spring Boot REST API
   ↓
Controller
   ↓
Service
   ↓
Repository
   ↓
MySQL Database
```

- **React Frontend:** Provides the browser-based user interface and communicates with the backend through HTTP requests.
- **Controller:** Receives REST API requests and returns responses.
- **Service:** Handles the application's business logic and rules.
- **Repository:** Handles communication with the MySQL database.
- **MySQL Database:**  Stores products, suppliers, inventory data, and purchase history.

### Frontend

The frontend is built with React and Vite and provides the browser-based user interface for the Inventory Management System.

The frontend communicates with the Spring Boot REST API using HTTP requests and JSON responses.


### Database Design

The database uses a relational structure to manage products, suppliers, supplier pricing, and purchase history.



### Database Setup and Migrations

The application uses **MySQL** for data persistence and **Flyway** to manage database schema migrations.

#### Initial Setup

1. Install MySQL and create an empty database named `inventory_management`.
2. Configure the database connection and password using the local environment configuration.
3. Start the Spring Boot application.

Flyway automatically applies versioned SQL migration files from `src/main/resources/db/migration/` when the application starts.

The initial database schema is defined in `V1__create_initial_schema.sql`. Hibernate uses schema validation to verify that the database structure matches the application's entity mappings.

#### Existing Databases

The development database was baselined at Flyway version 1 to register its existing schema without recreating its tables or data. The temporary baseline configuration was removed after this process.

#### Future Schema Changes

For future database schema changes, create a new versioned SQL migration instead of modifying an existing migration that has already been applied.

For example:

```text
src/main/resources/db/migration/
  V1__create_initial_schema.sql
  V2__add_product_category.sql
```

Flyway applies pending migrations in version order when the application starts.



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

Stores information about suppliers, including a normalized phone number used to support phone-number searches regardless of formatting.

```text
Supplier
-----------------------------
supplier_id             PK
name
contact_name
email
phone
normalized_phone        INDEX
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


### Database Relationships

```text
Supplier 1 ──────── * Product_Supplier * ──────── 1 Product

Supplier 1 ──────── * Purchase

Purchase 1 ──────── * Purchase_Item

Product 1 ────────── * Purchase_Item
```

The Product_Supplier table allows one product to have multiple suppliers and one supplier to provide multiple products.

The Purchase and Purchase_Item tables allow the system to maintain a history of actual purchases, including the quantity purchased and the price actually paid.


## API Design

The REST API will provide endpoints for managing products, suppliers, inventory, and purchases.

### Products

```text
GET       /products
GET       /products/{id}
POST      /products
PUT       /products/{id}
DELETE    /products/{id}
```

### Search & Filtering

#### Products
```text
GET       /products?search={term}
GET       /products?lowStock=true
```

#### Suppliers
```text
GET /suppliers?search={term}
```

### Inventory
```text
GET       /inventory/value
```

### Suppliers
```text
GET       /suppliers
GET       /suppliers/{id}
POST      /suppliers
PUT       /suppliers/{id}
DELETE    /suppliers/{id}
```

### Product-Suppliers
```text
POST      /product-suppliers
GET       /product-suppliers
GET       /product-suppliers/{productId}/{supplierId}
PUT       /product-suppliers/{productId}/{supplierId}
DELETE    /product-suppliers/{productId}/{supplierId}
```

### Purchases
```text
POST      /purchases
GET       /purchases
GET       /purchases/{id}
```


