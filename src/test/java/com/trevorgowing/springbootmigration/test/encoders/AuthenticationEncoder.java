package com.trevorgowing.springbootmigration.test.encoders;

import java.util.Base64;

public class AuthenticationEncoder {

  public static String basic(String username, String password) {
    return String.format(
        "Basic %s",
        Base64.getEncoder().encodeToString(String.format("%s:%s", username, password).getBytes()));
  }
}
