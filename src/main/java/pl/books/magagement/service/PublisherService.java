package pl.books.magagement.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.books.magagement.model.entity.AuthorEntity;
import pl.books.magagement.model.entity.BookEntity;
import pl.books.magagement.model.entity.PublisherEntity;
import pl.books.magagement.repository.AuthorRepository;
import pl.books.magagement.repository.BookRepository;
import pl.books.magagement.repository.PublisherRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final PublisherRepository publisherRepository;
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Page<PublisherEntity> getPage(Pageable pageable){

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return publisherRepository.findAll(pageable);
    }

    public Page<AuthorEntity> getPublisherAuthors(UUID publisherId, Pageable pageable) throws EntityNotFoundException{

        if(!publisherRepository.existsById(publisherId)){
            throw new EntityNotFoundException("Publisher was not found by given id");
        }

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return authorRepository.findAllByMainPublisherId(publisherId, pageable);
    }

    public Page<BookEntity> getPublisherBooks(UUID publisherId, Pageable pageable) throws EntityNotFoundException{

        if(!publisherRepository.existsById(publisherId)){
            throw new EntityExistsException("Publisher was not found by given id");
        }

        if(pageable == null){
            pageable = Pageable.unpaged();
        }

        return bookRepository.findAllByPublisherId(publisherId, pageable);
    }

    public PublisherEntity getById(UUID id) throws EntityNotFoundException{

        return publisherRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Publisher was not found by given id"));
    }

    @Transactional
    public PublisherEntity create(String name) throws EntityExistsException{

        if(publisherRepository.existsByNameIgnoreCase(name)){
            throw new EntityExistsException("Duplicate publisher name");
        }

        PublisherEntity publisher = new PublisherEntity(name);

        return publisherRepository.save(publisher);
    }

    @Transactional
    public void deleteById(UUID id) throws EntityNotFoundException{

        if(!publisherRepository.existsById(id)){
            throw new EntityNotFoundException("Publisher was not found by given id");
        }

        publisherRepository.deleteById(id);
    }
}
