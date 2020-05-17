CREATE database chat;

create table nicknames
(
  Id       serial primary key,
  Nickname varchar(255)
);

CREATE table client
(
  Id          serial primary key,
  Email       varchar(255),
  Nickname_id serial references nicknames (id),
  Password    varchar(255),
  Position    varchar(255)
);

create table messages
(
  Id      serial primary key,
  Message varchar(255),
  Count   bigint
);

