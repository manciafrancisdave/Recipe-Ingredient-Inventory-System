DROP DATABASE IF EXISTS recipe_management_system;
CREATE DATABASE recipe_management_system;
USE recipe_management_system;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE recipes (
    recipe_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    description TEXT,
    cuisine VARCHAR(80),
    difficulty VARCHAR(40),
    servings INT NOT NULL DEFAULT 1 CHECK (servings > 0),
    cooking_time INT NOT NULL DEFAULT 0 CHECK (cooking_time >= 0),
    image_path VARCHAR(255),
    created_by INT,
    cooked_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipes_user FOREIGN KEY (created_by)
        REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE ingredients (
    ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    category VARCHAR(80),
    unit VARCHAR(30) NOT NULL,
    cost_per_unit DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (cost_per_unit >= 0)
);

CREATE TABLE inventory_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    ingredient_id INT NOT NULL UNIQUE,
    stock_qty DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (stock_qty >= 0),
    threshold DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (threshold >= 0),
    expiry_date DATE,
    location VARCHAR(120),
    CONSTRAINT fk_inventory_ingredient FOREIGN KEY (ingredient_id)
        REFERENCES ingredients(ingredient_id) ON DELETE CASCADE
);

CREATE TABLE cooking_steps (
    step_id INT AUTO_INCREMENT PRIMARY KEY,
    recipe_id INT NOT NULL,
    step_number INT NOT NULL,
    instruction TEXT NOT NULL,
    time_minutes INT NOT NULL DEFAULT 0 CHECK (time_minutes >= 0),
    step_type VARCHAR(60),
    CONSTRAINT fk_steps_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_step UNIQUE (recipe_id, step_number)
);

CREATE TABLE recipe_ingredients (
    recipe_ingredient_id INT AUTO_INCREMENT PRIMARY KEY,
    recipe_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    unit VARCHAR(30) NOT NULL,
    is_optional BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_ri_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    CONSTRAINT fk_ri_ingredient FOREIGN KEY (ingredient_id)
        REFERENCES ingredients(ingredient_id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_ingredient UNIQUE (recipe_id, ingredient_id)
);

CREATE TABLE shopping_lists (
    list_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_date DATE NOT NULL,
    status ENUM('OPEN','COMPLETED') NOT NULL DEFAULT 'OPEN',
    CONSTRAINT fk_list_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE shopping_list_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    shopping_list_id INT NOT NULL,
    ingredient_id INT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL CHECK (quantity > 0),
    purchased BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_sli_list FOREIGN KEY (shopping_list_id)
        REFERENCES shopping_lists(list_id) ON DELETE CASCADE,
    CONSTRAINT fk_sli_ingredient FOREIGN KEY (ingredient_id)
        REFERENCES ingredients(ingredient_id) ON DELETE CASCADE
);

CREATE TABLE meal_plans (
    plan_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    CONSTRAINT fk_meal_plan_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE meal_plan_recipes (
    meal_plan_recipe_id INT AUTO_INCREMENT PRIMARY KEY,
    meal_plan_id INT NOT NULL,
    recipe_id INT NOT NULL,
    meal_date DATE NOT NULL,
    meal_type ENUM('BREAKFAST','LUNCH','DINNER','SNACK') NOT NULL,
    CONSTRAINT fk_mpr_plan FOREIGN KEY (meal_plan_id)
        REFERENCES meal_plans(plan_id) ON DELETE CASCADE,
    CONSTRAINT fk_mpr_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE
);

CREATE TABLE tags (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    color VARCHAR(20) NOT NULL DEFAULT '#4F8A8B'
);

CREATE TABLE recipe_tags (
    recipe_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (recipe_id, tag_id),
    CONSTRAINT fk_recipe_tags_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_tags_tag FOREIGN KEY (tag_id)
        REFERENCES tags(tag_id) ON DELETE CASCADE
);

CREATE TABLE recipe_ratings (
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    recipe_id INT NOT NULL,
    user_id INT NOT NULL,
    score INT NOT NULL CHECK (score BETWEEN 1 AND 5),
    comment TEXT,
    created_date DATE NOT NULL,
    CONSTRAINT fk_rating_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uq_user_recipe_rating UNIQUE (recipe_id, user_id)
);

CREATE TABLE favorites (
    user_id INT NOT NULL,
    recipe_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, recipe_id),
    CONSTRAINT fk_fav_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_fav_recipe FOREIGN KEY (recipe_id)
        REFERENCES recipes(recipe_id) ON DELETE CASCADE
);

CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(80) NOT NULL,
    generated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    generated_by INT,
    data JSON,
    CONSTRAINT fk_reports_user FOREIGN KEY (generated_by)
        REFERENCES users(user_id) ON DELETE SET NULL
);

