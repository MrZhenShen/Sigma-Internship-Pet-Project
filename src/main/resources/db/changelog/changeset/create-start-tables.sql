-- liquibase formatted sql
-- changeset yevhen-rozhylo:init-tables

CREATE TABLE IF NOT EXISTS end_user(
    id INT NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50),
    second_name VARCHAR(50),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS money_balance(
    id INT NOT NULL PRIMARY KEY,
    player_id INT NOT NULL REFERENCES end_user (id),
    amount FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS result(
    id INT NOT NULL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    amount FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS round(
    id INT NOT NULL PRIMARY KEY,
    player_bet FLOAT NOT NULL,
    result_id INT NOT NULL REFERENCES result (id)
);

CREATE TABLE IF NOT EXISTS game(
    id INT NOT NULL PRIMARY KEY,
    title VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL,
    is_one_user_action TINYINT NOT NULL,
    winning FLOAT NOT NULL,
    cost FLOAT NOT NULL,
    create_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS game_session(
    id INT NOT NULL PRIMARY KEY,
    game_id INT NOT NULL REFERENCES game (id),
    player_id INT NOT NULL REFERENCES end_user (id),
    round_id INT NOT NULL REFERENCES round (id),
    create_date TIMESTAMP NOT NULL
);
