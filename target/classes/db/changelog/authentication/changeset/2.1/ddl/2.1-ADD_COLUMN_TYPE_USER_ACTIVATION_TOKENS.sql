ALTER TABLE authentication.user_activation_tokens
ADD COLUMN type VARCHAR(255) NOT NULL DEFAULT 'activation';