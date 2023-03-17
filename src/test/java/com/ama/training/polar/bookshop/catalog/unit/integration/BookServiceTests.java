package com.ama.training.polar.bookshop.catalog.unit.integration;

import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.repository.BookRepository;
import com.ama.training.polar.bookshop.catalog.repository.InMemoryBookRepository;
import com.ama.training.polar.bookshop.catalog.service.BookService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)

@ActiveProfiles(profiles = {"BookServiceTests"})
//@TestExecutionListeners(value = {DirtiesContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
//@ContextConfiguration
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Execution(ExecutionMode.SAME_THREAD)
public class BookServiceTests {

    private ApplicationContext applicationContext;


    @Autowired
    public BookServiceTests(BookService bookService, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testBookAdditionToCatalog(@Autowired BookService bookService){
        Book originalBook = new Book("1234567890", "Title", "Author", 9.8);

        bookService.addBookToCalalog(originalBook);
        Book retrievedBook = bookService.viewBookDetails("1234567890");

        assertThat(retrievedBook).isNotNull();
        assertThat(retrievedBook.isbn()).isEqualTo(originalBook.isbn());

        //bookService.removeBookFromCatalog("1234567890");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testBookRepositoryEmptyByDefault(@Autowired BookService bookService){
        Iterable<Book> books = bookService.viewBookList();

        assertThat(books.iterator().hasNext()).isFalse();
    }

}
