# rest-home-spring-rest
NC homework


create table buyers (
	buyer_id serial primary key,
	last_name varchar(50) not null,
	district varchar(100) not null,
	discount smallint
);

create table shops (
	shop_id serial primary key,
	name varchar(100) not null,
	district varchar(100) not null,
	commission smallint not null
);

create table books (
	book_id serial primary key,
	name varchar(100) not null,
	price integer not null,
	warehouse varchar(100) not null,
	quantity integer not null
);

create table purchases (
	id serial primary key,
	date_of_purchase date,
	seller int references shops(shop_id),
	buyer int references buyers(buyer_id),
	book int references books(book_id),
	quantity integer not null,
	amount integer not null
);


insert into books (name,price,warehouse,quantity) values('Война и мир', 123, 'Дом', 13),
('101 далматинец', 432, 'Улица', 12),('Один дома', 542, 'Дом', 16),('Сила воли', 341, 'Химкинский', 11),('Физика', 567, 'Химкинский', 18);

insert into buyers (last_name,district,discount) values ('Данилов','Нижегородский',15), ('Петров','Владимирский',5),
('Смирнова','Нижегородский',20), ('Василенко','Московский',10), ('Петренко','Нижегородский',7);

insert into shops (name,district,commission) values ('Мир книги','Нижегородский',5), ('У дома','Владимирский',15),
('Дом книги','Нижегородский',10), ('Книги','Московский',10), ('Все знаем','Нижегородский',13);

insert into purchases (date_of_purchase,seller,buyer,book,quantity,amount) VALUES (current_date, 2, 3, 4, 27, 2320), (current_date, 2, 3, 4, 27, 2320), (current_date, 3, 3, 1, 25, 3450), (current_date, 1, 2, 3, 13, 250), (current_date, 4, 2, 2, 10, 3320);
