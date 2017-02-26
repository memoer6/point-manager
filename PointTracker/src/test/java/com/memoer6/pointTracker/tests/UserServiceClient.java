package com.memoer6.pointTracker.tests;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.memoer6.pointTracker.model.User;

//client for rest client test. 

//Spring Boot 1.4 brings both the auto-configured RestTemplateBuilder to simplify creating RestTemplates,
//and the matching @RestClientTest annotation to test the clients built with RestTemplateBuilder. Here’s
//how you can create a simple REST client with RestTemplateBuilder auto-injected for you
//Notice that we did not explicitly wire the RestTemplateBuilder instance to a constructor. This is possible
//thanks to a new Spring 4.3 feature called implicit constructor injection

@Service
public class UserServiceClient {
	
	private final RestTemplate restTemplate;
	
	public UserServiceClient(RestTemplateBuilder restTemplateBuilder) {
		
		restTemplate = restTemplateBuilder.build();
	}
	
	
	
	public List<User> getUserList() {
		User[] userArray = restTemplate.getForObject("/user", User[].class);
		return Arrays.asList(userArray);
		
	}
	
	

}
