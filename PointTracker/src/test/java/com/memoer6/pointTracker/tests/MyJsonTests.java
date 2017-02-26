package com.memoer6.pointTracker.tests;

import java.io.File;
import java.text.SimpleDateFormat;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.json.*;
import org.springframework.boot.test.json.*;
import org.springframework.test.context.junit4.*;

import static org.assertj.core.api.Assertions.*;

import com.memoer6.pointTracker.model.Transaction;
import com.memoer6.pointTracker.model.User;


//To test that Object JSON serialization and deserialization is working as expected you can use the @JsonTest
//annotation. @JsonTest will auto-configure Jackson ObjectMapper, any @JsonComponent beans and any Jackson Modules.
//It also configures Gson if you happen to be using that instead of, or as well as, Jackson. If you need to
//configure elements of the auto-configuration you can use the @AutoConfigureJsonTesters annotation.


@RunWith(SpringRunner.class)
@JsonTest
public class MyJsonTests {
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	@Autowired
	private JacksonTester<User> jsonUser;
	
	@Autowired
	private JacksonTester<Transaction> jsonTrans;
	
	
	@Test
	public void testSerializeUser() throws Exception {
		
		User user = new User("Lina", 10D);
		// Assert against a `.json` file in the same package as the test
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("expectedUser.json").getFile());		
		assertThat(this.jsonUser.write(user)).isEqualToJson(file);
		
		// Or use JSON path based assertions
		 assertThat(this.jsonUser.write(user)).hasJsonPathStringValue("@.name");
	     assertThat(this.jsonUser.write(user)).extractingJsonPathStringValue("@.name")
	                .isEqualTo("Lina");		
		
	}
	
	/* 
	@Test
	 public void testDeserializeUser() throws Exception {
	        String content = "{\"name\":\"Lina\",\"transactionList\":[],\"totalPoints\":10.0}";
	        assertThat(this.jsonUser.parse(content))
	                .isEqualTo(new User("Lina",10D));
	        assertThat(this.jsonUser.parseObject(content).getName()).isEqualTo("Lina");
	 }
	 */
	 
	 @Test
	public void testSerializeTransaction() throws Exception {
		
		Transaction transaction = new Transaction(new User("Lina", 5D), 10f, sdf.parse("2016-12-01"), "a description");
		// Assert against a `.json` file in the same package as the test
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("expectedTransaction.json").getFile());		
		assertThat(this.jsonTrans.write(transaction)).isEqualToJson(file);
		
		// Or use JSON path based assertions
		 assertThat(this.jsonTrans.write(transaction)).hasJsonPathStringValue("@.description");
	     assertThat(this.jsonTrans.write(transaction)).extractingJsonPathStringValue("@.description")
	                .isEqualTo("a description");		
			
	}
	 
	 /*
	 @Test
	    public void testDeserializeTransaction() throws Exception {
		 
		 	        String content = "{\"value\":10.0,\"date\":\"2016-12-02\",\"description\":\"a description\"}";
	        assertThat(this.jsonTrans.parse(content))
	                .isEqualTo(new Transaction(null, 10f, sdf.parse("2016-12-01"), "a description"));
	        assertThat(this.jsonTrans.parseObject(content).getDescription()).isEqualTo("a description");
	 }
	 */
	

}
