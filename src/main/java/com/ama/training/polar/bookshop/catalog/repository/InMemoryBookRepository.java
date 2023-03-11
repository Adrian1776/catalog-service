package com.ama.training.polar.bookshop.catalog.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.ama.training.polar.bookshop.catalog.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookRepository implements BookRepository {
    private static final Map<String, Book> books =
            new ConcurrentHashMap<>();
    @Override
    public Iterable<Book> findAll() {
        return books.values();
    }
    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return existsByIsbn(isbn) ? Optional.of(books.get(isbn)) :
                Optional.empty();
    }
    @Override
    public boolean existsByIsbn(String isbn) {
        return books.get(isbn) != null;
    }
    @Override
    public Book save(Book book) {
        books.put(book.isbn(), book);
        return book;
    }
    @Override
    public void deleteByIsbn(String isbn) {
        books.remove(isbn);
    }

    @Override
    public void clearRepository() {
        books.clear();
    }

    @Override
    public void destroy() throws Exception {
        books.clear();
    }
}