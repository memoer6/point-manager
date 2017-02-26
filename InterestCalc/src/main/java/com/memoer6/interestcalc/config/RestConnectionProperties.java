package com.memoer6.interestcalc.config;


import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

//The @Component annotation marks a java class as a bean so the component-scanning mechanism of spring can pick it
//up and pull it into the application context.
//Most of the time, you will using @Repository, @Service and @Controller annotations. @Component should be used when
//your class does not fall into either of three categories

//Spring Boot allows you to externalize your configuration so you can work with the same application code in different
//environments. You can use properties files, YAML files, environment variables and command-line arguments to externalize
//configuration. Property values can be injected directly into your beans using the @Value annotation, accessed via
//Spring’s Environment abstraction or bound to structured objects via @ConfigurationProperties.

//Using the @Value("${property}") annotation to inject configuration properties can sometimes be cumbersome, especially
//if you are working with multiple properties or your data is hierarchical in nature. Spring Boot provides an alternative
//method of working with properties that allows strongly typed beans to govern and validate the configuration
//of your application.

//Value is defined in application.yml (point-tracker.server.uri)

@Primary
@Component
@ConfigurationProperties(prefix="point-tracker.server")
public class RestConnectionProperties {	
	

	//Spring Boot will attempt to validate external configuration, by default using JSR-303 (if it is on the classpath).
	//You can simply add JSR-303 javax.validation constraint annotations to your @ConfigurationProperties class	
	@NotNull
	public String uri;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	
}

// @Primary is used to avoid the following error:

//Field connection in com.memoer6.interestcalc.rest_client.RestClient required a single bean, but 2 were found:
//	- restConnectionProperties: defined in file [/home/eduardo/Coding/JavaEclipseProjects/InterestCalc/bin/com/memoer6/interestcalc/config/RestConnectionProperties.class]
//	- point-tracker.server-com.memoer6.interestcalc.config.RestConnectionProperties: defined in null
//Action:
//Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed