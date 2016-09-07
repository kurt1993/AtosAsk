package com.atos.askatos.web.rest;

import com.atos.askatos.AskAtosApp;
import com.atos.askatos.domain.Tags;
import com.atos.askatos.repository.TagsRepository;
import com.atos.askatos.web.rest.dto.TagsDTO;
import com.atos.askatos.web.rest.mapper.TagsMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TagsResource REST controller.
 *
 * @see TagsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AskAtosApp.class)
@WebAppConfiguration
@IntegrationTest
public class TagsResourceIntTest {

    private static final String DEFAULT_LIBELE = "AAAAA";
    private static final String UPDATED_LIBELE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_DOMAINE = "AAAAA";
    private static final String UPDATED_DOMAINE = "BBBBB";

    @Inject
    private TagsRepository tagsRepository;

    @Inject
    private TagsMapper tagsMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTagsMockMvc;

    private Tags tags;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagsResource tagsResource = new TagsResource();
        ReflectionTestUtils.setField(tagsResource, "tagsRepository", tagsRepository);
        ReflectionTestUtils.setField(tagsResource, "tagsMapper", tagsMapper);
        this.restTagsMockMvc = MockMvcBuilders.standaloneSetup(tagsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tagsRepository.deleteAll();
        tags = new Tags();
        tags.setLibele(DEFAULT_LIBELE);
        tags.setDescription(DEFAULT_DESCRIPTION);
        tags.setDomaine(DEFAULT_DOMAINE);
    }

    @Test
    public void createTags() throws Exception {
        int databaseSizeBeforeCreate = tagsRepository.findAll().size();

        // Create the Tags
        TagsDTO tagsDTO = tagsMapper.tagsToTagsDTO(tags);

        restTagsMockMvc.perform(post("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
                .andExpect(status().isCreated());

        // Validate the Tags in the database
        List<Tags> tags = tagsRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeCreate + 1);
        Tags testTags = tags.get(tags.size() - 1);
        assertThat(testTags.getLibele()).isEqualTo(DEFAULT_LIBELE);
        assertThat(testTags.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTags.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
    }

    @Test
    public void checkLibeleIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagsRepository.findAll().size();
        // set the field null
        tags.setLibele(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.tagsToTagsDTO(tags);

        restTagsMockMvc.perform(post("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
                .andExpect(status().isBadRequest());

        List<Tags> tags = tagsRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagsRepository.findAll().size();
        // set the field null
        tags.setDescription(null);

        // Create the Tags, which fails.
        TagsDTO tagsDTO = tagsMapper.tagsToTagsDTO(tags);

        restTagsMockMvc.perform(post("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
                .andExpect(status().isBadRequest());

        List<Tags> tags = tagsRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllTags() throws Exception {
        // Initialize the database
        tagsRepository.save(tags);

        // Get all the tags
        restTagsMockMvc.perform(get("/api/tags?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tags.getId())))
                .andExpect(jsonPath("$.[*].libele").value(hasItem(DEFAULT_LIBELE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE.toString())));
    }

    @Test
    public void getTags() throws Exception {
        // Initialize the database
        tagsRepository.save(tags);

        // Get the tags
        restTagsMockMvc.perform(get("/api/tags/{id}", tags.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tags.getId()))
            .andExpect(jsonPath("$.libele").value(DEFAULT_LIBELE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE.toString()));
    }

    @Test
    public void getNonExistingTags() throws Exception {
        // Get the tags
        restTagsMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTags() throws Exception {
        // Initialize the database
        tagsRepository.save(tags);
        int databaseSizeBeforeUpdate = tagsRepository.findAll().size();

        // Update the tags
        Tags updatedTags = new Tags();
        updatedTags.setId(tags.getId());
        updatedTags.setLibele(UPDATED_LIBELE);
        updatedTags.setDescription(UPDATED_DESCRIPTION);
        updatedTags.setDomaine(UPDATED_DOMAINE);
        TagsDTO tagsDTO = tagsMapper.tagsToTagsDTO(updatedTags);

        restTagsMockMvc.perform(put("/api/tags")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagsDTO)))
                .andExpect(status().isOk());

        // Validate the Tags in the database
        List<Tags> tags = tagsRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeUpdate);
        Tags testTags = tags.get(tags.size() - 1);
        assertThat(testTags.getLibele()).isEqualTo(UPDATED_LIBELE);
        assertThat(testTags.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTags.getDomaine()).isEqualTo(UPDATED_DOMAINE);
    }

    @Test
    public void deleteTags() throws Exception {
        // Initialize the database
        tagsRepository.save(tags);
        int databaseSizeBeforeDelete = tagsRepository.findAll().size();

        // Get the tags
        restTagsMockMvc.perform(delete("/api/tags/{id}", tags.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tags> tags = tagsRepository.findAll();
        assertThat(tags).hasSize(databaseSizeBeforeDelete - 1);
    }
}
