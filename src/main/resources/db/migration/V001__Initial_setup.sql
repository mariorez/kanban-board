CREATE TABLE bucket (
    id SERIAL PRIMARY KEY,
    uuid UUID UNIQUE NOT NULL,
    position INT UNIQUE NOT NULL,
    name VARCHAR UNIQUE NOT NULL
);