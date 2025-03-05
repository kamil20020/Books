CREATE TABLE IF NOT EXISTS BOOKS (
    book_id UUID,
    publisher_id UUID NOT NULL,
    title VARCHAR(50) NOT NULL UNIQUE,
    price DECIMAL(8, 2) NOT NULL,
    publication_date DATE NOT NULL,
    picture BYTEA,
    CONSTRAINT pk_books PRIMARY KEY (book_id),
    CONSTRAINT fk_books_publisher FOREIGN KEY (publisher_id) REFERENCES PUBLISHERS(publisher_id),
    CONSTRAINT check_books_price CHECK (price > 0)
);