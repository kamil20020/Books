CREATE TABLE IF NOT EXISTS USERS_ROLES(
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, role_id),
	CONSTRAINT fk_users_roles_user FOREIGN KEY (user_id) REFERENCES USERS(user_id),
	CONSTRAINT fk_users_roles_role FOREIGN KEY (role_id) REFERENCES ROLES(role_id)
);