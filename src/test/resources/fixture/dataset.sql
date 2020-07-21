MERGE INTO bucket(id, external_id, position, name, created_at, updated_at) values
(1, '3731c747-ea27-42e5-a52b-1dfbfa9617db', 200.987, 'SECOND-BUCKET', '2020-05-02 20:26:42.415491', '2020-05-02 20:26:42.415491'),
(2, '6d9db741-ef57-4d5a-ac0f-34f68fb0ab5e', 100.15, 'FIRST-BUCKET', '2020-06-18 20:26:42.415491', '2020-06-18 20:26:42.415491');

MERGE INTO card(id, bucket_id, external_id, position, name, created_at, updated_at) values
(1, 2, 'df5cf5b1-c2c7-4c02-b4d4-341d6772f193', 100.01, 'FIRST-CARD', '2020-05-02 20:26:42.415491', '2020-05-02 20:26:42.415491'),
(2, 1, '021944cd-f516-4432-ba8d-44a312267c7d', 200.01, 'SECOND-CARD', '2020-05-02 20:26:42.415491', '2020-05-02 20:26:42.415491');
