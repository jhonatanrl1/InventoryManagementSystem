-- V1: Create the initial Inventory Management System schema.

-- 1. Products
CREATE TABLE product (
    product_id BIGINT NOT NULL AUTO_INCREMENT,
    description VARCHAR(255) DEFAULT NULL,
    low_stock_threshold INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity_in_stock INT NOT NULL,
    selling_price DECIMAL(10,2) DEFAULT NULL,
    PRIMARY KEY (product_id),
    CONSTRAINT chk_product_low_stock_threshold
        CHECK (low_stock_threshold >= 0),
    CONSTRAINT chk_product_quantity_in_stock
        CHECK (quantity_in_stock >= 0)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;

-- 2. Suppliers
CREATE TABLE supplier (
    supplier_id BIGINT NOT NULL AUTO_INCREMENT,
    contact_name VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    name VARCHAR(255) DEFAULT NULL,
    phone VARCHAR(255) DEFAULT NULL,
    normalized_phone VARCHAR(255) DEFAULT NULL,
    PRIMARY KEY (supplier_id),
    KEY idx_supplier_normalized_phone (normalized_phone)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;


  -- 3. Product-Supplier relationship
  CREATE TABLE product_supplier (
      product_id BIGINT NOT NULL,
      supplier_id BIGINT NOT NULL,
      purchase_price DECIMAL(38,2) DEFAULT NULL,
      PRIMARY KEY (product_id, supplier_id),
      CONSTRAINT fk_product_supplier_product
          FOREIGN KEY (product_id) REFERENCES product (product_id),
      CONSTRAINT fk_product_supplier_supplier
          FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id)
  ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;

  -- 4. Purchases
  CREATE TABLE purchase (
      purchase_id BIGINT NOT NULL AUTO_INCREMENT,
      purchase_date DATETIME(6) DEFAULT NULL,
      supplier_id BIGINT DEFAULT NULL,
      PRIMARY KEY (purchase_id),
      CONSTRAINT fk_purchase_supplier
          FOREIGN KEY (supplier_id) REFERENCES supplier (supplier_id)
  ) ENGINE=InnoDB
    DEFAULT CHARSET=utf8mb4
    COLLATE=utf8mb4_0900_ai_ci;


    -- 5. Purchase items
    CREATE TABLE purchase_item (
        purchase_item_id BIGINT NOT NULL AUTO_INCREMENT,
        purchase_price DECIMAL(38,2) DEFAULT NULL,
        quantity INT NOT NULL,
        product_id BIGINT DEFAULT NULL,
        purchase_id BIGINT DEFAULT NULL,
        PRIMARY KEY (purchase_item_id),
        CONSTRAINT fk_purchase_item_product
            FOREIGN KEY (product_id) REFERENCES product (product_id),
        CONSTRAINT fk_purchase_item_purchase
            FOREIGN KEY (purchase_id) REFERENCES purchase (purchase_id)
    ) ENGINE=InnoDB
      DEFAULT CHARSET=utf8mb4
      COLLATE=utf8mb4_0900_ai_ci;

