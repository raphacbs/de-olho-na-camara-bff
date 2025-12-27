-- Add voting_id column to politician_vote table as foreign key to voting table
ALTER TABLE camara_deputados.politician_vote
ADD COLUMN voting_id VARCHAR(255);

-- Add foreign key constraint
ALTER TABLE camara_deputados.politician_vote
ADD CONSTRAINT fk_politician_vote_voting_id
FOREIGN KEY (voting_id) REFERENCES camara_deputados.voting(id);
