CREATE TABLE IF NOT EXISTS REVOKED_REFRESH_TOKENS(
    token_id UUID,
    user_Id UUID NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT pk_revoked_refresh_tokens PRIMARY KEY (token_id)
);