package com.example.demo.service;

import com.example.demo.entity.Book;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookByIsbn(String isbn);
    Book addBook(Book book);
    Book updateBook(String isbn, Book book);
    void deleteBook(String isbn);
}