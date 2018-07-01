package com.trevorgowing.springbootmigration.patient;

import static com.trevorgowing.springbootmigration.common.domain.constant.Gender.FEMALE;
import static com.trevorgowing.springbootmigration.common.domain.constant.Gender.MALE;
import static com.trevorgowing.springbootmigration.common.domain.constant.IdentifierUse.OFFICIAL;
import static com.trevorgowing.springbootmigration.common.domain.constant.NameUse.USUAL;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.trevorgowing.springbootmigration.common.domain.persistence.HumanName;
import com.trevorgowing.springbootmigration.common.domain.persistence.Identifier;
import com.trevorgowing.springbootmigration.common.exception.ExceptionResponse;
import com.trevorgowing.springbootmigration.test.encoders.AuthenticationEncoder;
import com.trevorgowing.springbootmigration.test.types.AbstractSpringWebContextTests;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({PatientFinder.class, PatientPersister.class})
public class PatientApiTests extends AbstractSpringWebContextTests {

  @Autowired private PatientFinder finder;
  @Autowired private PatientPersister persister;

  @Test
  public void testGetPatientsWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .get("/api/patients")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void testGetPatientsWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .get("/api/patients")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void testGetPatientsWithNoExistingPatients_shouldResponseWithStatusOkAndAnEmptyArray() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .get("/api/patients")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(OK.value())
        .body(is(jsonEncoder.encodeToJsonString(Collections.emptyList())));
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

    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .get("/api/patients")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(OK.value())
        .body(is(jsonEncoder.encodeToJsonString(Arrays.asList(patientOne, patientTwo))));
  }

  @Test
  public void testGetPatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void testGetPatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
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

    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NOT_FOUND.value())
        .body(is(jsonEncoder.encodeToJsonString(exceptionResponse)));
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

    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .get("/api/patients/" + patient.getId())
        .then()
        .log()
        .ifValidationFails()
        .statusCode(OK.value())
        .body(is(jsonEncoder.encodeToJsonString(patient)));
  }

  @Test
  public void testPostPatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void testPostPatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
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

    MockMvcResponse response =
        given()
            .accept(JSON)
            .contentType(JSON)
            .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
            .body(jsonEncoder.encodeToJsonString(patient))
            .post("/api/patients");

    Patient persistedPatient = finder.findByGivenAndFamilyName("given", "family");

    response
        .then()
        .log()
        .all()
        .statusCode(CREATED.value())
        .body(is(jsonEncoder.encodeToJsonString(persistedPatient)));
  }

  @Test
  public void testDeletePatientWithNoCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void testDeletePatientWithInvalidCredentials_shouldRespondWithStatusUnauthorized() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("invalid", "invalid"))
        .get("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(UNAUTHORIZED.value());
  }

  @Test
  public void
      testDeletePatientWithNoMatchingPatient_shouldResponseWithStatusNotFoundAndExceptionResponse() {
    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .delete("/api/patients/1")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NO_CONTENT.value());
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

    given()
        .accept(JSON)
        .header(AUTHORIZATION, AuthenticationEncoder.basic("test", "test"))
        .delete("/api/patients/" + patient.getId())
        .then()
        .log()
        .ifValidationFails()
        .statusCode(NO_CONTENT.value());
  }
}
