package com.atos.askatos.web.rest;

import com.atos.askatos.AskAtosApp;
import com.atos.askatos.domain.Ask;
import com.atos.askatos.repository.AskRepository;

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
 * Test class for the AskResource REST controller.
 *
 * @see AskResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AskAtosApp.class)
@WebAppConfiguration
@IntegrationTest
public class AskResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_CONTENT = "AAAAA";
    private static final String UPDATED_CONTENT = "BBBBB";
    private static final String DEFAULT_TAGS = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private AskRepository askRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAskMockMvc;

    private Ask ask;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AskResource askResource = new AskResource();
        ReflectionTestUtils.setField(askResource, "askRepository", askRepository);
        this.restAskMockMvc = MockMvcBuilders.standaloneSetup(askResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        askRepository.deleteAll();
        ask = new Ask();
        ask.setTitle(DEFAULT_TITLE);
        ask.setContent(DEFAULT_CONTENT);
        ask.setTags(DEFAULT_TAGS);
    }

    @Test
    public void createAsk() throws Exception {
        int databaseSizeBeforeCreate = askRepository.findAll().size();

        // Create the Ask

        restAskMockMvc.perform(post("/api/asks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ask)))
                .andExpect(status().isCreated());

        // Validate the Ask in the database
        List<Ask> asks = askRepository.findAll();
        assertThat(asks).hasSize(databaseSizeBeforeCreate + 1);
        Ask testAsk = asks.get(asks.size() - 1);
        assertThat(testAsk.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAsk.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAsk.getTags()).isEqualTo(DEFAULT_TAGS);
    }

    @Test
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = askRepository.findAll().size();
        // set the field null
        ask.setTitle(null);

        // Create the Ask, which fails.

        restAskMockMvc.perform(post("/api/asks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ask)))
                .andExpect(status().isBadRequest());

        List<Ask> asks = askRepository.findAll();
        assertThat(asks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkTagsIsRequired() throws Exception {
        int databaseSizeBeforeTest = askRepository.findAll().size();
        // set the field null
        ask.setTags(null);

        // Create the Ask, which fails.

        restAskMockMvc.perform(post("/api/asks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ask)))
                .andExpect(status().isBadRequest());

        List<Ask> asks = askRepository.findAll();
        assertThat(asks).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllAsks() throws Exception {
        // Initialize the database
        askRepository.save(ask);

        // Get all the asks
        restAskMockMvc.perform(get("/api/asks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(ask.getId())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())));
    }

    @Test
    public void getAsk() throws Exception {
        // Initialize the database
        askRepository.save(ask);

        // Get the ask
        restAskMockMvc.perform(get("/api/asks/{id}", ask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(ask.getId()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()));
    }

    @Test
    public void getNonExistingAsk() throws Exception {
        // Get the ask
        restAskMockMvc.perform(get("/api/asks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAsk() throws Exception {
        // Initialize the database
        askRepository.save(ask);
        int databaseSizeBeforeUpdate = askRepository.findAll().size();

        // Update the ask
        Ask updatedAsk = new Ask();
        updatedAsk.setId(ask.getId());
        updatedAsk.setTitle(UPDATED_TITLE);
        updatedAsk.setContent(UPDATED_CONTENT);
        updatedAsk.setTags(UPDATED_TAGS);

        restAskMockMvc.perform(put("/api/asks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAsk)))
                .andExpect(status().isOk());

        // Validate the Ask in the database
        List<Ask> asks = askRepository.findAll();
        assertThat(asks).hasSize(databaseSizeBeforeUpdate);
        Ask testAsk = asks.get(asks.size() - 1);
        assertThat(testAsk.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAsk.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAsk.getTags()).isEqualTo(UPDATED_TAGS);
    }

    @Test
    public void deleteAsk() throws Exception {
        // Initialize the database
        askRepository.save(ask);
        int databaseSizeBeforeDelete = askRepository.findAll().size();

        // Get the ask
        restAskMockMvc.perform(delete("/api/asks/{id}", ask.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Ask> asks = askRepository.findAll();
        assertThat(asks).hasSize(databaseSizeBeforeDelete - 1);
    }
}
