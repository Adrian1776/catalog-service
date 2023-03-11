package com.ama.training.polar.bookshop.catalog.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String isbn){
        super("A book with ISBN " + isbn + " already exists." );
    }
}
