package com.example.exo14.service;

import com.example.exo14.exception.BookUnavailableException;
import com.example.exo14.exception.LoanNotFoundException;
import com.example.exo14.exception.MemberSuspendedException;
import com.example.exo14.model.Loan;
import com.example.exo14.repository.BookRepository;
import com.example.exo14.repository.LoanRepository;
import com.example.exo14.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        throw new UnsupportedOperationException();
    }

    public Loan returnBook(String loanId, LocalDate returnDate) {
        throw new UnsupportedOperationException();
    }

    public double calculatePenalty(String loanId, LocalDate returnDate) {
        throw new UnsupportedOperationException();
    }
}
