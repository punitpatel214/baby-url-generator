CREATE KEYSPACE IF NOT EXISTS key_generator WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

CREATE KEYSPACE IF NOT EXISTS url_shortener WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': 1
};

USE key_generator;

CREATE TABLE IF NOT EXISTS keys (
  key text PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS usedKeys (
  key text PRIMARY KEY
);

insert into keys(key) VALUES('abcd1');
insert into keys(key) VALUES('abcd2');
insert into keys(key) VALUES('abcd3');
insert into keys(key) VALUES('abcd4');
insert into keys(key) VALUES('abcd5');
insert into keys(key) VALUES('abcd6');
insert into keys(key) VALUES('abcd8');
insert into keys(key) VALUES('abcd8');
insert into keys(key) VALUES('abcd11');
insert into keys(key) VALUES('abcd12');
insert into keys(key) VALUES('abcd13');
insert into keys(key) VALUES('abcd14');
insert into keys(key) VALUES('abcd15');
insert into keys(key) VALUES('abcd16');
insert into keys(key) VALUES('abcd18');
insert into keys(key) VALUES('abcd18');

USE url_shortener;

CREATE TABLE IF NOT EXISTS ShortenURL (
  key text PRIMARY KEY,
  url text,
  create_time TIMESTAMP,
  expiry_time TIMESTAMP
);

