package com.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sample.domain.NoteCategory;

import com.sample.repository.NoteCategoryRepository;
import com.sample.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NoteCategory.
 */
@RestController
@RequestMapping("/api")
public class NoteCategoryResource {

    private final Logger log = LoggerFactory.getLogger(NoteCategoryResource.class);
        
    @Inject
    private NoteCategoryRepository noteCategoryRepository;

    /**
     * POST  /note-categories : Create a new noteCategory.
     *
     * @param noteCategory the noteCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new noteCategory, or with status 400 (Bad Request) if the noteCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/note-categories",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoteCategory> createNoteCategory(@Valid @RequestBody NoteCategory noteCategory) throws URISyntaxException {
        log.debug("REST request to save NoteCategory : {}", noteCategory);
        if (noteCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("noteCategory", "idexists", "A new noteCategory cannot already have an ID")).body(null);
        }
        NoteCategory result = noteCategoryRepository.save(noteCategory);
        return ResponseEntity.created(new URI("/api/note-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("noteCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /note-categories : Updates an existing noteCategory.
     *
     * @param noteCategory the noteCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated noteCategory,
     * or with status 400 (Bad Request) if the noteCategory is not valid,
     * or with status 500 (Internal Server Error) if the noteCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/note-categories",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoteCategory> updateNoteCategory(@Valid @RequestBody NoteCategory noteCategory) throws URISyntaxException {
        log.debug("REST request to update NoteCategory : {}", noteCategory);
        if (noteCategory.getId() == null) {
            return createNoteCategory(noteCategory);
        }
        NoteCategory result = noteCategoryRepository.save(noteCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("noteCategory", noteCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /note-categories : get all the noteCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of noteCategories in body
     */
    @RequestMapping(value = "/note-categories",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NoteCategory> getAllNoteCategories() {
        log.debug("REST request to get all NoteCategories");
        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();
        return noteCategories;
    }

    /**
     * GET  /note-categories/:id : get the "id" noteCategory.
     *
     * @param id the id of the noteCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the noteCategory, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/note-categories/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NoteCategory> getNoteCategory(@PathVariable Long id) {
        log.debug("REST request to get NoteCategory : {}", id);
        NoteCategory noteCategory = noteCategoryRepository.findOne(id);
        return Optional.ofNullable(noteCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /note-categories/:id : delete the "id" noteCategory.
     *
     * @param id the id of the noteCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/note-categories/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNoteCategory(@PathVariable Long id) {
        log.debug("REST request to delete NoteCategory : {}", id);
        noteCategoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("noteCategory", id.toString())).build();
    }

}
