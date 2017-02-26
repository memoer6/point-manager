package com.memoer6.interestcalc.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.domain.UserRepository;


//@Component is a generic stereotype for any Spring-managed component. @Repository, @Service, and @Controller are
//specializations of @Component for more specific use cases, for example, in the persistence, service, and presentation
//layers, respectively.
//@Service doesn’t currently provide any additional behavior over the @Component annotation, but it’s a good idea
//to use @Service over @Component in service-layer classes because it specifies intent better. 

@Service
public class Calculator {
	
	private UserRepository userRepository;
	
	
	private final Float INTEREST_RATE_SAVING = 0.1F; //10%
	private final Float INTEREST_RATE_DEBT = 0.1F; //10%
	private final int MONTHS_IN_YEAR = 12;
	private final int DAYS_IN_MONTH = 30;
	
	private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00"); 
	
	private static final Logger log = LoggerFactory.getLogger(Calculator.class);
	
	
	//Another detail that is important is how we can use constructor injection as a semantic way of declaring the
	//dependencies of an object. When we use constructor injection we are making explicit all required dependencies
	//of that object. Based on the constructor, it is clear that an instance of Calculator can't exist without an
	//instance of UserRepository.
	public Calculator(UserRepository userRepository) {		
		this.userRepository = userRepository;		
	}
				
	
	//Calculate and Add the daily interest of each user and save it in the repository
	public List<User> addDailyInterest(List<User> userList) {	
			
		for (User user: userList) {
			
			//Daily interest calculation based on total points
			Double totalPoints = user.getTotalPoints();
			Double dailyInterestSaving = ((totalPoints * INTEREST_RATE_SAVING)/ (MONTHS_IN_YEAR * DAYS_IN_MONTH));
			Double dailyInterestDebt = ((totalPoints * INTEREST_RATE_DEBT)/ (MONTHS_IN_YEAR * DAYS_IN_MONTH));
			
			//Store the aggregated interest of the month
			User localUser = userRepository.findOne(user.getId()); 
			
			if (localUser == null) {
				localUser = new User(user.getId(), user.getName(), user.getTotalPoints(), 0.0);				
			} else {
				
				//update total points in local repository
				localUser.setTotalPoints(user.getTotalPoints());				
			}
			
			//evaluate saving or debt
			if (localUser.getTotalPoints() >= 0.1) {
				localUser.setInterestSaved(localUser.getInterestSaved() + dailyInterestSaving);
				
				log.info(localUser.getName() + " added " + decimalFormat.format(dailyInterestSaving) + 
						" interest points, having a total of " +
						decimalFormat.format(localUser.getInterestSaved()) + " points obtained in the month, and "
								+ decimalFormat.format(localUser.getTotalPoints()) + " total points");		
			
			} else if (localUser.getTotalPoints() <= -0.1) {
				localUser.setInterestSaved(localUser.getInterestSaved() + dailyInterestDebt);
				
				log.info(localUser.getName() + " substracted " + decimalFormat.format(dailyInterestDebt) + 
						" interest points, having a total of " +
						decimalFormat.format(localUser.getInterestSaved()) + " points obtained in the month, and "
								+ decimalFormat.format(localUser.getTotalPoints()) + " total points");	
			
			} else {
				
				log.info(localUser.getName() + " doesn't get interest points, having a total of " +
						decimalFormat.format(localUser.getInterestSaved()) + " points obtained in the month, and "
								+ decimalFormat.format(localUser.getTotalPoints()) + " total points");					
			}
														
			userRepository.save(localUser);
				
		}
		
		//This return is used only for testing. It's not required by the rest client
		return userRepository.findAll();
						
	}	
	

	//provide the list of users with the aggregated interest field to rest client 
	public List<User> getInterestMonth(List<User> userList) {
		
		List<User> userListwithInterest = new ArrayList<>();
		
		if (userList == null) {
			log.error("calculator: List of users obtained from rest client is null");	
			return null;
		
		} else 	if (userList.isEmpty()) {
			
			log.info("calculator: List of users obtained from rest client is empty");
			
		} else {				
			
			for (User user: userList) {
				
				//check that user exist in local database. Avoid null when source user list change before
				//running interest calculation
				if (userRepository.exists(user.getId())) {
					
					userListwithInterest.add(userRepository.findOne(user.getId()));
					
				}
				
				
			}
		}		
		return userListwithInterest;					
					
	}
	
	
	//reset to 0 the aggregated interest 
	public void resetInterestMonth(User user) {
		
		user.setInterestSaved(0D);
		
		if (userRepository.save(user).getInterestSaved() == 0) {
			
			log.info("Interest is reset for user id:" + user.getId() + " name:" + user.getName());
			
		}		
	
	}
	
	//delete user triggered by AMQP message received when deleting user in point-tracker database 
	public void deleteUser(Long userId) {
		
		User user = userRepository.findOne(userId);
		userRepository.delete(userId);
		log.info("User " + user.getName() + " with userId= " + user.getId() + " is deleted from database");
		
	}
	
	
}	
