package com.memoer6.interestcalc.rest_client;


import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.memoer6.interestcalc.config.RestConnectionProperties;
import com.memoer6.interestcalc.domain.Transaction;
import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.service.Calculator;

//The RestTemplate is the core class for client-side access to RESTful services.
//RestTemplate’s behavior is customized by providing callback methods and configuring the `HttpMessageConverter
//used to marshal objects into the HTTP request body and to unmarshall any response back into an object. 

//The @Component annotation marks a java class as a bean so the component-scanning mechanism of spring can pick it
//up and pull it into the application context.
//Most of the time, you will using @Repository, @Service and @Controller annotations. @Component should be used when
//your class does not fall into either of three categories

@Component
public class RestClient {	

	private final RestTemplate restTemplate;	
	private final Calculator calculator;	
	
	//Injected from ConnectionProperties.class
	// another option is annotate @Value("${point-tracker.server.uri}")  without using ConfigurationProperties class 
	@Autowired
	private RestConnectionProperties connection;	
	
	private static final Logger log = LoggerFactory.getLogger(RestClient.class);
			
	
	public RestClient(RestTemplateBuilder restTemplateBuilder, Calculator calculator) {		
		this.restTemplate = restTemplateBuilder.build();
		this.calculator = calculator;		
	}
	
	//The “heart” of spring-retry is the @Retryable annotation. With the maxAttempts attribute we will set how many
	//times the method should be invoked. With the @Backoff annotation we will configure an initial delay in milliseconds;
	//this delay will increase of a multiplier factor for every iteration. 
	//In case of a RestClientException, the calcDailyInterest method will be automatically reinvoked 4 times. If during the
	//last execution the method fails again, the method annotated with @Recover will be launched. If there’s no recover
	//method, the exception it’s simply throwed up.
	
	//Cron- sub expressions are separated with white-space, and represent: (1) Seconds (2) Minutes (3) Hours 
	//(4) Day-of-Month	(5) Month	(6) Day-of-Week	(7) Year (optional field)
	
	//Request calculator to add daily interest to each user
	//cron expression is defined in application.yml
	@Scheduled(cron = "${cron.query.expression}")
	@Retryable(maxAttempts=3,value=ResourceAccessException.class,backoff = @Backoff(delay = 5000,multiplier=2))
	public List<User> calcDailyInterest() throws Exception  {	
				
		//try and catch to handle the exception for the retries
		try {
						
			log.info("RestClient - Attempting to connect to point-tracker server [" + connection.getUri() + 
					"] to retrieve list of users and add daily interest");			
			
			List<User> userList = getUserList();
			
			if (userList == null) {
				log.error("calcDailyInterest:  List of users obtained from point-tracker server is null");				
			} else	if (userList.isEmpty()) {
					
				log.info("calcDailyInterest:  List of users obtained from point-tracker server is empty");	
									
			} else {					
					
				//Compute the daily interest for each user
				return calculator.addDailyInterest(userList);	
				
				//the return of list users is needed for testing				
									
			}
			
			//in case of null or empty
			return userList;
			
		} catch (Exception e) {
			
			log.error("caclDailyInterest: Error connecting to the server. " + e);
			
			//throw the exception that activate the retry
			throw new Exception(e);
									
		}
				
	}
	
	//send a transaction update to point-tracker server to add the interests saved in the month
	//every last day of the month at 10:30 pm
	@Scheduled(cron = "${cron.update.31days.expression}")
	@Scheduled(cron = "${cron.update.30days.expression}")
	@Scheduled(cron = "${cron.update.28days.expression}")
	@Retryable(maxAttempts=3,value=ResourceAccessException.class,backoff = @Backoff(delay = 5000,multiplier=2))
	public void sendInterestMonth() throws Exception {
		
		
		//try and catch to handle the exception for the retries
		try {
			
			log.info("SendInterestMonth - Attempting to connect to point-tracker server to send update transaction");		
			
			List<User> userList = calculator.getInterestMonth(getUserList());
			
			if (userList == null) {
				log.error("sendInterestMonth: List of users obtained from calculator is null");	
			
			} else 	if (userList.isEmpty()) {
				
				log.info("sendInterestMonth: List of users obtained from calculator is empty");
				
			} else {	
				
				for (User user: userList) {
					
					log.info("sendInterestMonth: Attempting to add transaction to user id:" + user.getId() +
							" name:" + user.getName());
					Float interest = user.getInterestSaved().floatValue();
					
					//Send POST message to add transaction if transaction value is not 0
					
					if (interest != 0) {
						
						ResponseEntity<User> responseEntityPost = restTemplate.postForEntity(
								connection.getUri() + "/user/{userId}/transaction",
								new Transaction(user, interest, new Date(),	"interest"),
								User.class, 
								user.getId());
						
						if (responseEntityPost.getStatusCode() == HttpStatus.OK) {
							
							log.info("sendInterestMonth: Adding " + interest + " to " + user.getName() + ". "
									+ "Transaction was sucessfully updated");
							
							//reset to 0 the interest accumulated in local repository
							calculator.resetInterestMonth(user);
							
						} 			
						
					} else {
						log.info(user.getName() + " doesn't have interest to update");
					}
				
				}
				
			}
			
		} catch (Exception e) {
			
			log.error("sendInterestMonth: Error connecting to the server. " + e);
			
			//throw the exception that activate the retry
			throw new Exception(e);
			
		}
				
	}
	
	//Retrieve the list of users from point tracker database
	public List<User> getUserList() {	
		
					
		// the method getForObject() will perform a GET, convert the HTTP response into an object type of your
		//choice and return that object. 
		//In case of an exception processing the HTTP request, an exception of the type RestClientException will
		//be thrown; this behavior can be changed by plugging in another ResponseErrorHandler implementation into
		//the RestTemplate.
		User[] userArray = restTemplate.getForObject(connection.getUri() + "/user/", User[].class);
		return Arrays.asList(userArray);
		
		
	}
	

}
