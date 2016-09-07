package com.atos.askatos.repository;

import com.atos.askatos.domain.Questions;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by a630797 on 31/08/2016.
 */
@SuppressWarnings("unused")
public interface QuestionsRepository extends MongoRepository<Questions,String> {

}
