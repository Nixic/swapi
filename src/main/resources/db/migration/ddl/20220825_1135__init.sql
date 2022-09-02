CREATE SCHEMA IF NOT EXISTS "swapi";

CREATE TABLE IF NOT EXISTS swapi.users
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    username   varchar unique,
    firstName  varchar,
    lastName   varchar,
    email      varchar,
    password   varchar,
    phone      varchar,
    userStatus int

);

COMMENT ON TABLE swapi.users IS 'Users table';
