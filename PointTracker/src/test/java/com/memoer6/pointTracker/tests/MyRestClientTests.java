package com.memoer6.pointTracker.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.RequestMatcher;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.memoer6.pointTracker.model.User;

//For testing such a REST client built with RestTemplateBuilder, you may use a SpringRunner-executed test
//class annotated with @RestClientTest. This annotation disables full auto-configuration and only applies
//configuration relevant to REST client tests, i.e. Jackson or GSON auto-configuration and @JsonComponent
//beans, but not regular @Component beans.
//@RestClientTest ensures that Jackson and GSON support is auto-configured, and also adds pre-configured
//RestTemplateBuilder and MockRestServiceServer instances to the context. The bean under test is specified
//with value or components attribute of the @RestClientTest annotation

//So, what’s new?
//First – the @RestClientTest annotation allows us to specify the exact service under test – in our case it
//is the UserServiceClient class. This service will be loaded into the test context, while everything else
//is filtered out. This allows us to autowire the UserServiceClient instance inside our test and leave
//everything else outside, which speeds up the loading of the context.

//Second – as the MockRestServiceServer instance is also configured for a @RestClientTest-annotated test
//(and bound to the UserServiceClient instance for us), we can simply inject it and use.

//Finally – JSON support for @RestClientTest allows us to inject the Jackson’s ObjectMapper instance to prepare
//the MockRestServiceServer’s mock answer value.

//All that is left to do is to execute the call to our service and verify the results


@RunWith(SpringRunner.class)
@RestClientTest(UserServiceClient.class)
public class MyRestClientTests {

    @Autowired
    private UserServiceClient client;

    @Autowired
    private MockRestServiceServer server;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    
	
	@Before
	public void setUp() throws Exception {
		
		List<User> mockUserList = new ArrayList<>();		
		mockUserList.add(new User("Lina", 10.0));
		
		
		String listUserString = objectMapper.writeValueAsString(mockUserList);
		
		 server.expect(requestTo("/user"))
         .andRespond(withSuccess(listUserString, MediaType.APPLICATION_JSON));
		
	}

    @Test
    public void getUserListWhenResultIsSuccessShouldReturnUserList()
            throws Exception {
       
    	 List<User> userList = client.getUserList();
    	 
         assertThat(userList.get(0).getName()).isEqualTo("Lina");
         assertThat(userList.get(0).getTotalPoints()).isEqualTo(10.0);
    	
    }

	
}

