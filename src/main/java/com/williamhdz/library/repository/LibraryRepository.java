package com.williamhdz.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.williamhdz.library.entitie.Library;

public interface LibraryRepository extends JpaRepository<Library, Long> {
	
}
