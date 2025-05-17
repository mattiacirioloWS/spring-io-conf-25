CREATE TABLE sessions
(
    id       UUID PRIMARY KEY,
    title    VARCHAR(255)   NOT NULL,
    speakers VARCHAR(500)   NOT NULL,
    price    DECIMAL(10, 2) NOT NULL
);

CREATE TABLE attendees
(
    id         UUID PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL
);