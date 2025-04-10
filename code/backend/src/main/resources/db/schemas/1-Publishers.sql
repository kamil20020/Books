CREATE TABLE IF NOT EXISTS PUBLISHERS(
    publisher_id UUID,
    name VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT pk_publishers PRIMARY KEY (publisher_id)
);