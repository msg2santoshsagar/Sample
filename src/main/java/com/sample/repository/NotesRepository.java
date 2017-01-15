package com.sample.repository;

import com.sample.domain.Notes;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Notes entity.
 */
@SuppressWarnings("unused")
public interface NotesRepository extends JpaRepository<Notes,Long> {

}
