package edu.eci.cvds.tdd.library.returnloan;

import edu.eci.cvds.tdd.library.Library;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReturnTest {
    private Library library;
    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        library = new Library();
        user = new User("123", "John Doe");
        book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");

        library.addUser(user);
        library.addBook(book);

        loan = new Loan(user, book, LocalDate.now(), LoanStatus.ACTIVE);
        library.loanABook(user.getId(), book.getIsbn());
    }

    @Test
    void shouldReturnLoanSuccessfully() {
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan, "Loan should not be null after returning");
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus(), "Loan status should be RETURNED");
    }

    @Test
    void shouldNotReturnNullLoan() {
        assertNull(library.returnLoan(null), "Returning a null loan should return null");
    }

    @Test
    void shouldNotReturnNonExistentLoan() {
        Loan fakeLoan = new Loan(user, new Book("Fake Book", "Unknown", "000-0000000000"), LocalDate.now(), LoanStatus.ACTIVE);
        assertNull(library.returnLoan(fakeLoan), "Returning a non-existent loan should return null");
    }

    @Test
    void shouldNotReturnLoanForUnregisteredBook() {
        Book unregisteredBook = new Book("Unknown Book", "Unknown Author", "999-9999999999");
        Loan fakeLoan = new Loan(user, unregisteredBook, LocalDate.now(), LoanStatus.ACTIVE);
        assertNull(library.returnLoan(fakeLoan), "Returning a loan for an unregistered book should fail");
    }

    @Test
    void shouldNotModifyAlreadyReturnedLoan() {
        loan.setStatus(LoanStatus.RETURNED);
        Loan returnedLoan = library.returnLoan(loan);
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus(), "Loan status should remain RETURNED");
    }

    @Test
    void shouldUpdateReturnDateCorrectly() {
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan.getReturnDate(), "Return date should not be null");
        assertEquals(LocalDate.now(), returnedLoan.getReturnDate(), "Return date should be today's date");
    }

    @Test
    void shouldIncreaseBookCountAfterReturn() {
        int initialCount = library.getBookCount(book);
        library.returnLoan(loan);
        assertEquals(initialCount + 1, library.getBookCount(book), "Book count should increase after return");
    }

    @Test
    void shouldNotReturnLoanWithoutUser() {
        Loan invalidLoan = new Loan(null, book, LocalDate.now(), LoanStatus.ACTIVE);
        assertNull(library.returnLoan(invalidLoan), "Returning a loan without user should fail");
    }

    @Test
    void shouldNotReturnLoanWithoutBook() {
        Loan invalidLoan = new Loan(user, null, LocalDate.now(), LoanStatus.ACTIVE);
        assertNull(library.returnLoan(invalidLoan), "Returning a loan without book should fail");
    }

    @Test
    void shouldNotReturnLoanWithInvalidStatus() {
        Loan invalidLoan = new Loan(user, book, LocalDate.now(), LoanStatus.EXPIRED);
        assertNull(library.returnLoan(invalidLoan), "Returning a loan with invalid status should fail");
    }
}