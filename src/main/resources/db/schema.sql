CREATE TABLE sessions
(
    id       UUID PRIMARY KEY,
    title    VARCHAR(255)   NOT NULL,
    speakers VARCHAR(500)   NOT NULL,
    price    DECIMAL(10, 2) NOT NULL
);
