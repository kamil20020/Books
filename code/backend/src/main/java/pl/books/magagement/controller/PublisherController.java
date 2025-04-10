package pl.books.magagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.books.magagement.model.api.request.CreatePublisherRequest;
import pl.books.magagement.model.api.response.PublisherHeader;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.model.mappers.PublisherMapper;
import pl.books.magagement.service.PublisherService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    private final PublisherMapper publisherMapper;

    @GetMapping
    public ResponseEntity<Page<PublisherHeader>> getPage(@ParameterObject Pageable pageable){

        Page<PublisherEntity> publishersPage = publisherService.getPage(pageable);

        Page<PublisherHeader> publishersHeadersPage = publishersPage.map(publisherEntity -> {
            return publisherMapper.publisherEntityToPublisherHeader(publisherEntity);
        });

        return ResponseEntity.ok(publishersHeadersPage);
    }

    @GetMapping(value = "/{publisherId}/authors")
    public ResponseEntity<Page<AuthorEntity>> getPublisherAuthors(
        @PathVariable("publisherId") String publisherIdStr,
        @ParameterObject Pageable pageable
    ){
        UUID publisherId = UUID.fromString(publisherIdStr);

        Page<AuthorEntity> publisherAuthorsPage = publisherService.getPublisherAuthors(publisherId, pageable);

        return ResponseEntity.ok(publisherAuthorsPage);
    }


    @GetMapping(value = "/{publisherId}/books")
    public ResponseEntity<Page<BookEntity>> getPublisherBooks(
        @PathVariable("publisherId") String publisherIdStr,
        @ParameterObject Pageable pageable
    ){
        UUID publisherId = UUID.fromString(publisherIdStr);

        Page<BookEntity> publisherBooksPage = publisherService.getPublisherBooks(publisherId, pageable);

        return ResponseEntity.ok(publisherBooksPage);
    }

    @PostMapping
    public ResponseEntity<PublisherEntity> createPublisher(@RequestBody @Valid CreatePublisherRequest createPublisher){

        PublisherEntity createdPublisher = publisherService.create(createPublisher.name());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPublisher);
    }

    @DeleteMapping(value = "/{publisherId}")
    public ResponseEntity<Void> deletePublisherById(@PathVariable("publisherId") String publisherIdStr){

        UUID publisherId = UUID.fromString(publisherIdStr);

        publisherService.deleteById(publisherId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
