# --- !Ups

create table value (
  timestamp                     bigint not null,
  value                         bigint,
  country                       varchar(255),
  constraint pk_value primary key (timestamp)
);

create index ix_value_timestamp on value (timestamp);
create index ix_value_country on value (country);

# --- !Downs

drop table if exists value;

drop index if exists ix_value_timestamp;
drop index if exists ix_value_country;
