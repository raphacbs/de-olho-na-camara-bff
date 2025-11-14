CREATE TABLE IF NOT EXISTS authentication.user_activation_tokens (
                                              id UUID PRIMARY KEY,
                                              user_id UUID NOT NULL,
                                              token VARCHAR(255) NOT NULL,
                                              created_at TIMESTAMP NOT NULL,
                                              expires_at TIMESTAMP NOT NULL,
                                              used BOOLEAN NOT NULL,
                                              CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES authentication.users(id)
                                          );