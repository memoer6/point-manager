package com.memoer6.pointTracker.tests;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.Assertions.*;	

import org.springframework.http.*;

import com.memoer6.pointTracker.model.User;

/*

//If you need to start a full running server for tests, we recommend that you use random ports. If you use 
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) an available port will be picked at random each time
//your test runs.
//The @LocalServerPort annotation can be used to inject the actual port used into your test. For convenience, tests
//that need to make REST calls to the started server can additionally @Autowire a TestRestTemplate which will resolve
//relative links to the running server.
//The RestTemplate is the central Spring class for client-side HTTP access
//TestRestTemplate is a convenience alternative to Spring’s RestTemplate that is useful in integration tests.
//You can get a vanilla template or one that sends Basic HTTP authentication (with a username and password).
//In either case the template will behave in a test-friendly way: not following redirects (so you can assert
//the response location), ignoring cookies (so the template is stateless), and not throwing exceptions on 
//server-side errors.

//@RunWith(SpringRunner.class) tells JUnit to run using Spring’s testing support. SpringRunner is the new name
//for SpringJUnit4ClassRunner, it’s just a bit easier on the eye.

@RunWith(SpringRunner.class)

//@SpringBootTest is saying “bootstrap with Spring Boot’s support” (e.g. load application.properties and
//give me all the Spring Boot goodness)
//The webEnvironment attribute allows specific “web environments” to be configured for the test. You can
//start tests with a MOCK servlet environment or with a real HTTP server running on either a RANDOM_PORT
//or a DEFINED_PORT.
//If we want to load a specific configuration, we can use the classes attribute of @SpringBootTest. In this
//example, we’ve omitted classes which means that the test will first attempt to load @Configuration from
//any inner-classes, and if that fails, it will search for your primary @SpringBootApplication class.

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)

//By design, JUnit does not specify the execution order of test method invocations. Until now, the methods were simply
//invoked in the order returned by the reflection API.
// Sorts the test methods by method name, in lexicographic order.

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MyWebIntegrationTests {

	//Note that TestRestTemplate is now available as bean whenever @SpringBootTest is used. It’s pre-configured to resolve
	//relative paths to http://localhost:${local.server.port}. We could have also used the @LocalServerPort annotation to
	//inject the actual port that the server is running on into a test field.
	@Autowired
	private TestRestTemplate restTemplate;
	
	
	//The names of restTemplate methods clearly indicate which HTTP method they invoke, while the second part of the name
	//indicates what is returned
	//Create user
	@Test
	public void a_createUser() {
		ResponseEntity<User> responseEntityPost = restTemplate.postForEntity("/user",
				new User("Lina", 10D), User.class);
		User user = responseEntityPost.getBody();
		
		assertEquals(HttpStatus.OK, responseEntityPost.getStatusCode());
		assertEquals("Lina", user.getName());
		
		ResponseEntity<User> responseEntityGet = restTemplate.getForEntity("/{id}/data", User.class, "153");			
		assertEquals(HttpStatus.OK, responseEntityGet.getStatusCode());
	
						
	}
	
		
	
	//getForObject() will perform a GET, convert the HTTP response into an object type of your choice, and returns that object. 
	//get user list
	@Test
	public void b_getUserList() {
		
		
		User[] userArray = restTemplate.getForObject("/user", User[].class);
		User user = userArray[userArray.length -1];		
		
		//assertThat(userArray[userArray.length -1].getTotalPoints(), equals(10.0));
		assertThat(user.getName()).isEqualTo("Lina");
		
		
	}
	
	
	
	//delete user
	@Test
	public void c_deleteUser() {
		
		
		User[] userArray = restTemplate.getForObject("/user", User[].class);
	    long userId = userArray[userArray.length - 1].getId();
		
		restTemplate.delete("/user/{id}", userId);		
		ResponseEntity<User> responseEntity = restTemplate.getForEntity("/{id}/data", User.class, userId);			
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		
		//Another way using hashmap for uri variables
			//Map<String, String> vars = new HashMap<>();
			//vars.put("id", String.valueOf(userId));		
			//restTemplate.delete("/user/{id}", vars);
			//ResponseEntity<User> responseEntity = restTemplate.getForEntity("/user/{id}", User.class, vars);	
	
		
	}
	

}
*/
