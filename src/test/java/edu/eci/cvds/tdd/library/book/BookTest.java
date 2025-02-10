package edu.eci.cvds.tdd.library.book;

import edu.eci.cvds.tdd.library.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {
    private Library library;

    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void shouldAddNewBookSuccessfully() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        assertTrue(library.addBook(book), "The book should be added successfully");
    }

    @Test
    void shouldIncreaseBookCountIfBookAlreadyExists() {
        Book book1 = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        Book book2 = new Book("Clean Code", "Robert C. Martin", "978-0132350884");
        library.addBook(book1);
        assertTrue(library.addBook(book2), "The count of the book should increase");
    }

    @Test
    void shouldNotAddNullBook() {
        assertFalse(library.addBook(null), "Null books should not be added");
    }

    @Test
    void shouldHandleDuplicateISBNWithDifferentInstances() {
        Book book1 = new Book("Refactoring", "Martin Fowler", "978-0201485677");
        Book book2 = new Book("Refactoring", "Martin Fowler", "978-0201485677");
        library.addBook(book1);
        assertTrue(library.addBook(book2), "Duplicate ISBN books should increase the count");
    }

    @Test
    void shouldNotAddBookWithEmptyTitle() {
        Book book = new Book("", "Robert C. Martin", "978-0132350884");
        assertFalse(library.addBook(book), "Books with empty titles should not be added");
    }

    @Test
    void shouldNotAddBookWithEmptyAuthor() {
        Book book = new Book("Clean Code", "", "978-0132350884");
        assertFalse(library.addBook(book), "Books with empty authors should not be added");
    }

    @Test
    void shouldNotAddBookWithEmptyISBN() {
        Book book = new Book("Clean Code", "Robert C. Martin", "");
        assertFalse(library.addBook(book), "Books with empty ISBNs should not be added");
    }

    @Test
    void shouldNotAddBookWithInvalidISBNFormat() {
        Book book = new Book("Clean Code", "Robert C. Martin", "INVALID-ISBN");
        assertFalse(library.addBook(book), "Books with invalid ISBN format should not be added");
    }

    @Test
    void shouldNotAddBookWithWhitespaceTitle() {
        Book book = new Book("   ", "Robert C. Martin", "978-0132350884");
        assertFalse(library.addBook(book), "Books with whitespace titles should not be added");
    }

    @Test
    void shouldNotAddBookWithWhitespaceAuthor() {
        Book book = new Book("Clean Code", "   ", "978-0132350884");
        assertFalse(library.addBook(book), "Books with whitespace authors should not be added");
    }

    @Test
    void shouldNotAddBookWithWhitespaceISBN() {
        Book book = new Book("Clean Code", "Robert C. Martin", "   ");
        assertFalse(library.addBook(book), "Books with whitespace ISBNs should not be added");
    }

    @Test
    void shouldNotAddBookWithLongISBN() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978-01323508848888");
        assertFalse(library.addBook(book), "Books with too long ISBNs should not be added");
    }

    @Test
    void shouldNotAddBookWithShortISBN() {
        Book book = new Book("Clean Code", "Robert C. Martin", "978");
        assertFalse(library.addBook(book), "Books with too short ISBNs should not be added");
    }
}