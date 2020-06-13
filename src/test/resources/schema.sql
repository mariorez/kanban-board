CREATE TABLE bucket (
    id SERIAL PRIMARY KEY,
    uuid UUID UNIQUE NOT NULL,
    position INT UNIQUE NOT NULL,
    name VARCHAR UNIQUE NOT NULL
);

INSERT INTO bucket(uuid, position, name) values
('3731c747-ea27-42e5-a52b-1dfbfa9617db', 100, 'EXISTENT');