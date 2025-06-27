package com.example.demo;

import com.example.demo.controller.BookController;
import com.example.demo.entity.Book;
import com.example.demo.service.BookService;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CodingChallengeApplicationTests {

	


	    @Test
	    void testGetAllBooks() {
	        BookService mockService = mock(BookService.class);
	        BookController controller = new BookController();
	        controller.setBookService(mockService); // Add a setter or use @Autowired constructor

	        Book book = new Book();
	        book.setIsbn("101");
	        book.setTitle("Java Basics");
	        book.setAuthor("John");
	        book.setPublicationYear(2020);

	        when(mockService.getAllBooks()).thenReturn(Arrays.asList(book));

	        List<Book> books = controller.getAllBooks();
	        assertEquals(1, books.size());
	        assertEquals("Java Basics", books.get(0).getTitle());
	    }

	    @Test
	    void testGetBook() {
	        BookService mockService = mock(BookService.class);
	        BookController controller = new BookController();
	        controller.setBookService(mockService);

	        Book book = new Book();
	        book.setIsbn("102");
	        book.setTitle("Spring Boot");
	        book.setAuthor("Alice");
	        book.setPublicationYear(2021);

	        when(mockService.getBookByIsbn("102")).thenReturn(book);

	        Book result = controller.getBook("102");
	        assertEquals("Spring Boot", result.getTitle());
	    }

	    @Test
	    void testAddBook() {
	        BookService mockService = mock(BookService.class);
	        BookController controller = new BookController();
	        controller.setBookService(mockService);

	        Book book = new Book();
	        book.setIsbn("103");
	        book.setTitle("Test Driven Dev");
	        book.setAuthor("Kent Beck");
	        book.setPublicationYear(2002);

	        when(mockService.addBook(book)).thenReturn(book);

	        Book result = controller.addBook(book);
	        assertEquals("103", result.getIsbn());
	    }

	    @Test
	    void testUpdateBook() {
	        BookService mockService = mock(BookService.class);
	        BookController controller = new BookController();
	        controller.setBookService(mockService);

	        Book book = new Book();
	        book.setIsbn("104");
	        book.setTitle("Updated Title");
	        book.setAuthor("Updated Author");
	        book.setPublicationYear(2024);

	        when(mockService.updateBook("104", book)).thenReturn(book);

	        Book result = controller.updateBook("104", book);
	        assertEquals("Updated Title", result.getTitle());
	    }

	    @Test
	    void testDeleteBook() {
	        BookService mockService = mock(BookService.class);
	        BookController controller = new BookController();
	        controller.setBookService(mockService);

	        // No need to mock void methods if we just want to verify
	        controller.deleteBook("105");
	        verify(mockService).deleteBook("105");
	    }
	}

	


    