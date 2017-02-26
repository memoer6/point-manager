package com.memoer6.pointreader4.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable {


    private Long id;
    private String name;
    private List<Transaction> transactionList = new ArrayList<Transaction>();
    private double totalPoints;

    protected User() { //jpa only
    }


    public User(String name, double points) {

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


    public double getTotalPoints() { return totalPoints;  }


    public void setName(String name) {
        this.name = name;
    }


    public void setTotalPoints(double totalPoints) {
        this.totalPoints = totalPoints;
    }



    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", transactionList="
                + transactionList + ", totalPoints=" + totalPoints + "]";
    }

    //********** Methods to support Parcelable ***************

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeTypedList(transactionList);
        parcel.writeDouble(totalPoints);

    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private User(Parcel in) {
        id = in.readLong();
        name = in.readString();
        in.readTypedList(transactionList, Transaction.CREATOR);
        totalPoints = in.readDouble();
    }

    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<User> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}

