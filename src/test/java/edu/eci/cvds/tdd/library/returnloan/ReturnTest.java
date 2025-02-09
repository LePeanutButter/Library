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

class ReturnTest {

    private Library library;
    private User user;
    private Book book;
    private Loan loan;

    @BeforeEach
    void setUp() {
        library = new Library();

        user = new User();
        user.setId("123");
        user.setName("John Doe");
        book = new Book("Effective Java", "Joshua Bloch", "978-0134685991");
        library.addUser(user);
        library.addBook(book);
        loan = library.loanABook(user.getId(), book.getIsbn());
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
        Loan returnedLoan = library.returnLoan(loan);
        assertNotNull(returnedLoan, "Returned loan should not be null");
        assertNotNull(returnedLoan.getReturnDate(), "Return date should not be null");
        assertEquals(LocalDate.now(), returnedLoan.getReturnDate().toLocalDate(), "Return date should be today's date");
    }

    @Test
    void shouldIncreaseBookCountAfterReturn() {
        library.returnLoan(loan);
        Loan newLoan = library.loanABook(user.getId(), book.getIsbn());
        assertNotNull(newLoan, "Book should be available for loan after return");
    }


    @Test
    void shouldNotReturnLoanWithoutUser() {
        Loan invalidLoan = new Loan();
        invalidLoan.setBook(book);
        invalidLoan.setLoanDate(LocalDate.now().atStartOfDay());
        invalidLoan.setStatus(LoanStatus.ACTIVE);

        assertNull(library.returnLoan(invalidLoan), "Returning a loan without user should fail");
    }

    @Test
    void shouldNotReturnLoanWithoutBook() {
        Loan invalidLoan = new Loan();
        invalidLoan.setUser(user);
        invalidLoan.setLoanDate(LocalDate.now().atStartOfDay());
        invalidLoan.setStatus(LoanStatus.ACTIVE);

        assertNull(library.returnLoan(invalidLoan), "Returning a loan without book should fail");
    }
}
