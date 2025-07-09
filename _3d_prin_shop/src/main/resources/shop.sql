USE shop;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS blacklist;
drop table if exists product_categories;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;

-- Таблица пользователей
create table users (
    id bigint PRIMARY KEY auto_increment,
    username VARCHAR(64) NOT NULL UNIQUE,
    password VARCHAR(128) NOT NULL,
    email VARCHAR(128) NOT NULL UNIQUE,
    first_name VARCHAR(64),
    last_name VARCHAR(64),
    role VARCHAR(16) DEFAULT 'ROLE_USER',
    is_blacklisted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Таблица товаров
CREATE TABLE products (
    id bigint PRIMARY KEY auto_increment,
    name VARCHAR(128) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(255),
    stock INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    category_id BiGINT
);

-- Таблица корзин
CREATE TABLE carts (
    id bigint PRIMARY KEY auto_increment,
    user_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Таблица товаров в корзине
CREATE TABLE cart_items (
    id bigint PRIMARY KEY auto_increment,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE(cart_id, product_id)
);

-- Таблица заказов
CREATE TABLE orders (
    id bigint PRIMARY KEY auto_increment,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    total_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(32) DEFAULT 'NEW',
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Таблица товаров в заказе
CREATE TABLE order_items (
    id bigint PRIMARY KEY auto_increment,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    price DECIMAL(10,2) NOT NULL, -- Цена на момент покупки
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Таблица категорий
CREATE TABLE categories (
    id bigint PRIMARY KEY auto_increment,
    name VARCHAR(128) NOT NULL UNIQUE
);