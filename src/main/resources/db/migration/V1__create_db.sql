DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id   UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL CHECK (length(first_name) > 1),
    last_name VARCHAR(50) NOT NULL CHECK (length(last_name) > 1),
    birth_date DATE NOT NULL,
    email VARCHAR(200) NOT NULL,
    phone VARCHAR(13) NULL,
    address VARCHAR(2000) NULL
);