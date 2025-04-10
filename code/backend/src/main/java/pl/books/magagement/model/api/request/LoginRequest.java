package pl.books.magagement.model.api.request;

public record LoginRequest(
    String username,
    String password
){}
