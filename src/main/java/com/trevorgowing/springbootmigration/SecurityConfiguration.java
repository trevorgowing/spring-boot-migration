package com.trevorgowing.springbootmigration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration {

  private final String apiUsername;
  private final String apiPassword;
  private final String actuatorUser;
  private final String actuatorPassword;

  SecurityConfiguration(
      @Value("${spring.security.user.name}") String apiUsername,
      @Value("${spring.security.user.password}") String apiPassword,
      @Value("${migration.actuator.user}") String actuatorUser,
      @Value("${migration.actuator.password}") String actuatorPassword) {
    this.apiUsername = apiUsername;
    this.apiPassword = apiPassword;
    this.actuatorUser = actuatorUser;
    this.actuatorPassword = actuatorPassword;
  }

  @Bean
  @Order(2)
  public SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) {
    return http.securityMatcher(EndpointRequest.toAnyEndpoint())
        .csrf()
        .disable()
        .authorizeExchange()
        .matchers(EndpointRequest.to(HealthEndpoint.class))
        .permitAll()
        .anyExchange()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(basicAuthenticationEntryPoint())
        .and()
        .build();
  }

  @Bean
  @Order(1)
  public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
    return http.securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
        .csrf()
        .disable()
        .authorizeExchange()
        .anyExchange()
        .authenticated()
        .and()
        .httpBasic()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(basicAuthenticationEntryPoint())
        .and()
        .build();
  }

  @Bean
  public ServerAuthenticationEntryPoint basicAuthenticationEntryPoint() {
    HttpBasicServerAuthenticationEntryPoint basicAuthenticationEntryPoint =
        new HttpBasicServerAuthenticationEntryPoint();
    basicAuthenticationEntryPoint.setRealm("Spring Boot Migration");
    return basicAuthenticationEntryPoint;
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
    PasswordEncoder delegatingPasswordEncoder =
        PasswordEncoderFactories.createDelegatingPasswordEncoder();

    userDetailsManager.createUser(
        User.withUsername(apiUsername)
            .password(apiPassword)
            .roles("USER")
            .passwordEncoder(delegatingPasswordEncoder::encode)
            .build());
    userDetailsManager.createUser(
        User.withUsername(actuatorUser)
            .password(actuatorPassword)
            .roles("ACTUATOR")
            .passwordEncoder(delegatingPasswordEncoder::encode)
            .build());

    return userDetailsManager;
  }
}
