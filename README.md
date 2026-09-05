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
    * Manage suppliers



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