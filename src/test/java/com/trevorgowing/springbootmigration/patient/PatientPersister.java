package com.trevorgowing.springbootmigration.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.data.mongodb.core.MongoOperations;

@TestComponent
@RequiredArgsConstructor
class PatientPersister {

  private final MongoOperations mongoOperations;

  void persist(Patient patient) {
    mongoOperations.save(patient);
  }
}
