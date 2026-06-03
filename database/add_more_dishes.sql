USE recipe_management_system;

INSERT IGNORE INTO ingredients (name, category, unit, cost_per_unit) VALUES
('Onion', 'Vegetable', 'piece', 7.00),
('Vinegar', 'Condiments', 'tbsp', 0.80),
('Black Pepper', 'Spices', 'tsp', 1.20),
('Bay Leaf', 'Spices', 'piece', 1.00),
('Pork Belly', 'Protein', 'g', 0.36),
('Tamarind Soup Base', 'Condiments', 'tbsp', 3.50),
('Radish', 'Vegetable', 'piece', 18.00),
('String Beans', 'Vegetable', 'g', 0.12),
('Kangkong', 'Vegetable', 'bundle', 25.00),
('Fish Sauce', 'Condiments', 'tbsp', 1.20),
('Beef Chuck', 'Protein', 'g', 0.52),
('Potato', 'Vegetable', 'piece', 14.00),
('Carrot', 'Vegetable', 'piece', 12.00),
('Bell Pepper', 'Vegetable', 'piece', 18.00),
('Pancit Canton Noodles', 'Noodles', 'g', 0.10),
('Cabbage', 'Vegetable', 'g', 0.08),
('Green Beans', 'Vegetable', 'g', 0.14),
('Ginger', 'Spices', 'g', 0.15),
('Papaya', 'Fruit', 'piece', 45.00),
('Ground Pork', 'Protein', 'g', 0.32),
('Spring Roll Wrapper', 'Wrapper', 'piece', 1.50),
('Coconut Milk', 'Condiments', 'cup', 30.00),
('Green Chili', 'Vegetable', 'piece', 2.00),
('Taro Leaves', 'Vegetable', 'g', 0.18),
('Eggplant', 'Vegetable', 'piece', 18.00),
('Banana Ketchup', 'Condiments', 'tbsp', 1.00),
('Spaghetti Noodles', 'Pasta', 'g', 0.09),
('Cheese', 'Dairy', 'g', 0.45),
('All-Purpose Cream', 'Dairy', 'cup', 38.00),
('Graham Crackers', 'Bakery', 'piece', 4.00),
('Mango', 'Fruit', 'piece', 35.00);

INSERT INTO inventory_items (ingredient_id, stock_qty, threshold, expiry_date, location)
SELECT
    i.ingredient_id,
    CASE i.name
        WHEN 'Onion' THEN 20
        WHEN 'Vinegar' THEN 60
        WHEN 'Black Pepper' THEN 30
        WHEN 'Bay Leaf' THEN 20
        WHEN 'Pork Belly' THEN 1200
        WHEN 'Tamarind Soup Base' THEN 20
        WHEN 'Radish' THEN 6
        WHEN 'String Beans' THEN 500
        WHEN 'Kangkong' THEN 5
        WHEN 'Fish Sauce' THEN 60
        WHEN 'Beef Chuck' THEN 900
        WHEN 'Potato' THEN 10
        WHEN 'Carrot' THEN 12
        WHEN 'Bell Pepper' THEN 6
        WHEN 'Pancit Canton Noodles' THEN 800
        WHEN 'Cabbage' THEN 700
        WHEN 'Green Beans' THEN 450
        WHEN 'Ginger' THEN 250
        WHEN 'Papaya' THEN 3
        WHEN 'Ground Pork' THEN 1000
        WHEN 'Spring Roll Wrapper' THEN 40
        WHEN 'Coconut Milk' THEN 8
        WHEN 'Green Chili' THEN 30
        WHEN 'Taro Leaves' THEN 400
        WHEN 'Eggplant' THEN 8
        WHEN 'Banana Ketchup' THEN 50
        WHEN 'Spaghetti Noodles' THEN 900
        WHEN 'Cheese' THEN 500
        WHEN 'All-Purpose Cream' THEN 6
        WHEN 'Graham Crackers' THEN 30
        WHEN 'Mango' THEN 8
    END,
    CASE i.name
        WHEN 'Pork Belly' THEN 350
        WHEN 'Beef Chuck' THEN 300
        WHEN 'Ground Pork' THEN 300
        WHEN 'Chicken Breast' THEN 250
        ELSE 3
    END,
    CASE
        WHEN i.category IN ('Protein', 'Dairy') THEN DATE_ADD(CURDATE(), INTERVAL 10 DAY)
        WHEN i.category IN ('Vegetable', 'Fruit') THEN DATE_ADD(CURDATE(), INTERVAL 7 DAY)
        ELSE DATE_ADD(CURDATE(), INTERVAL 6 MONTH)
    END,
    CASE
        WHEN i.category IN ('Protein') THEN 'Freezer'
        WHEN i.category IN ('Dairy', 'Vegetable', 'Fruit') THEN 'Fridge'
        ELSE 'Pantry'
    END
