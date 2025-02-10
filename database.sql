CREATE DATABASE shopping_app;

USE shopping_app;

-- table user
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(200) NOT NULL DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0,
    role_id int,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ALTER TABLE users ADD COLUMN role_id INT;

CREATE TABLE roles(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);
-- ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);

CREATE TABLE token(
    id INT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(255) NOT NULL,
    expiration_date DATETIME,
    revoked tinyint(1) NOT NULL,
    expired tinyint(1) NOT NULL,
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users(id)
)

-- social account (ho tro dang nhap bang facebook hoac google)
-- 1 user
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'ten nha social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email tai khoan',
    name VARCHAR(100) NOT NULL COMMENT 'Ten nguoi dung',
    user_id int,
    FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL DEFAULT '' COMMENT 'Ten danh muc'
);

-- bang product
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    code varchar(255),
    name varchar(100) NOT NULL DEFAULT '',
    price FLOAT NOT NULL CHECK ( price >= 0 ),
    thumbnail varchar(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    category_id INT,
    FOREIGN KEY(category_id) REFERENCES categories(id)
);

CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_product_images_product_id FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);

CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(255) NOT NULL,
    note VARCHAR(255) DEFAULT,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') COMMENT 'trang thai don hang',
    active tinyint(1),
    total_money FLOAT CHECK ( total_money >= 0 ),
    shipping_method VARCHAR(100) NOT NULL,
    shipping_address VARCHAR(100) NOT NULL,
    shipping_date TIMESTAMP,
    tracking_number VARCHAR(100) COMMENT 'max van don',
    payment_method VARCHAR(100) NOT NULL
);

CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products (id),
    price FLOAT CHECK ( price >= 0 ),
    number_of_products INT CHECK ( number_of_products > 0 ),
    total_money FLOAT CHECK ( total_money >= 0 ),
    color VARCHAR(20) DEFAULT ''
);