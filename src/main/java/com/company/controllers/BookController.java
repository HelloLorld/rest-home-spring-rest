package com.company.controllers;

import com.company.entities.Book;
import com.company.exception.ResourceNotFound;
import com.company.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookRepository repository;

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return repository.findAll();
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<?> getBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> book = repository.findById(id);
        if (!book.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Book not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    @GetMapping("/names_of_books")
    public Map<String, Integer> getCostAndNames() {
        List<Book> books = getAllBooks();
        Map<String,Integer> bookMap = new HashMap<>();
        for(Book book : books)
            bookMap.put(book.getName(), book.getPrice());
        return bookMap;
    }

/*    @GetMapping("/book/{price}")
    public Map<String, Integer> getBooks(@PathVariable(value = "price") Integer price) {
        Map<String, Integer> books = repository.getBooksByPrice(price);
        return books;
    }*/

    @PostMapping("/books")
    public Book saveBook(@RequestBody Book book) {
        return repository.save(book);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable(value = "id") Integer id) {
        Optional<Book> book = repository.findById(id);
        if (!book.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Book not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else repository.delete(book.get());

        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> putOfBook(@PathVariable(value = "id") Integer id, @RequestBody Book bookChanged) {
        Optional<Book> book = repository.findById(id);
        if (!book.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Book not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            bookChanged.setId(book.get().getId());
            repository.save(bookChanged);
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("changed", true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/books/{id}")
    public ResponseEntity<?> patchOfBook(@PathVariable(value = "id") Integer id, @RequestBody Book bookChanged) {
        Optional<Book> book = repository.findById(id);
        boolean needChange = false;
        if (!book.isPresent()) return
                new ResponseEntity<>(new ResourceNotFound("Book not found for id: " + id).getMessage(), HttpStatus.NOT_FOUND);
        else {
            bookChanged.setId(book.get().getId());
            if (!book.get().getName().equals(bookChanged.getName())) {
                needChange = true;
            }
            if (book.get().getPrice() != bookChanged.getPrice()) {
                needChange = true;
            }
            if (!book.get().getWarehouse().equals(bookChanged.getWarehouse())) {
                needChange = true;
            }
            if (book.get().getQuantity() != bookChanged.getQuantity()) {
                needChange = true;
            }
            if (needChange) repository.save(bookChanged);
        }

        Map<String, Boolean> response = new HashMap<>();
        if (needChange) {
            response.put("changed", true);
        } else {
            response.put("changed", false);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
