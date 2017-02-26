package com.memoer6.pointreader4.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Transaction implements Parcelable {

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


    //********** Methods to support Parcelable ***************

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeFloat(value);
        parcel.writeLong(date.getTime());
        parcel.writeString(description);

    }

    // Using the `in` variable, we can retrieve the values that
    // we originally wrote into the `Parcel`.  This constructor is usually
    // private so that only the `CREATOR` field can access.
    private Transaction(Parcel in) {
        value = in.readFloat();
        date = new Date(in.readLong());
        description = in.readString();
    }

    // In the vast majority of cases you can simply return 0 for this.
    // There are cases where you need to use the constant `CONTENTS_FILE_DESCRIPTOR`
    // But this is out of scope of this tutorial
    @Override
    public int describeContents() {
        return 0;
    }

    // After implementing the `Parcelable` interface, we need to create the
    // `Parcelable.Creator<Transaction> CREATOR` constant for our class;
    // Notice how it has our class specified as its type.
    public static final Parcelable.Creator<Transaction> CREATOR
            = new Parcelable.Creator<Transaction>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

}

