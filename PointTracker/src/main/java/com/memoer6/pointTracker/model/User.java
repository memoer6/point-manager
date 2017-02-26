package com.memoer6.pointTracker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;





@Entity
public class User {

    
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonView(View.Summary.class)
	private Long id;
    
    @NotNull
    @Size(min = 1, max = 30)
    @JsonView(View.Summary.class)
	private String name;
    
    @OneToMany(mappedBy = "user")
    private List<Transaction> transactionList = new ArrayList<>();
    
    @NotNull
    @JsonView(View.Summary.class)
    private Double totalPoints;

    protected User() { 
    	// no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
  	}
    
   
    public User(String name, Double points) {
    	
    	this.name = name;    	
    	this.totalPoints = points;
    	
    }


    public Long getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public List<Transaction> getTransactionList() {
		
		return transactionList;	
	}
	
	
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}
	


	public Double getTotalPoints() {
		return totalPoints;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}


	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}


	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", transactionList="
				+ transactionList + ", totalPoints=" + totalPoints + "]";
	}
	
	//To compare two Java objects, we need to override both equals and hashCode (Good practice).
	//The comparison is useful for testing
	
	@Override
	public int hashCode() {
		return Objects.hash(name, totalPoints, transactionList);
	}

	
	@Override
	public boolean equals(Object obj) {
		
		return (obj instanceof User)
				&& Objects.equals(name, ((User) obj).name)
				&& totalPoints == ((User) obj).totalPoints
				&& Objects.equals(transactionList, ((User) obj).transactionList);
								
	}
	
 
}
