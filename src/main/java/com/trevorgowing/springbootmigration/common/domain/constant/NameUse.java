package com.trevorgowing.springbootmigration.common.domain.constant;

import java.util.Arrays;

public enum NameUse {
  ANONYMOUS,
  MAIDEN,
  NICKNAME,
  OFFICIAL,
  OLD,
  TEMP,
  USUAL;

  public static final String SYSTEM = "http://hl7.org/fhir/name-use";

  public static NameUse from(String useString) {
    return Arrays.stream(values())
        .filter(use -> use.toString().equalsIgnoreCase(useString))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "Unexpected NameUse \'%s\', expected one of %s",
                        useString, Arrays.toString(values()))));
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
