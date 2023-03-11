package com.ama.training.polar.bookshop.catalog.unit.json;

import com.ama.training.polar.bookshop.catalog.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookJsonTests {
    
    @Autowired
    private JacksonTester<Book> jsonTester;
    
    @Test
    void testSerialize() throws Exception{
        Book originalBook = new Book("1234567890", "Title", "Author", 9.8);

        JsonContent<Book> writenBook = jsonTester.write(originalBook);

        assertThat(writenBook).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(originalBook.isbn());
        assertThat(writenBook).extractingJsonPathStringValue("@.title")
                .isEqualTo(originalBook.title());
        assertThat(writenBook).extractingJsonPathStringValue("@.author")
                .isEqualTo(originalBook.author());
        assertThat(writenBook).extractingJsonPathNumberValue("@.price")
                .isEqualTo(originalBook.price());
    }

    @Test
    void testDeserialize() throws Exception {
        var content = """ 
            {
                "isbn": "1234567890",
                "title": "Title",
                "author": "Author",
                "price": 9.90
            }
        """;
        assertThat(jsonTester.parse(content))
                 .usingRecursiveComparison()
                .isEqualTo(new Book("1234567890", "Title", "Author", 9.90));
    }
    
}
