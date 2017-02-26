package com.memoer6.interestcalc.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.rest_client.RestClient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RetryRestClientTests {

      
    public RestClient mockRestClient; 
    List<User> mockUserList;
    
    @Before
    public void setUp() throws Exception {
    	
    	mockRestClient = Mockito.mock(RestClient.class);
    	
    	 //mock User list
		mockUserList = new ArrayList<>();
		User user = new User(1L, "Lina", 1000.0, 0.0);
		mockUserList.add(user);		
    	
	    
    }

    //Connection in 3 retries
    @Test (expected=ResourceAccessException.class)
    public void testConnectionIn3retries() throws Exception {
    	
    	 when(mockRestClient.calcDailyInterest())
         .thenThrow(new ResourceAccessException("Remote Exception 1"))
         .thenThrow(new ResourceAccessException("Remote Exception 2"))
         .thenReturn(mockUserList);    	
    	
        List<User> userList = this.mockRestClient.calcDailyInterest();
        verify(mockRestClient, times(3)).calcDailyInterest();
        assertThat(userList, is(mockUserList));
    }
    
    //Cannot connect after 3 retries
    @Test (expected=ResourceAccessException.class)
    public void testFailedConnectionAfter3retries() throws Exception {
    	
    	 when(mockRestClient.calcDailyInterest())
         .thenThrow(new ResourceAccessException("Remote Exception 1"))
         .thenThrow(new ResourceAccessException("Remote Exception 2"))
         .thenThrow(new ResourceAccessException("Remote Exception 3"))
         .thenReturn(mockUserList);
    	
        List<User> userList = this.mockRestClient.calcDailyInterest();
        verify(mockRestClient, times(3)).calcDailyInterest();
        assertThat(userList, is(nullValue()));
        
    }


}
