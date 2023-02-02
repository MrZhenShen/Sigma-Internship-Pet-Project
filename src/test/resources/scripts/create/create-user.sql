INSERT INTO role (id, type) VALUES
(0, 'ROLE_USER'), (1, 'ROLE_ADMIN');

INSERT INTO end_user (username, email, password, role_id, update_date, create_date) VALUES
    ('adminTest', 'adminTest@email.com', '$2a$12$YCssqgHd6r5pe35ABugi1up4h26csHj6IKQqd5ILA9O8djDmNsL3a', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO end_user (username, email, password, role_id, update_date, create_date) VALUES
    ('userTest', 'userTest@email.com', '$2a$12$9RcowX5mABLH5Es3PboCB.anLfipjlTs1qrwC152L21fyLYnQ7oU2', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO end_user (username, email, password, role_id, update_date, create_date) VALUES
    ('userWithoutMoney', 'userWithoutMoney@email.com', '$2a$12$9RcowX5mABLH5Es3PboCB.anLfipjlTs1qrwC152L21fyLYnQ7oU2', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO end_user (username, email, password, role_id, update_date, create_date) VALUES
    ('userWithMoney', 'userWithMoney@email.com', '$2a$12$9RcowX5mABLH5Es3PboCB.anLfipjlTs1qrwC152L21fyLYnQ7oU2', 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO money_balance
(player_id, amount)
SELECT id, 0.0 FROM end_user WHERE username LIKE 'userWithoutMoney';

INSERT INTO money_balance
(player_id, amount)
SELECT id, 100.0 FROM end_user WHERE username LIKE 'userWithMoney';