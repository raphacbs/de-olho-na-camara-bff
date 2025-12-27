-- Rollback: Remove voting_id column and foreign key from politician_vote table
ALTER TABLE camara_deputados.politician_vote
DROP CONSTRAINT IF EXISTS fk_politician_vote_voting_id;

ALTER TABLE camara_deputados.politician_vote
DROP COLUMN IF EXISTS voting_id;
