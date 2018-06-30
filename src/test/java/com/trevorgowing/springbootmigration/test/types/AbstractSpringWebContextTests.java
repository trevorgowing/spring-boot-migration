package com.trevorgowing.springbootmigration.test.types;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.trevorgowing.springbootmigration.test.encoders.JsonEncoder;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Import({JsonEncoder.class})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class AbstractSpringWebContextTests {

  @Autowired private MongoOperations mongoOperations;
  @Autowired private WebApplicationContext webApplicationContext;

  @Autowired protected JsonEncoder jsonEncoder;

  @Before
  public void initialiseRestAssuredMockMvc() {
    MockMvc springMockMvc =
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    RestAssuredMockMvc.mockMvc(springMockMvc);
  }

  @After
  public void clearPatients() {
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
