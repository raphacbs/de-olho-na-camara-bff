-- Rollback: Remove unique constraint from politician_vote table
ALTER TABLE camara_deputados.politician_vote
DROP CONSTRAINT IF EXISTS uk_politician_vote_politician_vote_id;
