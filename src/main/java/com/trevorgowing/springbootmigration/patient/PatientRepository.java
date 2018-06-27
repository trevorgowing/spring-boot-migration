package com.trevorgowing.springbootmigration.patient;

import org.springframework.data.mongodb.repository.MongoRepository;

interface PatientRepository extends MongoRepository<Patient, String> {}
