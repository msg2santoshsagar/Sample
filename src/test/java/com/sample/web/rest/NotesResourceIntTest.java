package com.sample.web.rest;

import com.sample.SampleApp;

import com.sample.domain.Notes;
import com.sample.repository.NotesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotesResource REST controller.
 *
 * @see NotesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApp.class)
public class NotesResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PLACE = "AAAAA";
    private static final String UPDATED_PLACE = "BBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    @Inject
    private NotesRepository notesRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNotesMockMvc;

    private Notes notes;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotesResource notesResource = new NotesResource();
        ReflectionTestUtils.setField(notesResource, "notesRepository", notesRepository);
        this.restNotesMockMvc = MockMvcBuilders.standaloneSetup(notesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notes createEntity(EntityManager em) {
        Notes notes = new Notes()
                .title(DEFAULT_TITLE)
                .date(DEFAULT_DATE)
                .place(DEFAULT_PLACE)
                .phoneNumber(DEFAULT_PHONE_NUMBER);
        return notes;
    }

    @Before
    public void initTest() {
        notes = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotes() throws Exception {
        int databaseSizeBeforeCreate = notesRepository.findAll().size();

        // Create the Notes

        restNotesMockMvc.perform(post("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notes)))
                .andExpect(status().isCreated());

        // Validate the Notes in the database
        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeCreate + 1);
        Notes testNotes = notes.get(notes.size() - 1);
        assertThat(testNotes.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testNotes.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testNotes.getPlace()).isEqualTo(DEFAULT_PLACE);
        assertThat(testNotes.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = notesRepository.findAll().size();
        // set the field null
        notes.setTitle(null);

        // Create the Notes, which fails.

        restNotesMockMvc.perform(post("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notes)))
                .andExpect(status().isBadRequest());

        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notesRepository.findAll().size();
        // set the field null
        notes.setDate(null);

        // Create the Notes, which fails.

        restNotesMockMvc.perform(post("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notes)))
                .andExpect(status().isBadRequest());

        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = notesRepository.findAll().size();
        // set the field null
        notes.setPlace(null);

        // Create the Notes, which fails.

        restNotesMockMvc.perform(post("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notes)))
                .andExpect(status().isBadRequest());

        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        notesRepository.saveAndFlush(notes);

        // Get all the notes
        restNotesMockMvc.perform(get("/api/notes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notes.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
                .andExpect(jsonPath("$.[*].place").value(hasItem(DEFAULT_PLACE.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getNotes() throws Exception {
        // Initialize the database
        notesRepository.saveAndFlush(notes);

        // Get the notes
        restNotesMockMvc.perform(get("/api/notes/{id}", notes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notes.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.place").value(DEFAULT_PLACE.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotes() throws Exception {
        // Get the notes
        restNotesMockMvc.perform(get("/api/notes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotes() throws Exception {
        // Initialize the database
        notesRepository.saveAndFlush(notes);
        int databaseSizeBeforeUpdate = notesRepository.findAll().size();

        // Update the notes
        Notes updatedNotes = notesRepository.findOne(notes.getId());
        updatedNotes
                .title(UPDATED_TITLE)
                .date(UPDATED_DATE)
                .place(UPDATED_PLACE)
                .phoneNumber(UPDATED_PHONE_NUMBER);

        restNotesMockMvc.perform(put("/api/notes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotes)))
                .andExpect(status().isOk());

        // Validate the Notes in the database
        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeUpdate);
        Notes testNotes = notes.get(notes.size() - 1);
        assertThat(testNotes.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testNotes.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNotes.getPlace()).isEqualTo(UPDATED_PLACE);
        assertThat(testNotes.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void deleteNotes() throws Exception {
        // Initialize the database
        notesRepository.saveAndFlush(notes);
        int databaseSizeBeforeDelete = notesRepository.findAll().size();

        // Get the notes
        restNotesMockMvc.perform(delete("/api/notes/{id}", notes.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Notes> notes = notesRepository.findAll();
        assertThat(notes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
