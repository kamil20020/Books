CREATE TABLE IF NOT EXISTS AUTHORS (
    author_id UUID,
    main_publisher_id UUID,
    firstname VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    published_books_count SMALLINT NOT NULL DEFAULT 0,
    CONSTRAINT pk_authors PRIMARY KEY (author_id),
    CONSTRAINT fk_authors_publisher FOREIGN KEY (main_publisher_id) REFERENCES PUBLISHERS(publisher_id),
    CONSTRAINT check_published_books_count CHECK (published_books_count >= 0)
);