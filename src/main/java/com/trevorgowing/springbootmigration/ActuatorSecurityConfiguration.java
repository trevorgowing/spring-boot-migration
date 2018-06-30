package com.trevorgowing.springbootmigration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.AuthenticationEntryPoint;

@Order(1)
@Configuration
class ActuatorSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final String actuatorUser;
  private final String actuatorPassword;
  private final AuthenticationEntryPoint authenticationEntryPoint;

  ActuatorSecurityConfiguration(
      @Value("${migration.actuator.user}") String actuatorUser,
      @Value("${migration.actuator.password}") String actuatorPassword,
      AuthenticationEntryPoint authenticationEntryPoint) {
    this.actuatorUser = actuatorUser;
    this.actuatorPassword = actuatorPassword;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .antMatcher("/actuator/**")
        .authorizeRequests()
        .antMatchers("/actuator/health")
        .permitAll()
        .anyRequest()
        .hasRole("ACTUATOR")
        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .passwordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder())
        .withUser(actuatorUser)
        .password(actuatorPassword)
        .roles("ACTUATOR");
  }
}
