# --- !Ups

create table value (
  id  SERIAL PRIMARY KEY,
  timestamp                     bigint not null,
  value                         bigint not null,
  country                       varchar(255) not null
);

create index ix_value_timestamp on value (timestamp);
create index ix_value_country on value (country);

# --- !Downs

drop table if exists value;

drop index if exists ix_value_timestamp;
drop index if exists ix_value_country;
