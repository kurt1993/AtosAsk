package com.atos.askatos.repository;

import com.atos.askatos.domain.Tags;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Tags entity.
 */
@SuppressWarnings("unused")
public interface TagsRepository extends MongoRepository<Tags,String> {

}
