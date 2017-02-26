package com.memoer6.interestcalc.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.memoer6.interestcalc.service.Calculator;


//With any messaging-based application, you need to create a receiver that will respond to published messages.
//The Receiver is a simple POJO that defines a method for receiving messages. When you register it to receive messages,
// you can name it anything you want.

public class MessageReceiver {
	
	private static final Logger log = LoggerFactory.getLogger(MessageReceiver.class);	
	
	@Autowired
	Calculator calculator;
	
	
	public void handleMessage(Long userId) {
		
		calculator.deleteUser(userId);
		//log.info("MessageReceiver. receiving message: " + userId);
		
	}
	

	
}
