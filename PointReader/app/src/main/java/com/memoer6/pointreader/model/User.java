package com.memoer6.pointreader.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {


    private Long id;
    private String name;
    private List<Transaction> transactionList = new ArrayList<Transaction>();
    private double totalPoints;


    protected User() { //required for serializable
    }



    public User(String name, double points) {

        this.name = name;
        this.totalPoints = points;

    }

    public void setId(Long id) { this.id = id; }

    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public List<Transaction> getTransactionList() {

        return transactionList;

    }


    public double getTotalPoints() { return totalPoints;  }


    public void setName(String name) {
        this.name = name;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }


    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }



    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", transactionList="
                + transactionList + ", totalPoints=" + totalPoints + "]";
    }


}