FROM ingredients i
WHERE i.name IN (
    'Onion', 'Vinegar', 'Black Pepper', 'Bay Leaf', 'Pork Belly', 'Tamarind Soup Base',
    'Radish', 'String Beans', 'Kangkong', 'Fish Sauce', 'Beef Chuck', 'Potato', 'Carrot',
    'Bell Pepper', 'Pancit Canton Noodles', 'Cabbage', 'Green Beans', 'Ginger', 'Papaya',
    'Ground Pork', 'Spring Roll Wrapper', 'Coconut Milk', 'Green Chili', 'Taro Leaves',
    'Eggplant', 'Banana Ketchup', 'Spaghetti Noodles', 'Cheese', 'All-Purpose Cream',
    'Graham Crackers', 'Mango'
)
AND NOT EXISTS (
    SELECT 1 FROM inventory_items inv WHERE inv.ingredient_id = i.ingredient_id
);

INSERT INTO recipes (title, description, cuisine, difficulty, servings, cooking_time, created_by, cooked_count)
SELECT
    seed.title,
    seed.description,
    seed.cuisine,
    seed.difficulty,
    seed.servings,
    seed.cooking_time,
    (SELECT u.user_id FROM users u WHERE u.role = 'ADMIN' ORDER BY u.user_id LIMIT 1),
    seed.cooked_count
FROM (
    SELECT 'Pork Sinigang' AS title, 'Tangy tamarind pork soup with vegetables.' AS description, 'Filipino' AS cuisine, 'Medium' AS difficulty, 5 AS servings, 55 AS cooking_time, 7 AS cooked_count
    UNION ALL SELECT 'Beef Caldereta', 'Rich tomato-based beef stew with potatoes, carrots, and peppers.', 'Filipino', 'Medium', 5, 75, 4
    UNION ALL SELECT 'Pancit Canton', 'Stir-fried noodles with chicken and crisp vegetables.', 'Filipino', 'Easy', 4, 30, 9
    UNION ALL SELECT 'Tinolang Manok', 'Ginger chicken soup with papaya and leafy greens.', 'Filipino', 'Easy', 4, 40, 6
    UNION ALL SELECT 'Lumpiang Shanghai', 'Crispy spring rolls filled with seasoned ground pork.', 'Filipino', 'Medium', 6, 45, 11
    UNION ALL SELECT 'Bicol Express', 'Creamy coconut pork stew with green chili.', 'Filipino', 'Medium', 4, 50, 5
    UNION ALL SELECT 'Laing', 'Taro leaves simmered in coconut milk with chili.', 'Filipino', 'Medium', 4, 55, 3
    UNION ALL SELECT 'Tortang Talong', 'Grilled eggplant omelet served with tomato.', 'Filipino', 'Easy', 2, 25, 8
    UNION ALL SELECT 'Filipino Spaghetti', 'Sweet-style spaghetti with ground pork, banana ketchup, and cheese.', 'Filipino', 'Easy', 5, 35, 10
    UNION ALL SELECT 'Mango Float', 'Layered graham, cream, and mango chilled dessert.', 'Filipino', 'Easy', 6, 20, 12
) seed
WHERE NOT EXISTS (
    SELECT 1 FROM recipes r WHERE r.title = seed.title
);

INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit, is_optional)
SELECT r.recipe_id, i.ingredient_id, seed.quantity, seed.unit, seed.is_optional
FROM (
    SELECT 'Pork Sinigang' AS recipe_title, 'Pork Belly' AS ingredient_name, 600.00 AS quantity, 'g' AS unit, FALSE AS is_optional
    UNION ALL SELECT 'Pork Sinigang', 'Tomato', 3.00, 'piece', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'Tamarind Soup Base', 3.00, 'tbsp', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'Radish', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'String Beans', 150.00, 'g', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'Kangkong', 1.00, 'bundle', FALSE
    UNION ALL SELECT 'Pork Sinigang', 'Fish Sauce', 2.00, 'tbsp', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Beef Chuck', 650.00, 'g', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Tomato', 4.00, 'piece', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Potato', 2.00, 'piece', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Carrot', 2.00, 'piece', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Bell Pepper', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Garlic', 4.00, 'clove', FALSE
    UNION ALL SELECT 'Beef Caldereta', 'Cheese', 50.00, 'g', TRUE
    UNION ALL SELECT 'Pancit Canton', 'Pancit Canton Noodles', 400.00, 'g', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Chicken Breast', 250.00, 'g', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Cabbage', 200.00, 'g', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Carrot', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Green Beans', 120.00, 'g', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Soy Sauce', 4.00, 'tbsp', FALSE
    UNION ALL SELECT 'Pancit Canton', 'Garlic', 3.00, 'clove', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Chicken Breast', 500.00, 'g', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Ginger', 40.00, 'g', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Garlic', 3.00, 'clove', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Papaya', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Kangkong', 1.00, 'bundle', FALSE
    UNION ALL SELECT 'Tinolang Manok', 'Fish Sauce', 2.00, 'tbsp', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Ground Pork', 500.00, 'g', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Spring Roll Wrapper', 20.00, 'piece', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Carrot', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Garlic', 3.00, 'clove', FALSE
    UNION ALL SELECT 'Lumpiang Shanghai', 'Egg', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Bicol Express', 'Pork Belly', 500.00, 'g', FALSE
    UNION ALL SELECT 'Bicol Express', 'Coconut Milk', 2.00, 'cup', FALSE
    UNION ALL SELECT 'Bicol Express', 'Green Chili', 8.00, 'piece', FALSE
    UNION ALL SELECT 'Bicol Express', 'Garlic', 4.00, 'clove', FALSE
    UNION ALL SELECT 'Bicol Express', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Bicol Express', 'Fish Sauce', 2.00, 'tbsp', FALSE
    UNION ALL SELECT 'Laing', 'Taro Leaves', 250.00, 'g', FALSE
    UNION ALL SELECT 'Laing', 'Coconut Milk', 2.00, 'cup', FALSE
    UNION ALL SELECT 'Laing', 'Green Chili', 5.00, 'piece', FALSE
    UNION ALL SELECT 'Laing', 'Pork Belly', 200.00, 'g', TRUE
    UNION ALL SELECT 'Laing', 'Garlic', 3.00, 'clove', FALSE
    UNION ALL SELECT 'Laing', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Tortang Talong', 'Eggplant', 2.00, 'piece', FALSE
    UNION ALL SELECT 'Tortang Talong', 'Egg', 3.00, 'piece', FALSE
    UNION ALL SELECT 'Tortang Talong', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Tortang Talong', 'Garlic', 2.00, 'clove', TRUE
    UNION ALL SELECT 'Tortang Talong', 'Tomato', 1.00, 'piece', TRUE
    UNION ALL SELECT 'Filipino Spaghetti', 'Spaghetti Noodles', 500.00, 'g', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Ground Pork', 350.00, 'g', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Banana Ketchup', 8.00, 'tbsp', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Tomato', 3.00, 'piece', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Cheese', 80.00, 'g', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Garlic', 3.00, 'clove', FALSE
    UNION ALL SELECT 'Filipino Spaghetti', 'Onion', 1.00, 'piece', FALSE
    UNION ALL SELECT 'Mango Float', 'Graham Crackers', 18.00, 'piece', FALSE
    UNION ALL SELECT 'Mango Float', 'All-Purpose Cream', 2.00, 'cup', FALSE
    UNION ALL SELECT 'Mango Float', 'Mango', 4.00, 'piece', FALSE
    UNION ALL SELECT 'Mango Float', 'Cheese', 40.00, 'g', TRUE
) seed
JOIN recipes r ON r.title = seed.recipe_title
JOIN ingredients i ON i.name = seed.ingredient_name
WHERE NOT EXISTS (
    SELECT 1
    FROM recipe_ingredients ri
    WHERE ri.recipe_id = r.recipe_id AND ri.ingredient_id = i.ingredient_id
);

