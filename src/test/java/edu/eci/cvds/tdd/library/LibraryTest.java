package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class LibraryTest {
    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
    }

    @Test
    public void shouldAddALoanIfRequirementsAreMet() {
        User user = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        Loan loan = library.loanABook(user.getId(), book.getIsbn());
        boolean verification =  loan.getStatus() == LoanStatus.ACTIVE &&
                                loan.getUser() == user &&
                                loan.getBook() == book;
        assertTrue(verification);
    }

    @Test
    public void shouldNotAddALoanIfUserAlreadyLoanedTheSameBook() {
        User user = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.loanABook(user.getId(), book.getIsbn());
        assertThrows(IllegalArgumentException.class, () -> library.loanABook(user.getId(), book.getIsbn()));
    }
}
