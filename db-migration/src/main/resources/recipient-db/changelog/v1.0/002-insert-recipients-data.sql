--changeset author:insert-recipients-data
INSERT INTO recipients (name, email) VALUES
('John Doe', 'john.doe@example.com'),
('Jane Smith', 'jane.smith@example.com'),
('Bob Johnson', 'bob.johnson@example.com');
--rollback DELETE FROM recipients WHERE email IN ('john.doe@example.com', 'jane.smith@example.com', 'bob.johnson@example.com'); 