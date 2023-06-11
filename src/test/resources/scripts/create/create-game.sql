INSERT INTO game
(id, title, description, is_one_user_action, winning, cost, create_date)
VALUES
(1, 'Lot', 'Lorem ipsum', false, 0.5, 50, current_timestamp),
(2, 'Spin', 'Spin is nice', true, 0.2, 10, current_timestamp),
(3, 'Lot2', 'Lot and -1', false, 0.5, 49, current_timestamp);
