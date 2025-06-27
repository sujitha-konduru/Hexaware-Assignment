package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookNotFoundException;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookByIsbn(String isbn) {
        return bookRepository.findById(isbn)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ISBN: " + isbn));
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(String isbn, Book book) {
        if (!bookRepository.existsById(isbn)) {
            throw new BookNotFoundException("Cannot update. Book not found with ISBN: " + isbn);
        }
        book.setIsbn(isbn);
        return bookRepository.save(book);
    }

    public void deleteBook(String isbn) {
        if (!bookRepository.existsById(isbn)) {
            throw new BookNotFoundException("Cannot delete. Book not found with ISBN: " + isbn);
        }
        bookRepository.deleteById(isbn);
    }
}