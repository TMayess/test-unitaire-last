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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, MemberRepository memberRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    public Loan createLoan(String memberId, String bookId, LocalDate loanDate) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        if (member.isSuspended()) {
            throw new MemberSuspendedException(memberId);
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        if (!book.isAvailable()) {
            throw new BookUnavailableException(bookId);
        }
        book.setAvailable(false);
        bookRepository.save(book);
        Loan loan = new Loan(java.util.UUID.randomUUID().toString(), memberId, bookId, loanDate);
        return loanRepository.save(loan);
    }

    public Loan returnBook(String loanId, LocalDate returnDate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        loan.setReturnDate(returnDate);

        Book book = bookRepository.findById(loan.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + loan.getBookId()));
        book.setAvailable(true);
        bookRepository.save(book);

        if (returnDate.isAfter(loan.getDueDate())) {
            Member member = memberRepository.findById(loan.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Member not found: " + loan.getMemberId()));
            member.setLateReturnsThisYear(member.getLateReturnsThisYear() + 1);
            if (member.getLateReturnsThisYear() >= 3) {
                member.setSuspended(true);
            }
            memberRepository.save(member);
        }

        return loanRepository.save(loan);
    }

    public double calculatePenalty(String loanId, LocalDate returnDate) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
        if (!returnDate.isAfter(loan.getDueDate())) {
            return 0.0;
        }
        long daysLate = loan.getDueDate().until(returnDate).getDays();
        return daysLate * 0.15;
    }
}
