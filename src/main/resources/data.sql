INSERT INTO retail_transaction(person_id, amount, transaction_date) VALUES
-- Person 1: ₹150 total in June 2025 (>100 → reward 300)
(1, 50.00, '2025-06-05'),
(1,74.00, '2025-06-17'),

-- Person 2: ₹95 total in June 2025 (≤100 → reward 0)
(2, 40.00, '2025-06-02'),
(2, 55.00, '2025-06-25');