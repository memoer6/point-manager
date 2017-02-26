package com.memoer6.pointTracker.web;



import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Lists;
import com.memoer6.pointTracker.messaging.MessageSender;
import com.memoer6.pointTracker.model.Transaction;
import com.memoer6.pointTracker.model.User;
import com.memoer6.pointTracker.model.View;
import com.memoer6.pointTracker.repository.TransactionRepository;
import com.memoer6.pointTracker.repository.UserRepository;



//HTTP requests are handled by a controller. These components are easily identified by the @RestController annotation,
//and the UserController below handles GET requests 

//Spring 4.0 introduced @RestController, a specialized version of the controller which is a convenience annotation
//that does nothing more than add the @Controller and @ResponseBody annotations. By annotating the controller class
//with @RestController annotation, you no longer need to add @ResponseBody to all the request mapping methods. 
//The @ResponseBody annotation is active by default

//The key difference between a human-facing controller and a REST endpoint controller is in how the response is created.
//Rather than rely on a view (such as JSP) to render model data in HTML, an endpoint controller simply returns the data
//to be written directly to the body of the response.
//The @ResponseBody annotation tells Spring MVC not to render a model into a view, but rather to write the returned
//object into the response body. It does this by using one of Spring’s message converters. Because Jackson 2 is in the
//classpath, this means that MappingJackson2HttpMessageConverter will handle the conversion of User and Transaction to JSON
//if the request’s Accept header specifies that JSON should be returned.
//How do you know Jackson 2 is on the classpath? Either run ` mvn dependency:tree` or ./gradlew dependencies and you’ll
//get a detailed tree of dependencies which shows Jackson 2.x. You can also see that it comes from spring-boot-starter-web.




@RestController
@RequestMapping("/user")
@CrossOrigin   //enable CORS on all handler methods of this class
public class UserController implements PointTrackerSvcAPI {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private MessageSender messageSender;
	

	//Get user list
	@RequestMapping(method = RequestMethod.GET)
	@JsonView(View.Summary.class)  //returns JSON with only username, id and totalPoints
    public List<User> getUserList() {	
			    	
        //using guava library to return list instead of iterable
		return Lists.newArrayList(userRepository.findAll()); 
				
		    	
    }
	
	//Add user
	@RequestMapping(method = RequestMethod.POST)   
	public User createUser(@RequestBody User user) {		
		
	if (user.getName() == null || user.getName().isEmpty())
				throw new UserWithoutNameException();					
				    	
        return userRepository.save(new User(user.getName(), user.getTotalPoints()));    	
    	
    }
    
      
    //Read user data
	@RequestMapping(value = "{userId}/data", method = RequestMethod.GET)
    public User readUserData(@PathVariable("userId") long userId, 
    		@RequestParam(value = "count", defaultValue = "-1", required = false) int count,
    		@RequestParam(value = "invert", defaultValue = "true", required = false) boolean invert) { 
		
		
		//ejemplo:   http://localhost:8080/user/72/data?invert=false&count=3
		
		User user = this.validateUser(userId);	
		List<Transaction> transactionList = user.getTransactionList();
		
		//Change the order of transaction list if new_first is true
		if (invert == true) Collections.reverse(transactionList);
				
		
		//number of transactions to skip so we stay with "count" records starting for the bottom of the list
		//if count is higher than the number of records, don't skip, otherwise skip the delta 
		//between list size and count"
		//int jump = (count > transactionList.size()) ? 0: transactionList.size() - count;
		
		// If count is < 0 set the whole transaction list
		user.setTransactionList((count < 0) ? transactionList :
		//	transactionList.stream().skip(jump).collect(Collectors.toList()));
			transactionList.stream().limit(count).collect(Collectors.toList()));
				
		return user;				      	
       
    }
	
