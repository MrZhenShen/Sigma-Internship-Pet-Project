INSERT INTO end_user (username, email, password, role, update_date, create_date) VALUES
    ('userWithoutMoney', 'userWithoutMoney@email.com', '$2a$12$9RcowX5mABLH5Es3PboCB.anLfipjlTs1qrwC152L21fyLYnQ7oU2', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO end_user (username, email, password, role, update_date, create_date) VALUES
    ('userWithMoney', 'userWithMoney@email.com', '$2a$12$9RcowX5mABLH5Es3PboCB.anLfipjlTs1qrwC152L21fyLYnQ7oU2', 'USER', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO money_balance
(player_id, amount)
SELECT id, 0.0 FROM end_user WHERE username LIKE 'userWithoutMoney';

INSERT INTO money_balance
(player_id, amount)
SELECT id, 100.0 FROM end_user WHERE username LIKE 'userWithMoney';
