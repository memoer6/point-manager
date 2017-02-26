package com.memoer6.pointTracker.tests;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.memoer6.pointTracker.web.UserController;

/*
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyControllerTests2 {
	
	private MockMvc mvc;
	
	@Before
	public void setUp() throws Exception {
		
		mvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
			
	}
	
		
	
	@Test
    public void testExample() throws Exception {
       
           
        mvc.perform(MockMvcRequestBuilders.get("/user").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().json("{'id':1,'name':'Lina'}"));
    }
    
}

//Note the use of the MockServletContext to set up an empty WebApplicationContext so the UserController can be created
//in the @Before and passed to MockMvcBuilders.standaloneSetup(). An alternative would be to create the full application
//context using the Application class and @Autowired the UserController into the test. The MockMvc comes from Spring Test
//and allows you, via a set of convenient builder classes, to send HTTP requests into the DispatcherServlet and make assertions
//about the result.

*/