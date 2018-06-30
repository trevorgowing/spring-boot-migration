package com.trevorgowing.springbootmigration.common.domain.constant;

import java.util.Arrays;

public enum IdentifierUse {
  OFFICIAL,
  SECONDARY,
  TEMPORARY,
  USUAL;

  public static final String SYSTEM = "https://www.hl7.org/fhir/codesystem-identifier-use.html";

  public static IdentifierUse from(String useString) {
    return Arrays.stream(values())
        .filter(use -> use.toString().equalsIgnoreCase(useString))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    String.format(
                        "Unexpected IdentifierUse \'%s\', expected one of %s",
                        useString, Arrays.toString(values()))));
  }

  @Override
  public String toString() {
    return name().toLowerCase();
  }
}