	//Update user
	@RequestMapping(value = "{userId}/update", method = RequestMethod.POST)
	public User updateUser(@PathVariable("userId") long userId,								 
						   @RequestBody User user) {
		
		User storedUser = this.validateUser(userId);
		
		if (user.getName() == null || user.getName().isEmpty())
			throw new UserWithoutNameException();	
		
		storedUser.setName(user.getName());
		storedUser.setTotalPoints(user.getTotalPoints());
		
		//reverse transaction list
		Collections.reverse(storedUser.getTransactionList());
		return userRepository.save(storedUser);		
				
	}
	
	
	//Add Transaction
	@RequestMapping(value = "{userId}/transaction", method = RequestMethod.POST)
	public User addTransaction(@PathVariable("userId") long userId,
						@RequestBody Transaction transaction) {
	    	
		User user = this.validateUser(userId);	
		
		if (transaction.getValue() == null || transaction.getValue() == 0f) 
			throw new TransactionWithoutValueException();	
		
		//update points
		user.setTotalPoints(user.getTotalPoints() + transaction.getValue());
		transactionRepository.save(new Transaction(user, 
        		transaction.getValue(), transaction.getDate(), transaction.getDescription()));
		
		//reverse transaction list
		Collections.reverse(user.getTransactionList());
		return userRepository.save(user);
		
		        		
	}
		
	//Update transaction
	@RequestMapping(value = "{userId}/update/transaction", method = RequestMethod.POST)
	public User updateTransaction(@PathVariable("userId") long userId,								 
								  @RequestBody Transaction transaction) {
		
		User user = this.validateUser(userId);
		Transaction storedTransaction = this.validateTransaction(user, transaction.getId());
		
		if (transaction.getValue() == null || transaction.getValue() == 0f) 
			throw new TransactionWithoutValueException();
		
		Float oldValue = storedTransaction.getValue();
		storedTransaction.setDate(transaction.getDate());
		storedTransaction.setValue(transaction.getValue());
		storedTransaction.setDescription(transaction.getDescription());
	    transactionRepository.save(storedTransaction);
	    
	    //update points
	    user.setTotalPoints(user.getTotalPoints() - oldValue + transaction.getValue());
	    
	    //reverse transaction list
	    Collections.reverse(user.getTransactionList());
		return userRepository.save(user);
	    
						
	}
	
	
	
	//Delete Transaction
	@RequestMapping(value = "{userId}/transaction/{transactionId}", method = RequestMethod.DELETE)
	public User deleteTransaction(@PathVariable("userId") long userId,
								  @PathVariable("transactionId") long transactionId) {
		
		User user = this.validateUser(userId);		
		Transaction transaction = this.validateTransaction(user, transactionId);
		
		//update points
	    user.setTotalPoints(user.getTotalPoints() - transaction.getValue());
		
	    transactionRepository.delete(transactionId);
	    
	    //reverse transaction list
	    Collections.reverse(user.getTransactionList());
	    return userRepository.save(user);
				
	}
	
	//Delete user
	@RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
	@JsonView(View.Summary.class)  //returns JSON with only username and id
	public List<User> deleteUser(@PathVariable("userId") long userId) {
		
		this.validateUser(userId);
			
		User user = userRepository.findOne(userId);
		transactionRepository.deleteByUser(user);
		userRepository.delete(userId);
		
		//send message to interestcalc to remove this user in its database too		
		messageSender.sendMessage(userId);
		
		return Lists.newArrayList(userRepository.findAll());
	}
	
	
	//Validate User
	private User validateUser(Long userId) {
		return this.userRepository.findById(userId).orElseThrow(
				() -> new UserNotFoundException(userId));
	}
	
	//Validate Transaction
	private Transaction validateTransaction(User user, Long transactionId) {
		
		return this.transactionRepository.findByIdAndUser(transactionId, user).orElseThrow(
				() -> new TransactionNotFoundException(user.getName(), transactionId));
	}
	
		
    
}

//Exceptions
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long userId) {
		super("Could not find user '" + userId + "'.");
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TransactionNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public TransactionNotFoundException(String userName, Long transactionId) {
		super("Could not find transaction '" + transactionId + 
				"' from user '" + userName + "'." );
	}

}

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The user must have a name")
class UserWithoutNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The transaction must have a value")
class TransactionWithoutValueException extends RuntimeException {
	private static final long serialVersionUID = 1L;
}






