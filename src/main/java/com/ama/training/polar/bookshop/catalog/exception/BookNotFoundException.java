package com.ama.training.polar.bookshop.catalog.exception;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String isbn){
        super("The book with ISBN " + isbn + " was not found");
    }
}
