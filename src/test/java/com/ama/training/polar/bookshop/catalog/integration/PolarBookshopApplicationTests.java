package com.ama.training.polar.bookshop.catalog.integration;

import com.ama.training.polar.bookshop.catalog.config.DataConfig;
import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = {"PolarBookshopApplicationTests", "integration"})
@AutoConfigureWebTestClient(timeout = "10000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
class PolarBookshopApplicationTests {

	//private WebTestClient webTestClient;

	private ApplicationContext applicationContext;


	@Autowired
	public PolarBookshopApplicationTests(WebTestClient webTestClient, ApplicationContext applicationContext){
		//this.webTestClient = webTestClient;
		this.applicationContext = applicationContext;
	}


	@BeforeEach
	void cleanData(@Autowired BookRepository bookRepository){
		bookRepository.deleteAll();
	}

	@Test
	@Order(1)
	void whenBookPostThenBookCreated(@Autowired  WebTestClient webTestClient) {
		Book expectedBook = Book.of("1234567890", "Title", "Author", 9.90);

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
	@Order(2)
	void whenMultipleBooksPostThenAllCreated(@Autowired WebTestClient webTestClient) {
		List<Book> allBooks = new ArrayList<>();
		allBooks.add(Book.of("01234567891", "Title 1", "Author", 9.90));
		allBooks.add(Book.of("01234567892", "Title 2", "Author", 9.90));
		allBooks.add(Book.of("01234567893", "Title 3", "Author", 9.90));

		allBooks.forEach(b -> webTestClient.post().uri("books").contentType(MediaType.APPLICATION_JSON).bodyValue(b).exchange());

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
	@Order(3)
	void whenBookPutThenDetailsUpdated(@Autowired WebTestClient testClient ){
		Book originalBook =  Book.of("1234567890", "Title", "Author", 9.90);

		testClient
				.post()
				.uri("/books")
				.bodyValue(originalBook)
				.exchange();

		Book updatedBook = Book.of("1234567890", "Updated Title", "Author", 10.90);

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
	@Order(4)
	void whenBookDeletedThenRemovedFromRepository(@Autowired WebTestClient testClient,
												  @Autowired BookRepository bookRepo){
		Book originalBook =  Book.of("1234567890", "Title", "Author", 9.90);
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
