CREATE TABLE IF NOT EXISTS USERS_ROLES(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, role_id)
);