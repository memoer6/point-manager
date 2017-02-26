package com.memoer6.pointTracker.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.memoer6.pointTracker.config.MessageBrokerConnection;

@Service
public class MessageSender {
	
	private static final Logger log = LoggerFactory.getLogger(MessageSender.class);	
	private final RabbitTemplate rabbitTemplate;
	
	public MessageSender(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}
	
	public void sendMessage(Long message) {
		
		log.info("MessageSender. sending message " + message);
		rabbitTemplate.convertAndSend(MessageBrokerConnection.QUEUE_NAME, message);
		
	}

}