INSERT INTO cooking_steps (recipe_id, step_number, instruction, time_minutes, step_type)
SELECT r.recipe_id, seed.step_number, seed.instruction, seed.time_minutes, seed.step_type
FROM (
    SELECT 'Pork Sinigang' AS recipe_title, 1 AS step_number, 'Boil pork with onion and tomato until tender.' AS instruction, 35 AS time_minutes, 'Boil' AS step_type
    UNION ALL SELECT 'Pork Sinigang', 2, 'Add tamarind base, radish, and string beans.', 12, 'Simmer'
    UNION ALL SELECT 'Pork Sinigang', 3, 'Finish with kangkong and fish sauce.', 5, 'Finish'
    UNION ALL SELECT 'Beef Caldereta', 1, 'Brown beef with garlic and onion.', 10, 'Saute'
    UNION ALL SELECT 'Beef Caldereta', 2, 'Simmer beef with tomato until tender.', 50, 'Simmer'
    UNION ALL SELECT 'Beef Caldereta', 3, 'Add vegetables and cheese, then cook until thick.', 15, 'Finish'
    UNION ALL SELECT 'Pancit Canton', 1, 'Saute garlic, chicken, and vegetables.', 10, 'Saute'
    UNION ALL SELECT 'Pancit Canton', 2, 'Add noodles and soy sauce.', 12, 'Stir fry'
    UNION ALL SELECT 'Pancit Canton', 3, 'Toss until noodles absorb the sauce.', 8, 'Finish'
    UNION ALL SELECT 'Tinolang Manok', 1, 'Saute ginger, garlic, onion, and chicken.', 10, 'Saute'
    UNION ALL SELECT 'Tinolang Manok', 2, 'Simmer with fish sauce and water until chicken is cooked.', 22, 'Simmer'
    UNION ALL SELECT 'Tinolang Manok', 3, 'Add papaya and greens before serving.', 8, 'Finish'
    UNION ALL SELECT 'Lumpiang Shanghai', 1, 'Mix pork, carrot, onion, garlic, and egg.', 10, 'Prep'
    UNION ALL SELECT 'Lumpiang Shanghai', 2, 'Wrap filling in spring roll wrappers.', 20, 'Wrap'
    UNION ALL SELECT 'Lumpiang Shanghai', 3, 'Fry until golden and crisp.', 15, 'Fry'
    UNION ALL SELECT 'Bicol Express', 1, 'Saute pork with garlic and onion.', 10, 'Saute'
    UNION ALL SELECT 'Bicol Express', 2, 'Simmer with coconut milk until pork is tender.', 30, 'Simmer'
    UNION ALL SELECT 'Bicol Express', 3, 'Add green chili and fish sauce.', 10, 'Finish'
    UNION ALL SELECT 'Laing', 1, 'Simmer coconut milk with garlic and onion.', 12, 'Simmer'
    UNION ALL SELECT 'Laing', 2, 'Add taro leaves and cook without stirring at first.', 30, 'Simmer'
    UNION ALL SELECT 'Laing', 3, 'Add chili and optional pork, then reduce until creamy.', 13, 'Finish'
    UNION ALL SELECT 'Tortang Talong', 1, 'Grill or broil eggplant until soft.', 10, 'Grill'
    UNION ALL SELECT 'Tortang Talong', 2, 'Flatten eggplant and dip in beaten egg.', 5, 'Prep'
    UNION ALL SELECT 'Tortang Talong', 3, 'Pan-fry until the egg is set.', 10, 'Fry'
    UNION ALL SELECT 'Filipino Spaghetti', 1, 'Cook spaghetti noodles until tender.', 10, 'Boil'
    UNION ALL SELECT 'Filipino Spaghetti', 2, 'Saute garlic, onion, ground pork, tomato, and banana ketchup.', 18, 'Sauce'
    UNION ALL SELECT 'Filipino Spaghetti', 3, 'Toss noodles with sauce and top with cheese.', 7, 'Finish'
    UNION ALL SELECT 'Mango Float', 1, 'Layer graham crackers in a dish.', 5, 'Layer'
    UNION ALL SELECT 'Mango Float', 2, 'Spread cream and sliced mango over each layer.', 10, 'Layer'
    UNION ALL SELECT 'Mango Float', 3, 'Chill until set before serving.', 5, 'Chill'
) seed
JOIN recipes r ON r.title = seed.recipe_title
WHERE NOT EXISTS (
    SELECT 1
    FROM cooking_steps cs
    WHERE cs.recipe_id = r.recipe_id AND cs.step_number = seed.step_number
);

