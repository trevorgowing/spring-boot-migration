package com.trevorgowing.springbootmigration.patient;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface PatientRepository extends ReactiveMongoRepository<Patient, String> {}
