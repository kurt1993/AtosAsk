package com.atos.askatos.repository;

import com.atos.askatos.domain.Ask;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Ask entity.
 */
@SuppressWarnings("unused")
public interface AskRepository extends MongoRepository<Ask,String> {

}
