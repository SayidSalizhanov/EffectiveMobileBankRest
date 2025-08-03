-- Вставка тестовых пользователей
INSERT INTO users (login, password_hash) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa'),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa');

-- Назначение ролей пользователям
INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.login = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.login = 'user1' AND r.name = 'ROLE_USER';

INSERT INTO user_roles (user_id, role_id)
SELECT u.user_id, r.role_id
FROM users u, roles r
WHERE u.login = 'user2' AND r.name = 'ROLE_USER';

-- Вставка тестовых карт
INSERT INTO cards (number, expiration_date, status, balance, user_id)
SELECT 
    '1234567890123456',
    '2025-12',
    'ACTIVE',
    1000.00,
    u.user_id
FROM users u WHERE u.login = 'user1';

INSERT INTO cards (number, expiration_date, status, balance, user_id)
SELECT 
    '6543210987654321',
    '2025-12',
    'ACTIVE',
    500.00,
    u.user_id
FROM users u WHERE u.login = 'user1';

INSERT INTO cards (number, expiration_date, status, balance, user_id)
SELECT 
    '1111222233334444',
    '2024-06',
    'EXPIRED',
    0.00,
    u.user_id
FROM users u WHERE u.login = 'user2'; 