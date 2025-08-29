CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS roles
(
    role_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name    VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users
(
    user_id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login         VARCHAR(50)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS cards
(
    number          VARCHAR(16) PRIMARY KEY,
    expiration_date VARCHAR(7)     NOT NULL,
    status          VARCHAR(10)    NOT NULL,
    balance         DECIMAL(19, 2) NOT NULL,
    user_id         UUID           NOT NULL,
    CONSTRAINT fk_cards_user_id FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id)
);

INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

CREATE TABLE IF NOT EXISTS block_requests
(
    request_id     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    card_number    VARCHAR(16) NOT NULL,
    user_id        UUID        NOT NULL,
    status         VARCHAR(20) NOT NULL,
    request_date   TIMESTAMP   NOT NULL,
    processed_date TIMESTAMP,
    reason         TEXT,
    CONSTRAINT fk_block_requests_user_id FOREIGN KEY (user_id) REFERENCES users (user_id),
    CONSTRAINT fk_block_requests_card_number FOREIGN KEY (card_number) REFERENCES cards (number)
);