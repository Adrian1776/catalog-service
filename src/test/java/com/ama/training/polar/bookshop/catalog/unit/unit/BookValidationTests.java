package com.ama.training.polar.bookshop.catalog.unit.unit;

import com.ama.training.polar.bookshop.catalog.model.Book;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {
    private static Validator validator;

    @BeforeAll
    static void setUp(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenFieldsCorrectThenValidationSuccedds() {
        var book = Book.of("1234567890", "Title", "Author", 9.8);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefineButIncorrecttThenValidationFails() {
        var book = Book.of("a1234567890", "Title", "Author", 9.8);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat((violations.iterator().next().getMessage()))
                .isEqualTo("The book ISBN in invalid");
    }
}
