package com.atos.askatos.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.atos.askatos.domain.Ask;
import com.atos.askatos.repository.AskRepository;
import com.atos.askatos.web.rest.util.HeaderUtil;
import com.atos.askatos.web.rest.util.PaginationUtil;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ask.
 */
@RestController
@RequestMapping("/api")
public class AskResource {

    private final Logger log = LoggerFactory.getLogger(AskResource.class);
        
    @Inject
    private AskRepository askRepository;
    
    /**
     * POST  /asks : Create a new ask.
     *
     * @param ask the ask to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ask, or with status 400 (Bad Request) if the ask has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asks",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ask> createAsk(@Valid @RequestBody Ask ask) throws URISyntaxException {
        log.debug("REST request to save Ask : {}", ask);
        if (ask.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("ask", "idexists", "A new ask cannot already have an ID")).body(null);
        }
        Ask result = askRepository.save(ask);
        return ResponseEntity.created(new URI("/api/asks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("ask", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /asks : Updates an existing ask.
     *
     * @param ask the ask to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ask,
     * or with status 400 (Bad Request) if the ask is not valid,
     * or with status 500 (Internal Server Error) if the ask couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asks",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ask> updateAsk(@Valid @RequestBody Ask ask) throws URISyntaxException {
        log.debug("REST request to update Ask : {}", ask);
        if (ask.getId() == null) {
            return createAsk(ask);
        }
        Ask result = askRepository.save(ask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("ask", ask.getId().toString()))
            .body(result);
    }

    /**
     * GET  /asks : get all the asks.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of asks in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/asks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Ask>> getAllAsks(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Asks");
        Page<Ask> page = askRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/asks");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /asks/:id : get the "id" ask.
     *
     * @param id the id of the ask to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ask, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/asks/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Ask> getAsk(@PathVariable String id) {
        log.debug("REST request to get Ask : {}", id);
        Ask ask = askRepository.findOne(id);
        return Optional.ofNullable(ask)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /asks/:id : delete the "id" ask.
     *
     * @param id the id of the ask to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/asks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAsk(@PathVariable String id) {
        log.debug("REST request to delete Ask : {}", id);
        askRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("ask", id.toString())).build();
    }

}
