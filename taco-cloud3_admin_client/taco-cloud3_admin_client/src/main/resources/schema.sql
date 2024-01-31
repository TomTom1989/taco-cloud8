-- Create the Users table (updated to match the AppUser entity)
CREATE TABLE IF NOT EXISTS AppUser (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    fullname VARCHAR(255),
    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip VARCHAR(10),
    phone_number VARCHAR(20)
);

-- Create the Ingredient table (now with a foreign key to AppUser)
CREATE TABLE IF NOT EXISTS Ingredient (
    id VARCHAR(4) NOT NULL,
    name VARCHAR(25) NOT NULL,
    type TINYINT NOT NULL,
    appUserId BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (appUserId) REFERENCES AppUser(id)
);

-- Create the Taco_Order table
CREATE TABLE IF NOT EXISTS Taco_Order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    delivery_Name VARCHAR(50),
    delivery_Street VARCHAR(50),
    delivery_City VARCHAR(50),
    delivery_State VARCHAR(255),
    delivery_Zip VARCHAR(10),
    cc_number VARCHAR(16),
    cc_expiration VARCHAR(5),
    cc_cvv VARCHAR(3),
    placed_at TIMESTAMP,
    taco_names VARCHAR(255)   
);

-- Create the Taco table
CREATE TABLE IF NOT EXISTS Taco (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    taco_order BIGINT,
    created_at TIMESTAMP,
    FOREIGN KEY (taco_order) REFERENCES Taco_Order(id)
);

-- Create the Ingredient_Ref table
CREATE TABLE IF NOT EXISTS Ingredient_Ref (
    ingredient VARCHAR(4) NOT NULL,
    taco BIGINT NOT NULL,
    PRIMARY KEY (taco, ingredient),
    FOREIGN KEY (ingredient) REFERENCES Ingredient(id) ON DELETE CASCADE,
    FOREIGN KEY (taco) REFERENCES Taco(id)
);
