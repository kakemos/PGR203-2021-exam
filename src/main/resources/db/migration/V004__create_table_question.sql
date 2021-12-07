create table question
(
    id serial primary key,
    question_title varchar(50) not null,
    question_description varchar(50)
);

Insert into question (question_title, question_description)
values ('Hvordan er din trivsel?', 'Trivsel står veldig høyt hos oss.');
Insert into question (question_title, question_description)
values ('Hva er ditt karriere-mål?', 'Velg kun ett.');
Insert into question (question_title, question_description)
values ('Har du søkt andre jobber? (Ja/Nei)', 'Om ansatte ønsker å bli på sin arbeidsplass.');
Insert into question (question_title, question_description)
values ('Hvor utfordret blir du på daglig basis?', 'Om oppgaver blir for lette eller avanserte.');
Insert into question (question_title, question_description)
values ('Hvor fornøyd er du med dine oppgaver?', 'Om du er misfornøyd oppgavene, kan du få nye.');
;
