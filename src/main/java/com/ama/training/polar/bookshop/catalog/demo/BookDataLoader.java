package com.ama.training.polar.bookshop.catalog.demo;

import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test-data")
public class BookDataLoader {

    @Autowired
    private BookRepository bookRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData(){
        bookRepo.deleteAll();
        var book1 = Book.of("1234567891", "Northen Lights", "Lyra Silverstart", 9.2);
        var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.9);
        bookRepo.saveAll(List.of(book1, book2));
    }
}
