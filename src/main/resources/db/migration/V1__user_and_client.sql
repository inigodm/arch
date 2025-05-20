CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    role VARCHAR NOT NULL
);

CREATE TABLE clients (
    id UUID PRIMARY KEY,
    goals VARCHAR NOT NULL,
    age INT NOT NULL,
    injuries VARCHAR NOT NULL,
    weight INT NOT NULL,
    equipment_access INT NOT NULL,
    prefered_training_style VARCHAR NOT NULL,
    phonenumber VARCHAR NOT NULL,
    userId UUID NOT NULL,
    coachId UUID
);

INSERT INTO users (id, username, password, email, role)
values
('00000000-0000-0000-0000-000000000001', 'admin', 'admin', 'admin@admin.com', 42);