package com.trevorgowing.springbootmigration.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

@TestComponent
@RequiredArgsConstructor
class PatientFinder {

  private final MongoOperations mongoOperations;

  Patient findByGivenAndFamilyName(String given, String family) {
    return mongoOperations.findOne(
        new Query()
            .addCriteria(Criteria.where("name.given").is(given).and("name.family").is(family)),
        Patient.class);
  }
}
