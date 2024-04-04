use timecoffeeproject;
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    price INT,
    created_at DATE,
    name varchar(150)
);

CREATE TABLE tables(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(10)
);

CREATE TABLE areas(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(10)
);


CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_date DATE,
    total_money INT
);

CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    quantity int
);

CREATE TABLE users (
    account_name varchar(100),
    password varchar(100)
)


-- add table category
create table categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name varchar(100)
)

-- add table id as foreign key in orders
alter table orders add table_id int;
alter table orders add foreign key (table_id) references tables(id);

-- add foreign key orders to order_details
alter table order_details add table_id int;

alter table order_details
drop column table_id;

alter table order_details add order_id int;


alter table order_details add foreign key (order_id) references orders(id);

-- add product id as a foreign key in order_details table
alter table order_details add product_id int;
alter table order_details add foreign key (product_id) references products(id); 


-- add foreign key for table
alter table tables add area_id int; 
alter table tables add foreign key tables(area_id) references areas(id); 

alter table tables add status varchar(50);
alter table tables MODIFY COLUMN status ENUM("occupied","empty");


-- set table status default 0
alter table tables drop column status;
alter table tables add status tinyint default 0;

-- add updated column to product
alter table products add updated_at date;

-- add foreign key into product 
alter table products add category_id int;
alter table products add foreign key(category_id) references categories(id);

-- add status to tables 
alter table tables add is_paid boolean;

alter table tables drop column is_paid

alter table orders add is_paid boolean;

-- add color to category




