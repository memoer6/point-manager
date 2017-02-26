package com.memoer6.pointTracker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




//Usually the class that defines the main method is also a good candidate as the primary @Configuration.
//You don’t need to put all your @Configuration into a single class. The @Import annotation can be used to import
//additional configuration classes. Alternatively, you can use @ComponentScan to automatically pick up all Spring
//components, including @Configuration classes.


@Configuration
public class MessageBrokerConnection {
	
	//Set up the RabbitMQ producer
	
	public final static String QUEUE_NAME = "interestCalc-queue";
	final static String EXCHANGE_NAME = "point-tracker-exchange";
	
	// creates an AMQP queue
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


	@Bean
	    TopicExchange exchange() {
	        return new TopicExchange(EXCHANGE_NAME);
	}
	
	//Given that a producer sends to an Exchange and a consumer receives from a Queue, the bindings that connect
	//Queues to Exchanges are critical for connecting those producers and consumers via messaging. In Spring AMQP,
	//we define a Binding class to represent those connections. 
	@Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_NAME);
    }
 	
	

}