INSERT INTO users (username, email, password, role) VALUES
('admin', 'admin@gmail.com', '240be518fabd2724d263edaf20c708a6cb4414a9ba257c568f7f410a0744d32c', 'ADMIN'),
('maria', 'maria@example.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'USER'),
('juan', 'juan@example.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'USER');

INSERT INTO ingredients (name, category, unit, cost_per_unit) VALUES
('Rice', 'Grains', 'cup', 12.00),
('Chicken Breast', 'Protein', 'g', 0.28),
('Garlic', 'Spices', 'clove', 2.00),
('Soy Sauce', 'Condiments', 'tbsp', 1.50),
('Egg', 'Protein', 'piece', 9.00),
('Tomato', 'Vegetable', 'piece', 8.00);

INSERT INTO inventory_items (ingredient_id, stock_qty, threshold, expiry_date, location) VALUES
(1, 8, 2, DATE_ADD(CURDATE(), INTERVAL 8 MONTH), 'Pantry'),
(2, 500, 250, DATE_ADD(CURDATE(), INTERVAL 3 DAY), 'Freezer'),
(3, 12, 4, DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'Pantry'),
(4, 10, 2, DATE_ADD(CURDATE(), INTERVAL 10 MONTH), 'Pantry'),
(5, 6, 4, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 'Fridge'),
(6, 2, 5, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Fridge');

INSERT INTO recipes (title, description, cuisine, difficulty, servings, cooking_time, created_by, cooked_count) VALUES
('Chicken Adobo', 'Savory Filipino chicken simmered with soy sauce and garlic.', 'Filipino', 'Easy', 4, 35, 1, 5),
('Garlic Fried Rice', 'Simple fried rice with garlic and egg.', 'Filipino', 'Easy', 2, 15, 2, 8);

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, is_optional) VALUES
(1, 2, 400, 'g', FALSE),
(1, 3, 4, 'clove', FALSE),
(1, 4, 5, 'tbsp', FALSE),
(2, 1, 3, 'cup', FALSE),
(2, 3, 3, 'clove', FALSE),
(2, 5, 2, 'piece', TRUE);

INSERT INTO cooking_steps (recipe_id, step_number, instruction, time_minutes, step_type) VALUES
(1, 1, 'Brown chicken with garlic.', 8, 'Saute'),
(1, 2, 'Simmer with soy sauce until tender.', 25, 'Simmer'),
(2, 1, 'Toast garlic, add rice, then fold in egg.', 12, 'Stir fry');

INSERT INTO tags (name, color) VALUES
('Dinner', '#4F8A8B'),
('Breakfast', '#D98E04'),
('Quick', '#2E86AB');

INSERT INTO recipe_tags (recipe_id, tag_id) VALUES
(1, 1),
(2, 2),
(2, 3);

INSERT INTO recipe_ratings (recipe_id, user_id, score, comment, created_date) VALUES
(1, 2, 5, 'Classic and easy.', CURDATE()),
(2, 3, 4, 'Great breakfast.', CURDATE());

INSERT INTO favorites (user_id, recipe_id) VALUES
(2, 1),
(3, 2);

INSERT INTO meal_plans (user_id, start_date, end_date) VALUES
(2, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 6 DAY));

INSERT INTO meal_plan_recipes (meal_plan_id, recipe_id, meal_date, meal_type) VALUES
(1, 1, CURDATE(), 'DINNER'),
(1, 2, DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'BREAKFAST');
