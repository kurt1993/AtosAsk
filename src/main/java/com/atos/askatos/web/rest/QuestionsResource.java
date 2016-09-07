package com.atos.askatos.web.rest;

import com.atos.askatos.domain.Questions;
import com.atos.askatos.web.rest.util.HeaderUtil;
import com.atos.askatos.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.atos.askatos.repository.QuestionsRepository;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by a630797 on 31/08/2016.
 */

@RestController
@RequestMapping("/api")
public class QuestionsResource {

    private final Logger log = LoggerFactory.getLogger(QuestionsResource.class);

    @Inject
    private QuestionsRepository questionsRepository;

    /** UPDATE **/

//    @RequestMapping(value = "/questions",
//        method = RequestMethod.PUT,
//        produces = MediaType.APPLICATION_JSON_VALUE)
//    @Timed
//    public ResponseEntity<Questions> updateQuestionsResponseEntity(@RequestBody Questions questions) throws URISyntaxException{
//        log.debug("REST request to update Questions : {}", questions);
//        if (questions.getId() == null){
//            return createQuestions(questions);
//        }
//        Questions result = questionsRepository.save(questions);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert("questions", questions.getId().toString()))
//            .body(result);
//    }

                                /** Pagination**/

    @RequestMapping(value = "/questions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Questions>> getAllQuestions(Pageable pageable)
        throws URISyntaxException{
        log.debug("REST request to get a page of Questions");
        Page<Questions> page = questionsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

                                /** READ**/

    @RequestMapping(value = "/questions/{id}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Questions> getQuestions(@PathVariable String id){
        log.debug("REST request to get Questions : {}", id);
        Questions questions = questionsRepository.findOne(id);
        return Optional.ofNullable(questions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
