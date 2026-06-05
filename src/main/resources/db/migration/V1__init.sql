CREATE TABLE salon (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    created_at TIMESTAMP
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    salon_id BIGINT REFERENCES salon(id),
    email VARCHAR(255),
    password_hash VARCHAR(255),
    role VARCHAR(50),
    enabled BOOLEAN,
    created_at TIMESTAMP
);

