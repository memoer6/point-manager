package com.memoer6.pointTracker.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction {
		
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@JsonIgnore   //Avoid a loop in Json
	@ManyToOne
	private User user;
	
	@NotNull
	private Float value;
	
	@NotNull
	@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date date;
	private String description;
	
	protected Transaction() { 
		// no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly		
	}
	
	public Transaction(User user, Float value, Date date, String description) {
		
		this.user = user;
		this.value = value;
		this.date = date;
		this.description = description;
		
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Float getValue() {
		return value;
	}

	public Date getDate() {
		return date;
	}

	public String getDescription() {
		return description;
	}
	
	
	public void setValue(Float value) {
		this.value = value;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", user=" + user + ", value=" + value
				+ ", date=" + date + ", description=" + description + "]";
	}
	
	//To compare two Java objects, we need to override both equals and hashCode (Good practice).
	//The comparison is useful for testing
		
	@Override
	public int hashCode() {
		return Objects.hash(user, date, value, description);
	}

	
	@Override
	public boolean equals(Object obj) {
		
		return (obj instanceof Transaction)
				&& Objects.equals(user, ((Transaction) obj).user)
				&& value == ((Transaction) obj).value
				&& Objects.equals(description, ((Transaction) obj).description);
				//&& Objects.equals(date, ((Transaction) obj).date);
				//&& date.equals( (Transaction) obj).date); 
								
	}
	
	
	

}


