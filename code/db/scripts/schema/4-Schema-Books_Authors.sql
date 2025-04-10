CREATE TABLE IF NOT EXISTS BOOKS_AUTHORS(
    book_id UUID NOT NULL,
    author_id UUID NOT NULL,
    CONSTRAINT pk_books_authors PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_books_authors_book FOREIGN KEY (book_id) REFERENCES BOOKS(book_id),
    CONSTRAINT fk_books_authors_author FOREIGN KEY (author_id) REFERENCES AUTHORS(author_id)
);