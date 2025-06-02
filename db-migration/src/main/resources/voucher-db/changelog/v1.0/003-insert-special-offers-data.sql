--changeset author:insert-special-offers-data
INSERT INTO special_offers (name, discount_percentage) VALUES
('Welcome Discount', 10),
('Summer Sale', 25),
('Black Friday Special', 50),
('Loyalty Reward', 15);
--rollback DELETE FROM special_offers WHERE name IN ('Welcome Discount', 'Summer Sale', 'Black Friday Special', 'Loyalty Reward'); 