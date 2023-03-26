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

import com.williamhdz.library.entitie.Library;
import com.williamhdz.library.repository.LibraryRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/library") //path url de entrada
public class LibraryController {
	
	private final Logger log = LoggerFactory.getLogger(LibraryController.class);
	
	@Autowired
	private LibraryRepository libraryRepository;
	
	@GetMapping
	public ResponseEntity<Page<Library>> listLibraries(Pageable pageable) {
		return ResponseEntity.ok(libraryRepository.findAll(pageable));
	}
	
	@PostMapping
	public ResponseEntity<Library> saveLibrary(@Valid @RequestBody Library library) {
		if (library.getId() != null) {
	           log.warn("trying to create a book"); // logs
	           return ResponseEntity.badRequest().build();
	       }
	       Library result = libraryRepository.save(library);
	       return ResponseEntity.ok(result);
	}
	
	@PutMapping
	 public ResponseEntity<Library> updateLibrary(@RequestBody Library library) {
        if (library.getId() == null) { //Si no tiene id es una creacion
            log.warn("Trying to update a non existent book");
            return ResponseEntity.badRequest().build();
        }
        if (!libraryRepository.existsById(library.getId())) {
            log.warn("Trying to update a non existent book");
            return ResponseEntity.notFound().build();
        }
        // actualizacion
        Library result = libraryRepository.save(library);
        return ResponseEntity.ok(result);
    }
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Library> delete(@PathVariable Long id) {
        if (!libraryRepository.existsById(id)) {
            log.warn("Trying to delete a non existent book");
            return ResponseEntity.notFound().build();
        }
        libraryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Library> findLibraryById(@PathVariable Long id) {
		//buscar solo una libreria o biblioteca por id
		Optional<Library> libraryOptional = libraryRepository.findById(id);

        // mediante Programacion Funcional
        return libraryOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

}
