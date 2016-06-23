package com.garrisonthomas.junkapp.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewFuel")

public class NewFuel extends ParseObject {

    public String getFuelVendor() {

        return getString("fuelVendor");

    }

    public double getFuelNetCost() {

        return getDouble("fuelNetCost");

    }

    public String getFuelReceiptNumber() {

        return getString("fuelReceiptNumber");

    }

    public String getRelatedJournal() {

        return getString("relatedJournal");

    }

    public void setFuelVendor(String fuelVendor) {

        put("fuelVendor", fuelVendor);

    }

    public void setFuelNetCost(double fuelNetCost) {

        put("fuelNetCost", fuelNetCost);

    }

    public void setFuelReceiptNumber(String fuelReceiptNumber) {

        put("fuelReceiptNumber", fuelReceiptNumber);

    }

    public void setRelatedJournal(String relatedJournal) {

        put("relatedJournal", relatedJournal);

    }
}