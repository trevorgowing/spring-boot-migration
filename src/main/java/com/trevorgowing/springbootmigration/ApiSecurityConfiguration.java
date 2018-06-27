package com.trevorgowing.springbootmigration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Order(2)
@Configuration
class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final String username;
  private final String password;

  ApiSecurityConfiguration(
      @Value("${security.user.name}") String username,
      @Value("${security.user.password}") String password) {
    this.username = username;
    this.password = password;
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
    entryPoint.setRealmName("Spring Boot Migration");
    return entryPoint;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .antMatcher("/api/**")
        .authorizeRequests()
        .anyRequest()
        .hasRole("USER")
        .and()
        .httpBasic()
        .authenticationEntryPoint(authenticationEntryPoint());
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .passwordEncoder(new BCryptPasswordEncoder())
        .withUser(username)
        .password(password)
        .roles("USER");
  }
}
