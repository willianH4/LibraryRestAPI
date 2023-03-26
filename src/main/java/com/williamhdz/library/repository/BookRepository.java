package com.williamhdz.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhdz.library.entitie.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	
}
