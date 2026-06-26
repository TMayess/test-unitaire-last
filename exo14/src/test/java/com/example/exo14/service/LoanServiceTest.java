package com.example.exo14.service;

import com.example.exo14.exception.BookUnavailableException;
import com.example.exo14.exception.LoanNotFoundException;
import com.example.exo14.exception.MemberSuspendedException;
import com.example.exo14.model.Book;
import com.example.exo14.model.Loan;
import com.example.exo14.model.Member;
import com.example.exo14.repository.BookRepository;
import com.example.exo14.repository.LoanRepository;
import com.example.exo14.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    private Member activeMember;
    private Member suspendedMember;
    private Book availableBook;
    private Book unavailableBook;
    private final LocalDate today = LocalDate.of(2024, 1, 15);

    @BeforeEach
    void setUp() {
        activeMember = new Member("M001", "Alice");
        suspendedMember = new Member("M002", "Bob");
        suspendedMember.setSuspended(true);

        availableBook = new Book("B001", "Clean Code");
        unavailableBook = new Book("B002", "Refactoring");
        unavailableBook.setAvailable(false);
    }

    @Test
    void createLoan_shouldReturnLoanWithCorrectDueDate() {
        // Arrange
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Loan loan = loanService.createLoan("M001", "B001", today);

        // Assert
        assertThat(loan.getDueDate()).isEqualTo(today.plusDays(21));
        assertThat(loan.getMemberId()).isEqualTo("M001");
        assertThat(loan.getBookId()).isEqualTo("B001");
    }

    @Test
    void createLoan_shouldMarkBookAsUnavailable() {
        // Arrange
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanService.createLoan("M001", "B001", today);

        // Assert
        assertThat(availableBook.isAvailable()).isFalse();
        verify(bookRepository).save(availableBook);
    }

    @Test
    void createLoan_shouldThrowWhenBookIsUnavailable() {
        // Arrange
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(bookRepository.findById("B002")).thenReturn(Optional.of(unavailableBook));

        // Act & Assert
        assertThatThrownBy(() -> loanService.createLoan("M001", "B002", today))
                .isInstanceOf(BookUnavailableException.class);
    }

    @Test
    void createLoan_shouldThrowWhenMemberIsSuspended() {
        // Arrange
        when(memberRepository.findById("M002")).thenReturn(Optional.of(suspendedMember));

        // Act & Assert
        assertThatThrownBy(() -> loanService.createLoan("M002", "B001", today))
                .isInstanceOf(MemberSuspendedException.class);
    }

    @Test
    void returnBook_shouldSetReturnDate() {
        // Arrange
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(10));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Loan returned = loanService.returnBook("L001", today);

        // Assert
        assertThat(returned.getReturnDate()).isEqualTo(today);
    }

    @Test
    void returnBook_shouldMarkBookAsAvailable() {
        // Arrange
        availableBook.setAvailable(false);
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(10));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanService.returnBook("L001", today);

        // Assert
        assertThat(availableBook.isAvailable()).isTrue();
    }

    @Test
    void returnBook_shouldThrowWhenLoanNotFound() {
        // Arrange
        when(loanRepository.findById("L999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> loanService.returnBook("L999", today))
                .isInstanceOf(LoanNotFoundException.class);
    }

    @Test
    void calculatePenalty_shouldReturnZeroWhenReturnedOnTime() {
        // Arrange
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(10));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));

        // Act
        double penalty = loanService.calculatePenalty("L001", today);

        // Assert
        assertThat(penalty).isEqualTo(0.0);
    }

    @Test
    void calculatePenalty_shouldCalculate0_15PerDayLate() {
        // Arrange
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(25));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));

        // Act
        double penalty = loanService.calculatePenalty("L001", today);

        // Assert
        assertThat(penalty).isEqualTo(4 * 0.15);
    }

    @Test
    void calculatePenalty_shouldReturnZeroWhenReturnedExactlyOnDueDate() {
        // Arrange
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(21));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));

        // Act
        double penalty = loanService.calculatePenalty("L001", today);

        // Assert
        assertThat(penalty).isEqualTo(0.0);
    }

    @Test
    void returnBook_shouldIncrementLateReturnCountWhenLate() {
        // Arrange
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(25));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanService.returnBook("L001", today);

        // Assert
        assertThat(activeMember.getLateReturnsThisYear()).isEqualTo(1);
    }

    @Test
    void returnBook_shouldSuspendMemberAfterThreeLateReturns() {
        // Arrange
        activeMember.setLateReturnsThisYear(2);
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(25));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanService.returnBook("L001", today);

        // Assert
        assertThat(activeMember.isSuspended()).isTrue();
        verify(memberRepository).save(activeMember);
    }

    @Test
    void returnBook_shouldNotSuspendMemberWithOnlyTwoLateReturns() {
        // Arrange
        activeMember.setLateReturnsThisYear(1);
        Loan loan = new Loan("L001", "M001", "B001", today.minusDays(25));
        when(loanRepository.findById("L001")).thenReturn(Optional.of(loan));
        when(bookRepository.findById("B001")).thenReturn(Optional.of(availableBook));
        when(memberRepository.findById("M001")).thenReturn(Optional.of(activeMember));
        when(loanRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        loanService.returnBook("L001", today);

        // Assert
        assertThat(activeMember.isSuspended()).isFalse();
    }
}
