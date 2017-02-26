package com.memoer6.interestcalc.domain;


import java.util.Objects;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class User {

    
	//The id is mostly for internal use by MongoDB.every document in MongoDB is required to have a primary key
	//with name _id, we can either provide it or MongoDB will generate it for us
	@Id
	private Long id;
   	private String name;
    private Double totalPoints;       
    private Double interestSaved;
    
 
	protected User() {  	}
    
   
    public User(Long id, String name, Double points, Double interestSaved) {
    	
    	this.id = id;
    	this.name = name;    	
    	this.totalPoints = points;
    	this.interestSaved = interestSaved;
    	
    }


    public Long getId() {
		return id;
	}


	
	public String getName() {
		return name;
	}
	
	public Double getInterestSaved() {
		
		return interestSaved;
	}
	
	public Double getTotalPoints() {
		return totalPoints;
	}
	
	public void setId(Long id) {
		this.id = id;
	}


	public void setInterestSaved(Double interestSaved) {
		this.interestSaved = interestSaved;
	}

	
		
	public void setName(String name) {
		this.name = name;
	}


	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}


	
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", " + ", totalPoints=" + totalPoints +
				", interestSaved=" + interestSaved + "]";
	}
	
	//To compare two Java objects, we need to override both equals and hashCode (Good practice).
	//The comparison is useful for testing
	
	
	@Override
	public int hashCode() {
		return Objects.hash(name, totalPoints);
	}

	
	@Override
	public boolean equals(Object obj) {
		
		return (obj instanceof User)
				&& Objects.equals(name, ((User) obj).name)
				&& totalPoints == ((User) obj).totalPoints;
								
	}
	
	
 
}
