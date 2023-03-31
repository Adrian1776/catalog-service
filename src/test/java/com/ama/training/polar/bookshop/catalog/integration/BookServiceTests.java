package com.ama.training.polar.bookshop.catalog.integration;

import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@ActiveProfiles(profiles = {"BookServiceTests", "integration"})
@Transactional
@Disabled
public class BookServiceTests {

    private ApplicationContext applicationContext;


    @Autowired
    public BookServiceTests(BookService bookService, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Test
   // @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testBookAdditionToCatalog(@Autowired BookService bookService){
        Book originalBook = Book.of("1234567895", "Title", "Author", 9.8);

        bookService.addBookToCalalog(originalBook);
        Book retrievedBook = bookService.viewBookDetails("1234567895");

        assertThat(retrievedBook).isNotNull();
        assertThat(retrievedBook.isbn()).isEqualTo(originalBook.isbn());

        //bookService.removeBookFromCatalog("1234567890");
    }

    @Test
    void testBookRepositoryEmptyByDefault(@Autowired BookService bookService){
        Iterable<Book> books = bookService.viewBookList();

        assertThat(books.iterator().hasNext()).isFalse();
    }

}
