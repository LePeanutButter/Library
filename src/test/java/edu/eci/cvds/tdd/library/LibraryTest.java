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
class LibraryTest {
    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();
    }

    @Test
    void shouldAddALoanIfRequirementsAreMet() {
        User user = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
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
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.addUser(user);
        library.addBook(book);
        library.loanABook(user.getId(), book.getIsbn());
        assertNull(library.loanABook(user.getId(), book.getIsbn()));
    }

    @Test
    void shouldNotAddLoanIfUserDoesNotExist() {
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
        library.addBook(book);
        assertNull(library.loanABook("743fd6a8-f2d9-410d-bd6f-206a2907a30d", book.getIsbn()));
    }

    @Test
    void shouldNotAddLoanIfBookDoesNotExist() {
        User user = new User();
        user.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        library.addUser(user);
        assertNull(library.loanABook(user.getId(), "9780553593716"));
    }

    @Test
    void shouldNotAddLoanIfNoInstancesAreAvailable() {
        User user1 = new User();
        User user2 = new User();
        Book book = new Book("A Game of Thrones", "George R. R. Martin", "9780553593716");
        user1.setId("743fd6a8-f2d9-410d-bd6f-206a2907a30d");
        user2.setId("91b7bfc1-cc37-47a0-ad7f-649d8cfb146f");
        library.addUser(user1);
        library.addUser(user2);
        library.addBook(book);
        library.loanABook(user1.getId(), book.getIsbn());
        assertNull(library.loanABook(user2.getId(), book.getIsbn()));
    }
}
