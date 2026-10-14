package com.example.library.controller;

import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.entity.Notification;
import com.example.library.entity.Transaction;
import com.example.library.service.BookService;
import com.example.library.service.MemberService;
import com.example.library.service.NotificationService;
import com.example.library.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LibraryController {

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    // --- Book Endpoints ---

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.addBook(book));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.updateBook(id, book));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/books/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author) {
        if (title != null) {
            return bookService.searchByTitle(title);
        } else if (author != null) {
            return bookService.searchByAuthor(author);
        }
        return bookService.getAllBooks();
    }

    // --- Member Endpoints ---

    @GetMapping("/members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/members")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        return ResponseEntity.ok(memberService.addMember(member));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        return ResponseEntity.ok(memberService.updateMember(id, member));
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    // --- Transaction Endpoints ---

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    @PutMapping("/transactions/{id}/return")
    public ResponseEntity<Transaction> returnBook(@PathVariable Long id) {
        Transaction tx = transactionService.returnBook(id);
        if (tx != null) {
            return ResponseEntity.ok(tx);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/transactions/member/{memberId}")
    public List<Transaction> getMemberTransactions(@PathVariable Long memberId) {
        return transactionService.getTransactionHistory(memberId);
    }

    // --- Notification Endpoints ---

    @GetMapping("/notifications/member/{memberId}")
    public List<Notification> getMemberNotifications(@PathVariable Long memberId) {
        return notificationService.getNotificationsForMember(memberId);
    }
}

