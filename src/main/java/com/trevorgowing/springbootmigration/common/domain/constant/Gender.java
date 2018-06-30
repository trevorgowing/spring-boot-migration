package com.trevorgowing.springbootmigration.common.domain.constant;

import java.util.Arrays;

public enum Gender {
  FEMALE,
  MALE,
  OTHER,
  UNKNOWN;

  public static final String SYSTEM = "http://hl7.org/fhir/administrative-gender";

  public static Gender from(String genderString) {
    return Arrays.stream(values())
        .filter(gender -> gender.toString().equalsIgnoreCase(genderString))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "Unexpected Gender string \'%s\', expected one of %s",
                        genderString, Arrays.toString(values()))));
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
