package com.memoer6.interestcalc.domain;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

//CustomerRepository extends the MongoRepository interface and plugs in the type of values and id it works with: User
//and Long. Out-of-the-box, this interface comes with many operations, including standard CRUD operations
//(create-read-update-delete).

public interface UserRepository extends MongoRepository<User, Long> {
	
	//Optional<User> findOne(Long userId);
		

}
