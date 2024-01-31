-- Create the Ingredient table
CREATE TABLE IF NOT EXISTS Ingredient (
    id varchar(4) not null,
    name varchar(25) not null,
    type TINYINT not null,
    PRIMARY KEY (id)  -- Add a primary key constraint
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
    name VARCHAR(50) ,
    taco_order BIGINT ,
    created_at TIMESTAMP ,
    FOREIGN KEY (taco_order) REFERENCES Taco_Order(id)
);

CREATE TABLE IF NOT EXISTS Users (
    username VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255),
    role VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS Ingredient_Ref (
    ingredient VARCHAR(4) NOT NULL,
    taco BIGINT NOT NULL,
    FOREIGN KEY (ingredient) REFERENCES Ingredient(id) ON DELETE CASCADE,
    PRIMARY KEY (taco, ingredient)
);

-- Add foreign key constraints after all tables have been created
ALTER TABLE Taco
ADD FOREIGN KEY (taco_order) REFERENCES Taco_Order(id);

ALTER TABLE Ingredient_Ref
ADD FOREIGN KEY (ingredient) REFERENCES Ingredient(id);