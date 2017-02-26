package com.memoer6.interestcalc.tests;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import com.memoer6.interestcalc.domain.User;
import com.memoer6.interestcalc.domain.UserRepository;

//Spring Boot provides a @SpringBootTest annotation which can be used as an alternative to the standard spring-test
//@ContextConfiguration annotation when you need Spring Boot features. The annotation works by creating the
//ApplicationContext used in your tests via SpringApplication.
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoryTests {
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	public void readFirstUser() {
		List<User> userList = userRepository.findAll();
		assertThat(userList.size(), is(greaterThan(0)));
	}

}
