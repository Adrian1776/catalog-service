package com.ama.training.polar.bookshop.catalog.controller;

import com.ama.training.polar.bookshop.catalog.json.JsonExamples;
import com.ama.training.polar.bookshop.catalog.model.Book;
import com.ama.training.polar.bookshop.catalog.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("books")
@Tag(name = "Books API")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(
            operationId = "getBooks",
            summary = "GET a collection of all the books registered",
            responses = {@ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = Book.class)),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(
                                    name = "Standard Book",
                                    summary = "A standardized example of a complete return entity",
                                    description = JsonExamples.BOOKS_JSON_EXAMPLE)}),
                    description = "Success Response")})
    public Iterable<Book> getAll(){
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    @Operation(
            operationId = "getBook",
            summary = "Attempts to GET a single book by its ISBN",
            parameters = {@Parameter(name = "isbn", description = "The search ISBN", example = "0123456789", in = ParameterIn.PATH)},
            responses = {
                    @ApiResponse(
                        responseCode = "200",
                        content = @Content(
                            schema = @Schema(implementation = Book.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE),
                        description = "Success Response"),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    schema = @Schema(implementation = Book.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(name = "Book not Found", value = "The book with ISBN {isbn} was not found")}),
                            description = "Error Response - caused by providing an ISBN not found in the application data stores"
                            )})
    public Book getByIsbn(@PathVariable String isbn){
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            operationId = "postBook",
            summary = "Attempts to POST a new book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON representation of the new entity",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class))),
            responses = {@ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            schema = @Schema(implementation = Book.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE),
                    description = "Successfull creation - a representation of the newly created entity is returned"
                    ),
                        @ApiResponse(
                                responseCode = "422",
                                content = @Content(
                                        schema = @Schema(implementation = String.class),
                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                        examples = {@ExampleObject(name = "Book Already Exists", value = "A book with ISBN {isbn} already exists.")}
                                ))})
    public Book post(@Valid @RequestBody Book book){
        return bookService.addBookToCalalog(book);
    }

    @Operation(
            operationId = "putBook",
            summary = "Attempts an edit of the current book",
            description = "Attempts to edit a current book, as identified by the ISBN. If the book is not found a new entity is created",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON representation for the book of which edits are edited",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Book.class))),
            parameters = {@Parameter(name = "isbn", description = "The search ISBN", example = "0123456789", in = ParameterIn.PATH)},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = Book.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE),
                            description = "JSON representation of the edited/newly created book")}
                    )
    @PutMapping("{isbn}")
    public Book put(@PathVariable String isbn, @Valid @RequestBody Book book){
        return bookService.editBookDetails(isbn, book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            operationId = "deleteBook",
            summary = "Deletes the Book with the given ISBN if its found",
            parameters = {@Parameter(name = "isbn", description = "The Book's ISBN", example = "01234567890", in = ParameterIn.PATH)},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            content = @Content,
                            description = "Answering the delete call"
                    )})
    public void delete(@PathVariable String isbn){
        bookService.removeBookFromCatalog(isbn);
    }

}
