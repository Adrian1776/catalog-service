package com.ama.training.polar.bookshop.catalog.unit.integration;

import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.repository.BookRepository;
import com.ama.training.polar.bookshop.catalog.repository.InMemoryBookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = {"PolarBookshopApplicationTests"})
//@TestExecutionListeners(value = {DirtiesContextTestExecutionListener.class}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
//@ContextConfiguration
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Execution(ExecutionMode.SAME_THREAD)
class PolarBookshopApplicationTests {

	//private WebTestClient webTestClient;

	private ApplicationContext applicationContext;

	@Autowired
	public PolarBookshopApplicationTests(WebTestClient webTestClient, ApplicationContext applicationContext){
		//this.webTestClient = webTestClient;
		this.applicationContext = applicationContext;
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void whenBookPostThenBookCreated(@Autowired  WebTestClient webTestClient) {
		Book expectedBook = new Book("1234567890", "Title", "Author", 9.90);

		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn())
							.isEqualTo(expectedBook.isbn());
				});
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void whenMultipleBooksPostThenAllCreated(@Autowired WebTestClient webTestClient) {
		List<Book> allBooks = new ArrayList<>();
		allBooks.add(new Book("01234567890", "Title 1", "Author", 9.90));
		allBooks.add(new Book("01234567890", "Title 2", "Author", 9.90));
		allBooks.add(new Book("01234567890", "Title 3", "Author", 9.90));

		allBooks.forEach(b -> webTestClient.post().uri("books").exchange());

		List<Book> receivedBooks = webTestClient
				.get()
				.uri("/books")
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(Book.class)
				.returnResult()
				.getResponseBody();

		//As we don't have equals() on the book record we are falling back on this
		assertThat(receivedBooks).allMatch(rb -> allBooks.stream().anyMatch(ob -> ob.isbn().equals(rb.isbn())));
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void whenBookPutThenDetailsUpdated(@Autowired WebTestClient testClient ){
		Book originalBook =  new Book("1234567890", "Title", "Author", 9.90);

		testClient
				.post()
				.uri("/books")
				.bodyValue(originalBook)
				.exchange();

		Book updatedBook = new Book("1234567890", "Updated Title", "Author", 10.90);

		testClient
				.put()
				.uri("/books/" + updatedBook.isbn())
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(b -> {
					assertThat(b.isbn()).isEqualTo(updatedBook.isbn());
					assertThat(b.author()).isEqualTo(updatedBook.author());
					assertThat(b.price()).isEqualTo(updatedBook.price());
				});
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void whenBookDeletedThenRemovedFromRepository(@Autowired WebTestClient testClient,
												  @Autowired BookRepository bookRepo){
		Book originalBook =  new Book("1234567890", "Title", "Author", 9.90);
		bookRepo.save(originalBook);

		testClient
				.method(HttpMethod.DELETE)
				.uri("/books/" + originalBook.isbn())
				.bodyValue(originalBook)
				.exchange()
				.expectStatus().isNoContent();

		assertThat(bookRepo.existsByIsbn(originalBook.isbn())).isFalse();
	}

}
