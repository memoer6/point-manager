package com.memoer6.pointreader.model;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction implements Serializable {

    private Float value;
    private Date date;
    private String description;


    protected Transaction() { //jpa only

    }

    public Transaction(Float value, Date date, String description) {

        this.value = value;
        this.date = date;
        this.description = description;
    }


    public String getValue() {

        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(value);
    }

    public String getDate() {

        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date);

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
        return "Transaction [value=" + this.getValue()
                + ", date=" + this.getDate() + ", description=" + description + "]";
    }

}
