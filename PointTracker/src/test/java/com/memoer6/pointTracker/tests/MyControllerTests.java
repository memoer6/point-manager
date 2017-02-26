package com.memoer6.pointTracker.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.memoer6.pointTracker.model.User;
import com.memoer6.pointTracker.web.PointTrackerSvcAPI;
import com.memoer6.pointTracker.web.UserController;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//To test Spring MVC controllers are working as expected you can use the @WebMvcTest annotation.
//Often @WebMvcTest will be limited to a single controller and used in combination with @MockBean
//to provide mock implementations for required collaborators.
//@WebMvcTest also auto-configures MockMvc. Mock MVC offers a powerful way to quickly test MVC controllers
//without needing to start a full HTTP server.


/*
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class MyControllerTests {
	
	@Autowired
    private MockMvc mvc;
	
	@MockBean
    private PointTrackerSvcAPI client;
	
	List<User> mockUserList;
	
	
	@Before
	public void setUp() {
		
		mockUserList = new ArrayList<>();
		User mockUser = new User("Lina", 10D);
		mockUserList.add(mockUser);
		
	}
	
		
	
	@Test
    public void testExample() throws Exception {
        given(this.client.getUserList())
                .willReturn((mockUserList));
        
        mvc.perform(get("/user").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{'id':1,'name':'Lina'}"));
    }
    


	

}
*/
