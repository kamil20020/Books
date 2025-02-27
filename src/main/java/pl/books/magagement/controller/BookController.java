package pl.books.magagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.books.magagement.model.api.request.CreateBookRequest;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.internal.CreateBook;
import pl.books.magagement.model.mappers.BookMapper;
import pl.books.magagement.service.BookService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/books")
public class BookController {

    private final BookService bookService;

    private final BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<Page<BookEntity>> getBooksPage(@ParameterObject Pageable pageable){

        Page<BookEntity> gotBooksPage = bookService.getBooksPage(pageable);

        return ResponseEntity.ok(gotBooksPage);
    }

    @PostMapping
    public ResponseEntity<BookEntity> create(@RequestBody @Valid CreateBookRequest createBookRequest){

        CreateBook createBook = bookMapper.createBookRequestToCreateBook(createBookRequest);

        BookEntity createdBook = bookService.create(createBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @DeleteMapping(value = "/{bookId}")
    public ResponseEntity<Void> deleteById(@PathVariable("bookId") String bookIdStr){

        UUID bookId = UUID.fromString(bookIdStr);

        bookService.deleteById(bookId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
