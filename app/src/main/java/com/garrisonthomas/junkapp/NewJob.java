package com.garrisonthomas.junkapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewJob")
public class NewJob extends ParseObject {

    public int getSSID() {

        return getInt("ssid");

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

    public void setGrossSale(double grossSale) {

        put("grossSale", grossSale);

    }

    public void setNetSale(double netSale) {

        put("netSale", netSale);

    }

    public void setReceiptNumber(int receiptNumber) {

        put("receiptNumber", receiptNumber);

    }

    @Override
    public String toString() {
        return getString("word");
    }
}