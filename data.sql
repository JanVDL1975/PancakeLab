--CREATE DATABASE pancakeshop;
-- \c pancakeshop;

-- Table for Orders
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    building INT NOT NULL,
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
    description TEXT NOT NULL
);

-- Table for Recipe Ingredients (Many-to-Many relationship between pancakes and ingredients)
CREATE TABLE pancake_ingredients (
    pancake_id UUID REFERENCES pancakes(id) ON DELETE CASCADE,
    ingredient_id INT REFERENCES ingredients(id) ON DELETE CASCADE,
    PRIMARY KEY (pancake_id, ingredient_id)
);

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
