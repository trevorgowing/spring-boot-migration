package com.trevorgowing.springbootmigration.patient;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.springbootmigration.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
@RequestMapping("/api/patients")
class PatientController {

  private final PatientRepository patientRepository;

  @Secured("USER")
  @ResponseStatus(OK)
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  Flux<Patient> get() {
    return patientRepository.findAll();
  }

  @Secured("USER")
  @ResponseStatus(OK)
  @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
  Mono<Patient> get(@PathVariable String id) {
    return patientRepository
        .findById(id)
        .switchIfEmpty(
            Mono.error(
                ResourceNotFoundException.causedBy(
                    String.format("Patient not found for id: \'%s\'", id))));
  }

  @Secured("USER")
  @ResponseStatus(CREATED)
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  Mono<Patient> post(@RequestBody Patient patient) {
    return patientRepository.save(patient);
  }

  @Secured("USER")
  @ResponseStatus(NO_CONTENT)
  @DeleteMapping(path = "/{id}")
  Mono<Void> delete(@PathVariable String id) {
    return patientRepository.deleteById(id);
  }
}
