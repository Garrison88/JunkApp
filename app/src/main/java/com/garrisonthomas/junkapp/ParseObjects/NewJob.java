package com.garrisonthomas.junkapp.ParseObjects;

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

    public String getPayType() {

        return getString("payType");

    }

    public String getJobNotes() {

        return getString("jobNotes");

    }

    public String getRelatedJournal() {

        return getString("relatedJournal");

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

    public void setPayType(String payType) {

        put("payType", payType);

    }

    public void setJobNotes(String jobNotes) {

        put("jobNotes", jobNotes);

    }

    public void setRelatedJournal(String relatedJournal) {

        put("relatedJournal", relatedJournal);

    }

}