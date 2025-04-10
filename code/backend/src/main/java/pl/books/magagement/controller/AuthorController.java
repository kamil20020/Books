package pl.books.magagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.books.magagement.model.api.request.CreateAuthorRequest;
import pl.books.magagement.model.api.response.AuthorHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.internal.CreateAuthor;
import pl.books.magagement.model.mappers.AuthorMapper;
import pl.books.magagement.service.AuthorService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/authors")
public class AuthorController {

    private final AuthorService authorService;

    private final AuthorMapper authorMapper;

    @GetMapping
    public ResponseEntity<Page<AuthorHeader>> getPage(@ParameterObject Pageable pageable){

        Page<AuthorEntity> gotAuthorsPage = authorService.getPage(pageable);

        Page<AuthorHeader> gotAuthorsHeadersPage = gotAuthorsPage.map(author -> {
            return authorMapper.authorEntityToAuthorHeader(author);
        });

        return ResponseEntity.ok(gotAuthorsHeadersPage);
    }

    @GetMapping(value = "/{authorId}/books")
    public ResponseEntity<Page<BookEntity>> getAuthorBooksPage(
        @PathVariable("authorId") String authorIdStr,
        @ParameterObject Pageable pageable
    ){
        UUID authorId = UUID.fromString(authorIdStr);

        Page<BookEntity> gotBooksPage = authorService.getAuthorBooksPage(authorId, pageable);

        return ResponseEntity.ok(gotBooksPage);
    }

    @PostMapping
    public ResponseEntity<AuthorHeader> create(@RequestBody @Valid CreateAuthorRequest request){

        CreateAuthor createAuthor = authorMapper.createAuthorRequestToCreateAuthor(request);

        AuthorEntity createdAuthorEntity = authorService.create(createAuthor);
        AuthorHeader createdAuthorHeader = authorMapper.authorEntityToAuthorHeader(createdAuthorEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthorHeader);
    }

    @DeleteMapping(value = "/{authorId}")
    public ResponseEntity<Void> deleteById(@PathVariable("authorId") String authorIdStr){

        UUID authorId = UUID.fromString(authorIdStr);

        authorService.deleteById(authorId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
