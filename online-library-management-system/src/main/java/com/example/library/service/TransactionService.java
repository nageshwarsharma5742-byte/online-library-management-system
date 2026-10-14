package com.example.library.service;

import com.example.library.entity.Book;
import com.example.library.entity.Transaction;
import com.example.library.repository.BookRepository;
import com.example.library.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, BookRepository bookRepository) {
        this.transactionRepository = transactionRepository;
        this.bookRepository = bookRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getBorrowDate() == null) {
            transaction.setBorrowDate(new Date());
        }
        transaction.setReturned(false);

        if (transaction.getBook() != null && transaction.getBook().getId() != null) {
            bookRepository.findById(transaction.getBook().getId()).ifPresent(book -> {
                book.setAvailable(false);
                bookRepository.save(book);
                transaction.setBook(book);
            });
        }
        return transactionRepository.save(transaction);
    }

    public Transaction borrowBook(Transaction transaction) {
        return createTransaction(transaction);
    }

    public Transaction returnBook(Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);
        if (transaction != null) {
            transaction.setReturned(true);
            transaction.setReturnDate(new Date());

            if (transaction.getBook() != null) {
                Book book = transaction.getBook();
                book.setAvailable(true);
                bookRepository.save(book);
            }
            return transactionRepository.save(transaction);
        }
        return null;
    }

    public List<Transaction> getTransactionHistory(Long memberId) {
        return transactionRepository.findByMemberId(memberId);
    }
}