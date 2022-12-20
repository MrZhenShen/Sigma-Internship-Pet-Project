INSERT INTO money_balance
(player_id, amount)
SELECT id, 0.0 FROM end_user WHERE username LIKE 'userTest';
