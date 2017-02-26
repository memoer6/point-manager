package com.memoer6.interestcalc.config;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.memoer6.interestcalc.messaging.MessageReceiver;



//Usually the class that defines the main method is also a good candidate as the primary @Configuration.
//You don’t need to put all your @Configuration into a single class. The @Import annotation can be used to import
//additional configuration classes. Alternatively, you can use @ComponentScan to automatically pick up all Spring
//components, including @Configuration classes.


@Configuration
public class MessageBrokerConnection {
	
		
	//Set up the RabbitMQ consumer
	//Register a Receiver with the message listener container to receive messages. The connection factory set the connection
	//to the RabbitMQ server.
	final static String QUEUE_NAME = "interestCalc-queue";

	
	// creates an AMQP queue
	//Queue(String name, boolean durable)
	//name - the name of the queue.
	//durable - true if we are declaring a durable queue (the queue will survive a server restart)
	//exclusive - true if we are declaring an exclusive queue (the queue will only be used by the declarer's connection)
	//autoDelete - true if the server should delete the queue when it is no longer in use
	@Bean
    Queue queue() {
        return new Queue(QUEUE_NAME, false);            
    }
	
	
	//The bean defined in the listenerAdapter() method is registered as a message listener in the container defined
	//in container(). It will listen for messages on the QUEUE_NAME queue. Because the Receiver class is a POJO, it
	//needs to be wrapped in the MessageListenerAdapter, where you specify it to invoke receiveMessage.
	
	@Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }
	
	@Bean
    MessageReceiver receiver() {
        return new MessageReceiver();
	 }
	 
	 
	
	@Bean
    MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
	
        return new MessageListenerAdapter(receiver, "handleMessage");
	 }

	
}

