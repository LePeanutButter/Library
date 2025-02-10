package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Library responsible for manage the loans and the users.
 */
public class Library {

    private final List<User> users;
    private final Map<Book, Integer> books;
    private final List<Loan> loans;

    public Library() {
        users = new ArrayList<>();
        books = new HashMap<>();
        loans = new ArrayList<>();
    }

    /**
     * Adds a new {@link edu.eci.cvds.tdd.library.book.Book} into the system, the book is store in a Map that contains
     * the {@link edu.eci.cvds.tdd.library.book.Book} and the amount of books available, if the book already exist the
     * amount should increase by 1 and if the book is new the amount should be 1, this method returns true if the
     * operation is successful false otherwise.
     *
     * @param book The book to store in the map.
     *
     * @return true if the book was stored false otherwise.
     */
    public boolean addBook(Book book) {
        boolean isValid = book != null &&
                book.getTittle() != null && !book.getTittle().trim().isEmpty() &&
                book.getAuthor() != null && !book.getAuthor().trim().isEmpty() &&
                book.getIsbn() != null && book.getIsbn().matches("\\d{3}-\\d{10}");

        if (isValid) {
            books.put(book, books.getOrDefault(book, 0) + 1);
        }
        return isValid;
    }

    /**
     * This method creates a new loan with for the User identify by the userId and the book identify by the isbn,
     * the loan should be store in the list of loans, to successfully create a loan is required to validate that the
     * book is available, that the user exist and the same user could not have a loan for the same book
     * {@link edu.eci.cvds.tdd.library.loan.LoanStatus#ACTIVE}, once these requirements are meet the amount of books is
     * decreased and the loan should be created with {@link edu.eci.cvds.tdd.library.loan.LoanStatus#ACTIVE} status and
     * the loan date should be the current date.
     *
     * @param userId id of the user.
     * @param isbn book identification.
     *
     * @return The new created loan.
     */
    public Loan loanABook(String userId, String isbn) {
        Loan loan = null;
        User user = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
        Book book = books.keySet()
                .stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
        boolean activeLoan = loans.stream()
                .anyMatch(l -> l.getUser().equals(user) && l.getBook().equals(book) && l.getStatus() == LoanStatus.ACTIVE);
        int availableBooks = books.get(book);
        if (user != null && book != null && !activeLoan && availableBooks > 0) {
            loan = new Loan();
            loan.setBook(book);
            loan.setUser(user);
            loan.setLoanDate(LocalDateTime.now());
            loan.setStatus(LoanStatus.ACTIVE);
            loans.add(loan);
            books.put(book, availableBooks - 1);
        }
        return loan;
    }

    /**
     * This method return a loan, meaning that the amount of books should be increased by 1, the status of the Loan
     * in the loan list should be {@link edu.eci.cvds.tdd.library.loan.LoanStatus#RETURNED} and the loan return
     * date should be the current date, validate that the loan exist.
     *
     * @param loan loan to return.
     *
     * @return the loan with the RETURNED status.
     */
    public Loan returnLoan(Loan loan) {
        if (loan == null || loan.getUser() == null || loan.getBook() == null) {
            return null;
        }
        for (Loan l : loans) {
            if (l.getUser().equals(loan.getUser()) && l.getBook().equals(loan.getBook()) && l.getStatus() == LoanStatus.ACTIVE) {
                l.setStatus(LoanStatus.RETURNED);
                l.setReturnDate(LocalDateTime.now().atStartOfDay());
                books.put(l.getBook(), books.getOrDefault(l.getBook(), 0) + 1);

                return l;
            }
        }
        return null;
    }

    /**
     * Adds a new user to the library.
     *
     * @param user User to add.
     * @return true if the user was added, false otherwise.
     */
    public boolean addUser(User user) {
        return users.add(user);
    }
}