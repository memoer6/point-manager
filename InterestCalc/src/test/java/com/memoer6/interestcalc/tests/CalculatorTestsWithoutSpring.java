package com.memoer6.interestcalc.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.domain.UserRepository;
import com.memoer6.interestcalc.service.Calculator;

import static org.junit.Assert.*;


import static org.mockito.AdditionalAnswers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;



/*
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;

import static org.mockito.internal.verification.VerificationModeFactory.times;

*/

//https://dzone.com/articles/unit-and-integration-tests-in-spring-boot

//A good practice when working with Spring is to isolate your business logic from specific Spring functionalities.
//Everyone wants to keep their code loosely coupled and with high cohesion. When you need to mock lots of dependencies
//to unit test a specific thing, that is a sign of high coupling. If you caught yourself in this situation, maybe it's
//a good idea to stop and think about separhttps://dzone.com/articles/unit-and-integration-tests-in-spring-bootating some concerns into new classes.

//Take a look into the Calculator class. You'll notice that it has a single dependency on the UserRepository class.
//Now take a look into the CalculatorTestwoSpring and notice how I can mock the dependencies of my Calculator easily
//and execute simple unit tests using zero Spring functionality.

public class CalculatorTestsWithoutSpring {
	
	private Calculator calculator;
	private UserRepository userRepositoryMock;
	private List<User> mockUserList;
	
	@Before
    public void setUp() {
        
		//Class initialization
		userRepositoryMock = Mockito.mock(UserRepository.class);
        calculator = new Calculator(userRepositoryMock);
        
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
	@Test 
    public void addInterestNullList() throws Exception {			
	
        assertNull(calculator.addDailyInterest(null));
	}
	
	// Add not valid list in addDailyInterest method 
	@Test(expected = IllegalStateException.class)
	public void addInterestIllegalList() throws Exception {	
		Mockito.doThrow(new IllegalStateException()).when(userRepositoryMock).findOne(-1L);		
		
		mockUserList.get(0).setId(-1L);		
	    calculator.addDailyInterest(mockUserList);
	 }

	
	
	
	

}
