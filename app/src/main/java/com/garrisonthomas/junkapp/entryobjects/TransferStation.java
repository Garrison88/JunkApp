package com.garrisonthomas.junkapp.entryobjects;

/**
 * Created by Garrison on 2016-08-24.
 */

//@JsonIgnoreProperties(ignoreUnknown = true)

public class TransferStation {

    private String address, info, name;
    private int minimum, rate;
    private long phoneNumber;

    public TransferStation() {

        // empty default constructor, necessary for Firebase to be able to deserialize

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }
}
