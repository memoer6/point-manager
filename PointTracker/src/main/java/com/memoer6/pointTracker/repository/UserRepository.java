package com.memoer6.pointTracker.repository;

//Boot is leveraging Spring Data for its Object Relational Mapping, and given that, we can leverage its conventions and mechanisms
//for working with databases. A convenient abstraction that Spring Data provides is the concept of a "repository", which is
//essentially a data access object (DAO) that is automatically wired together on our behalf. To get CRUD functionality for
//the User entity, we need only create an interface that extends Spring Data's CrudRepository 
//Spring Data JPA repositories are interfaces that you can define to access data. JPA queries are created automatically
// from your method names.
// For more complex queries you can annotate your method using Spring Data’s Query annotation.
//By default, JPA databases will be automatically created only if you use an embedded database (H2, HSQL or Derby)



import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.memoer6.pointTracker.model.User;


public interface UserRepository extends CrudRepository<User, Long> {
    
	Optional<User> findById(Long userId);

	//Used only for JPA testing
	Optional<User> findByName(String string);

	//List<User> findAllByOrderByNameAsc();	
	
}


//Spring Data can create implementations for you of @Repository interfaces of various flavors. Spring Boot will
//handle all of that for you as long as those @Repositories are included in the same package (or a sub-package)
//of your @EnableAutoConfiguration class.

//Spring Boot tries to guess the location of your @Repository definitions, based on the @EnableAutoConfiguration it finds.
