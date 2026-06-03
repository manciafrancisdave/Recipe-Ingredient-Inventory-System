-- Adds the inventory_transactions and notifications tables to an existing
-- recipe_management_system database WITHOUT dropping or resetting data.
--
-- Run from the MySQL command line:
--   mysql -u root recipe_management_system < database\add_transactions_notifications.sql
-- or open this file in MySQL Workbench / phpMyAdmin and execute it.

USE recipe_management_system;

CREATE TABLE IF NOT EXISTS inventory_transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_id INT NOT NULL,
    user_id INT,
    change_type ENUM('RESTOCK','DEDUCT','COOK') NOT NULL,
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    note VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_txn_ingredient FOREIGN KEY (ingredient_id)
        REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
    CONSTRAINT fk_txn_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    type ENUM('LOW_STOCK','EXPIRY','SYSTEM') NOT NULL DEFAULT 'SYSTEM',
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);
