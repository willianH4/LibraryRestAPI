package com.williamhdz.library.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.williamhdz.library.entitie.Book;
import com.williamhdz.library.repository.BookRepository;
import com.williamhdz.library.repository.LibraryRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/book") //path url de entrada
public class BookController {
	
	private final Logger log = LoggerFactory.getLogger(BookController.class);
	
	@Autowired
	private BookRepository bookRepository;
	
	@Autowired
	private LibraryRepository libraryRepository;
	
	@GetMapping
	public ResponseEntity<Page<Book>> getAllBooks(Pageable pageable) {
		return ResponseEntity.ok(bookRepository.findAll(pageable));
	}
	
	@PostMapping
	public ResponseEntity<Book> saveBook(@Valid @RequestBody Book book) {
		// Forma corta
		if (book.getId() != null && book.getLibrary().getId() != null) {
	           log.warn("trying to create a book"); // logs
	           return ResponseEntity.badRequest().build();
	       }
	       Book result = bookRepository.save(book);
	       return ResponseEntity.ok(result);
	}
	
	@PutMapping
	 public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        if (book.getId() == null) { //Si no tiene id es una creacion
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }
        if (!bookRepository.existsById(book.getId())) {
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();
        }
        // actualizacion
        Book result = bookRepository.save(book);
        return ResponseEntity.ok(result);
    }

	@DeleteMapping("/{id}")
	public ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }
        bookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

	@GetMapping("/{id}")
    public ResponseEntity<Book> findBookById(@PathVariable Long id) {
		//buscar solo un libro por id
		Optional<Book> bookOptional = bookRepository.findById(id);

        // mediante Programacion Funcional
        return bookOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}