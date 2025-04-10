CREATE TABLE IF NOT EXISTS REVOKED_REFRESH_TOKENS(
    token_id UUID NOT NULL,
    user_id UUID NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT pk_revoked_refresh_tokens PRIMARY KEY (token_id),
    CONSTRAINT fk_revoked_refresh_users FOREIGN KEY (token_id) REFERENCES USERS(user_id)
)