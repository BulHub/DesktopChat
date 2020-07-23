CREATE database chat;

create table nicknames
(
  Id       serial primary key,
  Nickname varchar(255),
  Count_messages integer
);

CREATE table client
(
  Id          serial primary key,
  Email       varchar(255),
  Password    varchar(255),
  Nickname_id serial references nicknames (id)
);

create table messages
(
  Id      serial primary key,
  Message varchar(255)
);

