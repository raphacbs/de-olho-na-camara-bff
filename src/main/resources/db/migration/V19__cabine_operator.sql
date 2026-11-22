INSERT INTO authentication.users (id, email, full_name, password_hash, active)
SELECT gen_random_uuid(),
       'cabine@deolhonacamara.local',
       'Cabine',
       '$2a$10$9y3q8fDTNXbNxSma3mez4.vPde3bmICshjCIPpfrK/GAT.T6ilKoG',
       true
WHERE NOT EXISTS (
    SELECT 1 FROM authentication.users WHERE email = 'cabine@deolhonacamara.local'
);
