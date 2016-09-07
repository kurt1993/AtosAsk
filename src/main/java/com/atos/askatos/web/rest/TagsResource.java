package com.atos.askatos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.atos.askatos.domain.Tags;
import com.atos.askatos.repository.TagsRepository;
import com.atos.askatos.web.rest.util.HeaderUtil;
import com.atos.askatos.web.rest.util.PaginationUtil;
import com.atos.askatos.web.rest.dto.TagsDTO;
import com.atos.askatos.web.rest.mapper.TagsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Tags.
 */
@RestController
@RequestMapping("/api")
public class TagsResource {

    private final Logger log = LoggerFactory.getLogger(TagsResource.class);
        
    @Inject
    private TagsRepository tagsRepository;
    
    @Inject
    private TagsMapper tagsMapper;
    
    /**
     * POST  /tags : Create a new tags.
     *
     * @param tagsDTO the tagsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tagsDTO, or with status 400 (Bad Request) if the tags has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tags",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagsDTO> createTags(@Valid @RequestBody TagsDTO tagsDTO) throws URISyntaxException {
        log.debug("REST request to save Tags : {}", tagsDTO);
        if (tagsDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tags", "idexists", "A new tags cannot already have an ID")).body(null);
        }
        Tags tags = tagsMapper.tagsDTOToTags(tagsDTO);
        tags = tagsRepository.save(tags);
        TagsDTO result = tagsMapper.tagsToTagsDTO(tags);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tags", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tags : Updates an existing tags.
     *
     * @param tagsDTO the tagsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tagsDTO,
     * or with status 400 (Bad Request) if the tagsDTO is not valid,
     * or with status 500 (Internal Server Error) if the tagsDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tags",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagsDTO> updateTags(@Valid @RequestBody TagsDTO tagsDTO) throws URISyntaxException {
        log.debug("REST request to update Tags : {}", tagsDTO);
        if (tagsDTO.getId() == null) {
            return createTags(tagsDTO);
        }
        Tags tags = tagsMapper.tagsDTOToTags(tagsDTO);
        tags = tagsRepository.save(tags);
        TagsDTO result = tagsMapper.tagsToTagsDTO(tags);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tags", tagsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/tags",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TagsDTO>> getAllTags(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tags");
        Page<Tags> page = tagsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tags");
        return new ResponseEntity<>(tagsMapper.tagsToTagsDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /tags/:id : get the "id" tags.
     *
     * @param id the id of the tagsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tagsDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tags/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagsDTO> getTags(@PathVariable String id) {
        log.debug("REST request to get Tags : {}", id);
        Tags tags = tagsRepository.findOne(id);
        TagsDTO tagsDTO = tagsMapper.tagsToTagsDTO(tags);
        return Optional.ofNullable(tagsDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tags/:id : delete the "id" tags.
     *
     * @param id the id of the tagsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tags/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTags(@PathVariable String id) {
        log.debug("REST request to delete Tags : {}", id);
        tagsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tags", id.toString())).build();
    }

}
