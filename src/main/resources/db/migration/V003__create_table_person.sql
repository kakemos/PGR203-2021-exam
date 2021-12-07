create table person
(
    id serial primary key,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    email varchar(50) not null
);

Insert into person (first_name, last_name, email)
VALUES ('Siri', 'Wert', 'Sirir@hotmail.no');
Insert into person (first_name, last_name, email)
VALUES ('Samuel', 'Porter', 'SPorter@hotmail.no');
Insert into person (first_name, last_name, email)
VALUES ('Sander', 'Monsen', 'SanderM@hotmail.no');
Insert into person (first_name, last_name, email)
VALUES ('Kassandra', 'Simonsen', 'SimonsenK@hotmail.no');
Insert into person (first_name, last_name, email)
VALUES ('Monika', 'Thomson', 'MT@hotmail.no');

