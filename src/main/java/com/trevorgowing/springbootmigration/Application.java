package com.trevorgowing.springbootmigration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public Module jdk8() {
    return new Jdk8Module();
  }

  @Bean
  public Module javaTimeModule() {
    return new JavaTimeModule();
  }

  @Bean
  public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder objectMapperBuilder) {
    return objectMapperBuilder.dateFormat(new ISO8601DateFormat()).build();
  }
}