INSERT IGNORE INTO tags (name, color) VALUES
('Lunch', '#2E86AB'),
('Party', '#8E44AD'),
('Comfort', '#C47A00'),
('Vegetable', '#2E8B57'),
('Dessert', '#C85A8E'),
('Spicy', '#C0392B');

INSERT IGNORE INTO recipe_tags (recipe_id, tag_id)
SELECT r.recipe_id, t.tag_id
FROM (
    SELECT 'Pork Sinigang' AS recipe_title, 'Dinner' AS tag_name
    UNION ALL SELECT 'Pork Sinigang', 'Comfort'
    UNION ALL SELECT 'Beef Caldereta', 'Dinner'
    UNION ALL SELECT 'Beef Caldereta', 'Comfort'
    UNION ALL SELECT 'Pancit Canton', 'Lunch'
    UNION ALL SELECT 'Pancit Canton', 'Party'
    UNION ALL SELECT 'Tinolang Manok', 'Dinner'
    UNION ALL SELECT 'Tinolang Manok', 'Comfort'
    UNION ALL SELECT 'Lumpiang Shanghai', 'Party'
    UNION ALL SELECT 'Bicol Express', 'Dinner'
    UNION ALL SELECT 'Bicol Express', 'Spicy'
    UNION ALL SELECT 'Laing', 'Vegetable'
    UNION ALL SELECT 'Laing', 'Spicy'
    UNION ALL SELECT 'Tortang Talong', 'Breakfast'
    UNION ALL SELECT 'Tortang Talong', 'Quick'
    UNION ALL SELECT 'Tortang Talong', 'Vegetable'
    UNION ALL SELECT 'Filipino Spaghetti', 'Party'
    UNION ALL SELECT 'Mango Float', 'Dessert'
    UNION ALL SELECT 'Mango Float', 'Party'
) seed
JOIN recipes r ON r.title = seed.recipe_title
JOIN tags t ON t.name = seed.tag_name;

INSERT IGNORE INTO recipe_ratings (recipe_id, user_id, score, comment, created_date)
SELECT r.recipe_id, u.user_id, seed.score, seed.comment, CURDATE()
FROM (
    SELECT 'Pork Sinigang' AS recipe_title, 'maria@example.com' AS email, 5 AS score, 'Sour and comforting.' AS comment
    UNION ALL SELECT 'Beef Caldereta', 'juan@example.com', 4, 'Rich and filling.'
    UNION ALL SELECT 'Pancit Canton', 'maria@example.com', 5, 'Good for sharing.'
    UNION ALL SELECT 'Tinolang Manok', 'juan@example.com', 4, 'Light and warm.'
    UNION ALL SELECT 'Lumpiang Shanghai', 'maria@example.com', 5, 'Party favorite.'
    UNION ALL SELECT 'Bicol Express', 'juan@example.com', 5, 'Creamy with a nice kick.'
    UNION ALL SELECT 'Laing', 'maria@example.com', 4, 'Great with rice.'
    UNION ALL SELECT 'Tortang Talong', 'juan@example.com', 4, 'Simple breakfast dish.'
    UNION ALL SELECT 'Filipino Spaghetti', 'maria@example.com', 5, 'Sweet and nostalgic.'
    UNION ALL SELECT 'Mango Float', 'juan@example.com', 5, 'Easy dessert win.'
) seed
JOIN recipes r ON r.title = seed.recipe_title
JOIN users u ON u.email = seed.email;

INSERT IGNORE INTO favorites (user_id, recipe_id)
SELECT u.user_id, r.recipe_id
FROM (
    SELECT 'maria@example.com' AS email, 'Pork Sinigang' AS recipe_title
    UNION ALL SELECT 'maria@example.com', 'Mango Float'
    UNION ALL SELECT 'juan@example.com', 'Pancit Canton'
    UNION ALL SELECT 'juan@example.com', 'Bicol Express'
) seed
JOIN users u ON u.email = seed.email
JOIN recipes r ON r.title = seed.recipe_title;
