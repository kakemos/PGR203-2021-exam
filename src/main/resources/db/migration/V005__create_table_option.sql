create table option
(
    id serial primary key,
    option_name varchar(20) not null,
    question_id integer references question (id)
);

insert into option (option_name, question_id) VALUES ('Veldig bra', 1);
insert into option (option_name, question_id) VALUES ('Bra', 1);
insert into option (option_name, question_id) VALUES ('Verken eller', 1);
insert into option (option_name, question_id) VALUES ('Kjedelig', 1);
insert into option (option_name, question_id) VALUES ('Veldig Kjedelig', 1);
insert into option (option_name, question_id) VALUES ('Være motivert', 2);
insert into option (option_name, question_id) VALUES ('Bli leder', 2);
insert into option (option_name, question_id) VALUES ('Være flink', 2);
insert into option (option_name, question_id) VALUES ('Mye fri', 2);
insert into option (option_name, question_id) VALUES ('Bli CEO', 2);
insert into option (option_name, question_id) VALUES ('Høy lønn', 2);
insert into option (option_name, question_id) VALUES ('Jobbe hardt', 2);
insert into option (option_name, question_id) VALUES ('Ja', 3);
insert into option (option_name, question_id) VALUES ('Nei', 3);
insert into option (option_name, question_id) VALUES ('Mye', 4);
insert into option (option_name, question_id) VALUES ('Noe', 4);
insert into option (option_name, question_id) VALUES ('Lite', 4);
insert into option (option_name, question_id) VALUES ('Meget fornøyd', 5);
insert into option (option_name, question_id) VALUES ('Fornøyd', 5);
insert into option (option_name, question_id) VALUES ('Verken eller', 5);
insert into option (option_name, question_id) VALUES ('Veldig ufornøyd', 5);
insert into option (option_name, question_id) VALUES ('Ufornøyd', 5);
