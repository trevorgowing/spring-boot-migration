package com.trevorgowing.springbootmigration.test.types;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.trevorgowing.springbootmigration.test.encoders.JsonEncoder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;

@Import({JsonEncoder.class})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractSpringWebContextTests {

  @Autowired private MongoOperations mongoOperations;
  @Autowired private ReactiveWebApplicationContext webApplicationContext;

  @Autowired protected JsonEncoder jsonEncoder;

  protected WebTestClient restClient;

  @Before
  public void setup() {
    this.restClient =
        WebTestClient.bindToApplicationContext(webApplicationContext)
            .apply(SecurityMockServerConfigurers.springSecurity())
            .configureClient()
            .filter(ExchangeFilterFunctions.basicAuthentication())
            .build();
  }

  @After
  public void dropCollections() {
    mongoOperations.dropCollection("patients");
  }

  @Rule
  public TestRule watcher =
      new TestWatcher() {
        protected void starting(Description description) {
          System.out.println(description.getDisplayName());
        }
      };
}
