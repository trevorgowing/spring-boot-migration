package com.trevorgowing.springbootmigration.patient;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.trevorgowing.springbootmigration.common.exception.ResourceNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SuppressWarnings("unused")
@RequestMapping("/api/patients")
class PatientController {

  private final PatientRepository patientRepository;

  @ResponseStatus(OK)
  @PreAuthorize("hasRole(\'USER\')")
  @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  List<Patient> get() {
    return patientRepository.findAll();
  }

  @ResponseStatus(OK)
  @PreAuthorize("hasRole(\'USER\')")
  @GetMapping(path = "/{id}", produces = APPLICATION_JSON_UTF8_VALUE)
  Patient get(@PathVariable String id) {
    return patientRepository
        .findById(id)
        .orElseThrow(
            () ->
                ResourceNotFoundException.causedBy(
                    String.format("Patient not found for id: \'%s\'", id)));
  }

  @ResponseStatus(CREATED)
  @PreAuthorize("hasRole(\'USER\')")
  @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  Patient post(@RequestBody Patient patient) {
    return patientRepository.save(patient);
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping(path = "/{id}")
  @PreAuthorize("hasRole(\'USER\')")
  void delete(@PathVariable String id) {
    patientRepository.deleteById(id);
  }
}
