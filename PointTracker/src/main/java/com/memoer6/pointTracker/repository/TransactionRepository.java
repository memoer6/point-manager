package com.memoer6.pointTracker.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;


import com.memoer6.pointTracker.model.Transaction;
import com.memoer6.pointTracker.model.User;


public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    
	//Collection<Transaction> findByUser(String user);
	
	@Modifying
	@Transactional
	void deleteByUser(User user);
	
	Optional<Transaction> findByIdAndUser(Long Id, User user);
    
    
}
