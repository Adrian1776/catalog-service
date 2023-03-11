package com.ama.training.polar.bookshop.catalog.repository;

import com.ama.training.polar.bookshop.catalog.model.Book;
import org.springframework.beans.factory.DisposableBean;

import java.util.Optional;

public interface BookRepository extends DisposableBean {
    Iterable<Book> findAll();
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    Book save(Book book);
    void deleteByIsbn(String isbn);

    void clearRepository();
}
