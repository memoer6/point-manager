package com.memoer6.interestcalc.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.domain.UserRepository;
import com.memoer6.interestcalc.service.Calculator;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorTests {
	
	@Mock
	private UserRepository userRepositoryMock;
	
	
	@InjectMocks
	private Calculator calculator = new Calculator(userRepositoryMock);
	
	private List<User> mockUserList;
	
		
	
	@Before
    public void setUp() {
        
		       
        //mock User list
        mockUserList = new ArrayList<>();
        User user = new User(1L, "Lina", 1000.0, 0.0);
        user.setInterestSaved(10D);
		mockUserList.add(user);		
    }
	
	
	
	//Add user list in addDailyInterst method with user in repository
	@Test
    public void addInterestExistingUser() throws Exception {
	 
        when(userRepositoryMock.findOne(1L)).thenReturn(mockUserList.get(0));
        when(userRepositoryMock.findAll()).thenReturn(mockUserList);
        
        User user = calculator.addDailyInterest(mockUserList).get(0);
        
        assertThat(user.getName(), comparesEqualTo("Lina"));
        //0.01 is marginal error
        assertEquals(10.27, user.getInterestSaved(), 0.01);	    
	 }
	 
	//Add user list in addDailyInterest method with user not in repository
	@Test
    public void addInterestNonExistingUser() throws Exception {
	 
	 	when(userRepositoryMock.findOne(1L)).thenReturn(null);
	 	Mockito.doAnswer(returnsFirstArg()).when(userRepositoryMock).save(Mockito.any(User.class));
        
	 	
	 	
        User user = calculator.addDailyInterest(mockUserList).get(0);
        assertThat(user.getName(), comparesEqualTo("Lina"));
        assertEquals(0.27, user.getInterestSaved(), 0.01);	    
	 }
	 
	// Add null list in addDailyInterest method 
	@Test(expected = NullPointerException.class)
    public void addInterestNullList() throws Exception {	
	
        calculator.addDailyInterest(null);
	}
	
	// Add not valid list in addDailyInterest method 
	@Test(expected = NullPointerException.class)
	public void createClientWithEmptyName() throws Exception {	
		Mockito.doThrow(new NullPointerException()).when(userRepositoryMock).findOne(-1L);		
		
		mockUserList.get(0).setId(-1L);		
	    calculator.addDailyInterest(mockUserList);
	 }
	 

	
}
