package com.memoer6.interestcalc;



import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.memoer6.interestcalc.config.RestConnectionProperties;
import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.rest_client.RestClient;





//Although it is possible to package this service as a traditional WAR file for deployment to an external application server,
//the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable
// JAR file, driven by a good old Java main() method. Along the way, you use Spring’s support for embedding the Tomcat
// servlet container as the HTTP runtime, instead of deploying to an external instance.

//If you need to run some specific code once the SpringApplication has started, you can implement the ApplicationRunner
//or CommandLineRunner interfaces. Both interfaces work in the same way and offer a single run method which will
//be called just before SpringApplication.run(…​) completes.
//The CommandLineRunner interfaces provides access to application arguments as a simple string array, whereas the
//ApplicationRunner uses the ApplicationArguments interface

@SpringBootApplication
@EnableScheduling
@EnableRetry
@EnableConfigurationProperties(RestConnectionProperties.class)
public class Application implements CommandLineRunner {
	
	@Autowired
	RestClient restClient;
	
	@Autowired
	RestConnectionProperties connection;
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);	
	
	//Our main method delegates to Spring Boot’s SpringApplication class by calling run. SpringApplication will
	//bootstrap our application, starting Spring which will in turn start the auto-configured Tomcat web server. We need
	//to pass Application.class as an argument to the run method to tell SpringApplication which is the primary Spring component.
	//The args array is also passed through to expose any command-line arguments.
	public static void main(String[] args)  {
		SpringApplication.run(Application.class, args);			
	}	
	
	//Method added by the CommandLineRunner interface implemented in Application.class	
	//Test if the connection to point-tracker server is successful
	@Override
	public void run(String... args) {
		
		
		try {
			
			//wait for point-tracker web service to be ready
			TimeUnit.SECONDS.sleep(10);
			
			List<User> userList = restClient.getUserList();
			
			log.info("Successful connection to: " + connection.getUri());
			log.info("Users in the original list are: ");
			for (User user : userList) {
				log.info("- " + user.getName());
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
		
}	
	

//@SpringBootApplication is a convenience annotation that adds all of the following:
//-  @Configuration tags the class as a source of bean definitions for the application context.
//-  @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and
//    various property settings. By default all packages below your main configuration class (the one annotated with 
//     @EnableAutoConfiguration or @SpringBootApplication) will be searched.
//-  Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees
//spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors
//such as setting up a DispatcherServlet.
//-  @ComponentScan tells Spring to look for other components, configurations, and services in the package, allowing
//it to find the Calculator service. Spring will scan the project's classpath looking for components, and make them available
//as autowire candidates within the application.

//If you are using Gradle, you can run the application using ./gradlew bootRun.

//You can build a single executable JAR file that contains all the necessary dependencies, classes, and resources.
//This makes it easy to ship, version, and deploy the service as an application throughout the development lifecycle,
//across different environments, and so forth.

//   ./gradlew build

//Then you can run the JAR file:

//java -jar build/libs/gs-rest-service-0.1.0.jar

