package com.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.sample.domain.Notes;

import com.sample.repository.NotesRepository;
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
 * REST controller for managing Notes.
 */
@RestController
@RequestMapping("/api")
public class NotesResource {

    private final Logger log = LoggerFactory.getLogger(NotesResource.class);
        
    @Inject
    private NotesRepository notesRepository;

    /**
     * POST  /notes : Create a new notes.
     *
     * @param notes the notes to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notes, or with status 400 (Bad Request) if the notes has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Notes> createNotes(@Valid @RequestBody Notes notes) throws URISyntaxException {
        log.debug("REST request to save Notes : {}", notes);
        if (notes.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notes", "idexists", "A new notes cannot already have an ID")).body(null);
        }
        Notes result = notesRepository.save(notes);
        return ResponseEntity.created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notes", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notes : Updates an existing notes.
     *
     * @param notes the notes to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notes,
     * or with status 400 (Bad Request) if the notes is not valid,
     * or with status 500 (Internal Server Error) if the notes couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Notes> updateNotes(@Valid @RequestBody Notes notes) throws URISyntaxException {
        log.debug("REST request to update Notes : {}", notes);
        if (notes.getId() == null) {
            return createNotes(notes);
        }
        Notes result = notesRepository.save(notes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notes", notes.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notes : get all the notes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notes in body
     */
    @RequestMapping(value = "/notes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Notes> getAllNotes() {
        log.debug("REST request to get all Notes");
        List<Notes> notes = notesRepository.findAll();
        return notes;
    }

    /**
     * GET  /notes/:id : get the "id" notes.
     *
     * @param id the id of the notes to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notes, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Notes> getNotes(@PathVariable Long id) {
        log.debug("REST request to get Notes : {}", id);
        Notes notes = notesRepository.findOne(id);
        return Optional.ofNullable(notes)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notes/:id : delete the "id" notes.
     *
     * @param id the id of the notes to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotes(@PathVariable Long id) {
        log.debug("REST request to delete Notes : {}", id);
        notesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notes", id.toString())).build();
    }

}
