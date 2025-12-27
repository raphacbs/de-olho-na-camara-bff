-- Add unique constraint to politician_vote table to prevent duplicate votes per politician per voting
ALTER TABLE camara_deputados.politician_vote
ADD CONSTRAINT uk_politician_vote_politician_vote_id UNIQUE (politician_id, vote_id);
