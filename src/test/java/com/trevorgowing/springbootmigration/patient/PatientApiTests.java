package com.trevorgowing.springbootmigration.patient;

import static com.trevorgowing.springbootmigration.common.domain.constant.Gender.FEMALE;
import static com.trevorgowing.springbootmigration.common.domain.constant.Gender.MALE;
import static com.trevorgowing.springbootmigration.common.domain.constant.IdentifierUse.OFFICIAL;
import static com.trevorgowing.springbootmigration.common.domain.constant.NameUse.USUAL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import com.trevorgowing.springbootmigration.common.domain.persistence.HumanName;
import com.trevorgowing.springbootmigration.common.domain.persistence.Identifier;
import com.trevorgowing.springbootmigration.common.exception.ExceptionResponse;
import com.trevorgowing.springbootmigration.test.encoders.AuthenticationEncoder;
import com.trevorgowing.springbootmigration.test.types.AbstractSpringWebContextTests;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

@Import({PatientFinder.class, PatientPersister.class})
public class PatientApiTests extends AbstractSpringWebContextTests {

  @Autowired private PatientFinder finder;
  @Autowired private PatientPersister persister;

  @Test
  public void testGetPatientsWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .get()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void testGetPatientsWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .get()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void testGetPatientsWithNoExistingPatients_shouldResponseWithStatusOkAndAnEmptyArray() {
    restClient
        .get()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(jsonEncoder.encodeToJsonString(Collections.emptyList()));
  }

  @Test
  public void testGetPatientsWithExistingPatients_shouldResponseWithStatusOkAndPatientArray() {
    Patient patientOne =
        Patient.builder()
            .active(true)
            .gender(FEMALE)
            .birthDate(LocalDate.now().minusYears(30))
            .name(HumanName.builder().given("one").family("patient").use(USUAL).build())
            .identifiers(
                Collections.singleton(
                    Identifier.builder().system("system").value("one").use(OFFICIAL).build()))
            .build();
    persister.persist(patientOne);

    Patient patientTwo =
        Patient.builder()
            .active(true)
            .gender(MALE)
            .birthDate(LocalDate.now().minusYears(40))
            .name(HumanName.builder().given("two").family("patient").use(USUAL).build())
            .identifiers(
                Collections.singleton(
                    Identifier.builder().system("system").value("two").use(OFFICIAL).build()))
            .build();
    persister.persist(patientTwo);

    restClient
        .get()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(jsonEncoder.encodeToJsonString(Arrays.asList(patientOne, patientTwo)));
  }

  @Test
  public void testGetPatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .get()
        .uri("/api/patients/1")
        .accept(APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void testGetPatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .get()
        .uri("/api/patients/1")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void
      testGetPatientWithNoMatchingPatient_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    ExceptionResponse exceptionResponse =
        ExceptionResponse.builder()
            .status(NOT_FOUND.value())
            .error(NOT_FOUND.getReasonPhrase())
            .message("Patient not found for id: \'1\'")
            .build();

    restClient
        .get()
        .uri("/api/patients/1")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .json(jsonEncoder.encodeToJsonString(exceptionResponse));
  }

  @Test
  public void testGetPatientWithMatchingPatient_shouldResponseWithStatusOkAndPatient() {
    Patient patient =
        Patient.builder()
            .active(true)
            .gender(FEMALE)
            .birthDate(LocalDate.now().minusYears(30))
            .name(HumanName.builder().given("given").family("family").use(USUAL).build())
            .identifiers(
                Collections.singleton(
                    Identifier.builder().system("system").value("one").use(OFFICIAL).build()))
            .build();
    persister.persist(patient);

    restClient
        .get()
        .uri("/api/patients/" + patient.getId())
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .accept(APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .json(jsonEncoder.encodeToJsonString(patient));
  }

  @Test
  public void testPostPatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .post()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .contentType(APPLICATION_JSON_UTF8)
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void testPostPatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .post()
        .uri("/api/patients")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .syncBody(Patient.builder().build())
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void testPostPatientWithValidPatient_shouldResponseWithStatusCreatedAndPatient() {
    Patient patient =
        Patient.builder()
            .active(true)
            .gender(FEMALE)
            .birthDate(LocalDate.now().minusYears(30))
            .name(HumanName.builder().given("given").family("family").use(USUAL).build())
            .identifiers(
                Collections.singleton(
                    Identifier.builder().system("system").value("one").use(OFFICIAL).build()))
            .build();

    ResponseSpec response =
        restClient
            .post()
            .uri("/api/patients")
            .accept(APPLICATION_JSON_UTF8)
            .contentType(APPLICATION_JSON_UTF8)
            .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
            .syncBody(patient)
            .exchange();

    Patient persistedPatient = finder.findByGivenAndFamilyName("given", "family");

    response
        .expectStatus()
        .isCreated()
        .expectBody()
        .json(jsonEncoder.encodeToJsonString(persistedPatient));
  }

  @Test
  public void testDeletePatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    restClient.delete().uri("/api/patients/1").exchange().expectStatus().isUnauthorized();
  }

  @Test
  public void testDeletePatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    restClient
        .delete()
        .uri("/api/patients/1")
        .accept(APPLICATION_JSON_UTF8)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .exchange()
        .expectStatus()
        .isUnauthorized();
  }

  @Test
  public void
      testDeletePatientWithNoMatchingPatient_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    restClient
        .delete()
        .uri("/api/patients/1")
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();
  }

  @Test
  public void testDeletePatientWithMatchingPatient_shouldResponseWithStatusOkAndPatient() {
    Patient patient =
        Patient.builder()
            .active(true)
            .gender(FEMALE)
            .birthDate(LocalDate.now().minusYears(30))
            .name(HumanName.builder().given("one").family("patient").use(USUAL).build())
            .identifiers(
                Collections.singleton(
                    Identifier.builder().system("system").value("one").use(OFFICIAL).build()))
            .build();
    persister.persist(patient);

    restClient
        .delete()
        .uri("/api/patients/" + patient.getId())
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .exchange()
        .expectStatus()
        .isNoContent()
        .expectBody()
        .isEmpty();
  }
}
