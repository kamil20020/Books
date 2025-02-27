package pl.books.magagement.config;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionsGlobalHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<String> handleNotFoundEntity(EntityNotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = {EntityExistsException.class})
    public ResponseEntity<String> handleConflictEntities(RuntimeException e){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalRequestArgument(RuntimeException e){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleInvalidFormArguments(MethodArgumentNotValidException e){

        Map<String, String> errorsMappings = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> {

            String formElementName = error.getField();
            String message = error.getDefaultMessage();

            errorsMappings.put(formElementName, message);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsMappings);
    }
}
