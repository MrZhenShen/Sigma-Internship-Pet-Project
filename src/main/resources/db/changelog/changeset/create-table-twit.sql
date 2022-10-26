-- liquibase formatted sql
-- changeset yevhen-rozhylo:create-table-twit
CREATE TABLE IF NOT EXISTS twit(
    id INT NOT NULL PRIMARY KEY,
    author varchar(50) NOT NULL,
    twit_text varchar(255) NOT NULL
);
