# --- !Ups

create table sum (
  country                       varchar(255) PRIMARY KEY,
  value_sum                     bigint not null
);

# --- !Downs

drop table if exists sum;

