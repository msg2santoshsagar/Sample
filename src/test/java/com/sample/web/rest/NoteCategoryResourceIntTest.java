package com.sample.web.rest;

import com.sample.SampleApp;

import com.sample.domain.NoteCategory;
import com.sample.repository.NoteCategoryRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.sample.domain.enumeration.Language;
/**
 * Test class for the NoteCategoryResource REST controller.
 *
 * @see NoteCategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleApp.class)
public class NoteCategoryResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAA";
    private static final String UPDATED_CATEGORY = "BBBBB";

    private static final Language DEFAULT_LANGUAGE = Language.HINDI;
    private static final Language UPDATED_LANGUAGE = Language.ENGLISH;

    @Inject
    private NoteCategoryRepository noteCategoryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restNoteCategoryMockMvc;

    private NoteCategory noteCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NoteCategoryResource noteCategoryResource = new NoteCategoryResource();
        ReflectionTestUtils.setField(noteCategoryResource, "noteCategoryRepository", noteCategoryRepository);
        this.restNoteCategoryMockMvc = MockMvcBuilders.standaloneSetup(noteCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NoteCategory createEntity(EntityManager em) {
        NoteCategory noteCategory = new NoteCategory()
                .category(DEFAULT_CATEGORY)
                .language(DEFAULT_LANGUAGE);
        return noteCategory;
    }

    @Before
    public void initTest() {
        noteCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createNoteCategory() throws Exception {
        int databaseSizeBeforeCreate = noteCategoryRepository.findAll().size();

        // Create the NoteCategory

        restNoteCategoryMockMvc.perform(post("/api/note-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noteCategory)))
                .andExpect(status().isCreated());

        // Validate the NoteCategory in the database
        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();
        assertThat(noteCategories).hasSize(databaseSizeBeforeCreate + 1);
        NoteCategory testNoteCategory = noteCategories.get(noteCategories.size() - 1);
        assertThat(testNoteCategory.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testNoteCategory.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = noteCategoryRepository.findAll().size();
        // set the field null
        noteCategory.setCategory(null);

        // Create the NoteCategory, which fails.

        restNoteCategoryMockMvc.perform(post("/api/note-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(noteCategory)))
                .andExpect(status().isBadRequest());

        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();
        assertThat(noteCategories).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNoteCategories() throws Exception {
        // Initialize the database
        noteCategoryRepository.saveAndFlush(noteCategory);

        // Get all the noteCategories
        restNoteCategoryMockMvc.perform(get("/api/note-categories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(noteCategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
                .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())));
    }

    @Test
    @Transactional
    public void getNoteCategory() throws Exception {
        // Initialize the database
        noteCategoryRepository.saveAndFlush(noteCategory);

        // Get the noteCategory
        restNoteCategoryMockMvc.perform(get("/api/note-categories/{id}", noteCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(noteCategory.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNoteCategory() throws Exception {
        // Get the noteCategory
        restNoteCategoryMockMvc.perform(get("/api/note-categories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNoteCategory() throws Exception {
        // Initialize the database
        noteCategoryRepository.saveAndFlush(noteCategory);
        int databaseSizeBeforeUpdate = noteCategoryRepository.findAll().size();

        // Update the noteCategory
        NoteCategory updatedNoteCategory = noteCategoryRepository.findOne(noteCategory.getId());
        updatedNoteCategory
                .category(UPDATED_CATEGORY)
                .language(UPDATED_LANGUAGE);

        restNoteCategoryMockMvc.perform(put("/api/note-categories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNoteCategory)))
                .andExpect(status().isOk());

        // Validate the NoteCategory in the database
        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();
        assertThat(noteCategories).hasSize(databaseSizeBeforeUpdate);
        NoteCategory testNoteCategory = noteCategories.get(noteCategories.size() - 1);
        assertThat(testNoteCategory.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testNoteCategory.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void deleteNoteCategory() throws Exception {
        // Initialize the database
        noteCategoryRepository.saveAndFlush(noteCategory);
        int databaseSizeBeforeDelete = noteCategoryRepository.findAll().size();

        // Get the noteCategory
        restNoteCategoryMockMvc.perform(delete("/api/note-categories/{id}", noteCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<NoteCategory> noteCategories = noteCategoryRepository.findAll();
        assertThat(noteCategories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
