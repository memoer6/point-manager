package com.memoer6.pointTracker.web;
 

import java.util.List;

import com.memoer6.pointTracker.model.Transaction;
import com.memoer6.pointTracker.model.User;


public interface PointTrackerSvcAPI {
	
	
	//Get user list
	public List<User> getUserList(); 
		
	//Add user
	public User createUser(User user); 		
	
	   
    //Read user data
	public User readUserData(long userId, int count, boolean invert);  
		
			
	//Update user
	public User updateUser(long userId,	User user); 
		
	
	//Add Transaction
	public User addTransaction(long userId, Transaction transaction); 
	    	
				
	//Update transaction
	public User updateTransaction(long userId,	Transaction transaction); 
		
		
	//Delete Transaction
	public User deleteTransaction(long userId, long transactionId); 
		
	//Delete user
	public List<User> deleteUser(long userId); 
		
		
}
