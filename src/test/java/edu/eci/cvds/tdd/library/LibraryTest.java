package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
class LibraryTest {
    private Library library;

    @BeforeEach
    public void setUp() {
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

    @Test
    void shouldAddALoanIfRequirementsAreMet() {
        User user = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "978-0553593716");
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.addUser(user);
        library.addBook(book);
        Loan loan = library.loanABook(user.getId(), book.getIsbn());
        boolean verification =  loan.getStatus() == LoanStatus.ACTIVE &&
                                loan.getUser() == user &&
                                loan.getBook() == book;
        assertTrue(verification);
    }

    @Test
    void shouldNotAddALoanIfUserAlreadyLoanedTheSameBook() {
        User user = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "978-0553593716");
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        assertNull(library.loanABook(user.getId(), book.getIsbn()));
    }

    @Test
    void shouldNotAddLoanIfUserDoesNotExist() {
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "978-0553593716");
        library.addBook(book);
        assertNull(library.loanABook("743fd6a8-f2d9-410d-bd6f-206a2907a30d", book.getIsbn()));
    }

    @Test
    void shouldNotAddLoanIfBookDoesNotExist() {
        User user = new User();
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.addUser(user);
        assertNull(library.loanABook(user.getId(), "978-0553593716"));
    }

    @Test
    void shouldNotAddLoanIfNoInstancesAreAvailable() {
        User user1 = new User();
        User user2 = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "978-0553593716");
        user1.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        user2.setId("91b7bfc1-cc37-47a0-ad7f-649d8cfb146f");
        library.addUser(user1);
        library.addUser(user2);
        library.addBook(book);
        library.loanABook(user1.getId(), book.getIsbn());
        assertNull(library.loanABook(user2.getId(), book.getIsbn()));
    }

    @Test
    void shouldReturnLoanSuccessfully() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        Loan loan = library.loanABook(user.getId(), book.getIsbn());
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan, "Loan should not be null after returning");
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus(), "Loan status should be RETURNED");
    }

    @Test
    void shouldNotReturnNullLoan() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        assertNull(library.returnLoan(null), "Returning a null loan should return null");
    }

    @Test
    void shouldNotReturnNonExistentLoan() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        Book fakeBook = new Book("Fake Book", "Unknown", "000-0000000000");
        Loan fakeLoan = new Loan();
        fakeLoan.setUser(user);
        fakeLoan.setBook(fakeBook);
        fakeLoan.setLoanDate(LocalDate.now().atStartOfDay());
        fakeLoan.setStatus(LoanStatus.ACTIVE);
        assertNull(library.returnLoan(fakeLoan), "Returning a non-existent loan should return null");
    }

    @Test
    void shouldNotReturnLoanForUnregisteredBook() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        Book unregisteredBook = new Book("Unknown Book", "Unknown Author", "999-9999999999");
        Loan fakeLoan = new Loan();
        fakeLoan.setUser(user);
        fakeLoan.setBook(unregisteredBook);
        fakeLoan.setLoanDate(LocalDate.now().atStartOfDay());
        fakeLoan.setStatus(LoanStatus.ACTIVE);
        assertNull(library.returnLoan(fakeLoan), "Returning a loan for an unregistered book should fail");
    }

    @Test
    void shouldUpdateReturnDateCorrectly() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        Loan loan = library.loanABook(user.getId(), book.getIsbn());
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan, "Returned loan should not be null");
        assertNotNull(returnedLoan.getReturnDate(), "Return date should not be null");
        assertEquals(LocalDate.now(), returnedLoan.getReturnDate().toLocalDate(), "Return date should be today's date");
    }

    @Test
    void shouldIncreaseBookCountAfterReturn() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        Loan loan = library.loanABook(user.getId(), book.getIsbn());
        library.returnLoan(loan);
        Loan newLoan = library.loanABook(user.getId(), book.getIsbn());
        assertNotNull(newLoan, "Book should be available for loan after return");
    }


    @Test
    void shouldNotReturnLoanWithoutUser() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        Loan invalidLoan = new Loan();
        invalidLoan.setBook(book);
        invalidLoan.setLoanDate(LocalDate.now().atStartOfDay());
        invalidLoan.setStatus(LoanStatus.ACTIVE);
        assertNull(library.returnLoan(invalidLoan), "Returning a loan without user should fail");
    }

    @Test
    void shouldNotReturnLoanWithoutBook() {
        User user = new User();
        user.setId("123");
        user.setName("John Doe");
        Book book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        Loan invalidLoan = new Loan();
        invalidLoan.setUser(user);
        invalidLoan.setLoanDate(LocalDate.now().atStartOfDay());
        invalidLoan.setStatus(LoanStatus.ACTIVE);
        assertNull(library.returnLoan(invalidLoan), "Returning a loan without book should fail");
    }
}
