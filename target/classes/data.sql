CREATE DATABASE pancakeshop;
\c pancakeshop;

-- Table for Orders
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    building VARCHAR(50) NOT NULL,
    room INT NOT NULL
);

-- Table for Ingredients
CREATE TABLE ingredients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity DOUBLE PRECISION NOT NULL,
    unit VARCHAR(50) NOT NULL
);

-- Table for Pancakes (Recipes)
CREATE TABLE pancakes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL
);

-- This may go and becomes ingredients_list_items 
-- Table for Recipe Ingredients (Many-to-Many relationship between pancakes and ingredients)
--CREATE TABLE pancake_ingredients (
--    pancake_id UUID REFERENCES pancakes(id) ON DELETE CASCADE,
--    ingredient_id INT REFERENCES ingredients(id) ON DELETE CASCADE,
--    PRIMARY KEY (pancake_id, ingredient_id)
--);

-- Table for Orders and Pancakes (Many-to-Many relationship)
CREATE TABLE order_pancakes (
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    pancake_id UUID REFERENCES pancakes(id) ON DELETE CASCADE,
    PRIMARY KEY (order_id, pancake_id)
);

-- Table for Order Statuses
CREATE TABLE order_status (
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    status VARCHAR(20) CHECK (status IN ('prepared', 'completed')),
    PRIMARY KEY (order_id, status)
);

CREATE TABLE Buildings (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Rooms (
    id SERIAL PRIMARY KEY,
    building_id INT NOT NULL,
    room_number VARCHAR(50) NOT NULL,
    FOREIGN KEY (building_id) REFERENCES Building(id) ON DELETE CASCADE
);

CREATE TABLE Recipes (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


--CREATE TABLE Recipe_Ingredient (
--    recipe_id INT REFERENCES Recipe(id) ON DELETE CASCADE,
--    ingredient_id INT REFERENCES Ingredients(id) ON DELETE CASCADE,
--    quantity DECIMAL(10,2),
--    unit VARCHAR(50),
--    PRIMARY KEY (recipe_id, ingredient_id)
--);

CREATE TABLE ingredients_lists (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL, 
	description VARCHAR(255) NOT NULL
);


-- EMPTY
--CREATE TABLE ingredients_list_items (
--    ingredients_list_id INT REFERENCES ingredients_lists(id) ON DELETE CASCADE,
--    ingredient_id INT REFERENCES ingredients(id) ON DELETE CASCADE,
--    quantity DECIMAL(10,2),
--    unit VARCHAR(50),
--    PRIMARY KEY (ingredients_list_id, ingredient_id)
--);




