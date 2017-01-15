package com.sample.repository;

import com.sample.domain.NoteCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NoteCategory entity.
 */
@SuppressWarnings("unused")
public interface NoteCategoryRepository extends JpaRepository<NoteCategory,Long> {

}
