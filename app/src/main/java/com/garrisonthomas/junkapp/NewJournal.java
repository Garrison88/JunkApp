package com.garrisonthomas.junkapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewJournal")
public class NewJournal extends ParseObject {

    public String getDate() {

        return getString("date");

    }

    public double getGrossSale() {

        return getDouble("grossSale");

    }

    public double getNetSale() {

        return getDouble("netSale");

    }

    public int getReceiptNumber() {

        return getInt("receiptNumber");

    }

    public void setSSID(int ssid) {

        put("ssid", ssid);

    }

    @Override
    public String toString() {
        return getString("word");
    }
}