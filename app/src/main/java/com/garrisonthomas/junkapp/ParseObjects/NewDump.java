package com.garrisonthomas.junkapp.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewDump")

public class NewDump extends ParseObject {

    public String getDumpName() {

        return getString("dumpName");

    }

    public double getGrossCost() {

        return getDouble("grossCost");

    }

    public double getNetCost() {

        return getDouble("netCost");

    }

    public int getDumpReceiptNumber() {

        return getInt("dumpReceiptNumber");

    }

    public int getPercentPrevious() {

        return getInt("percentPrevious");

    }

    public String getRelatedJournal() {

        return getString("relatedJournal");

    }

    public void setDumpName(String dumpName) {

        put("dumpName", dumpName);

    }

    public void setGrossCost(double grossCost) {

        put("grossCost", grossCost);

    }

    public void setNetCost(double netCost) {

        put("netCost", netCost);

    }

    public void setDumpReceiptNumber(int dumpReceiptNumber) {

        put("dumpReceiptNumber", dumpReceiptNumber);

    }

    public void setPercentPrevious(int percentPrevious) {

        put("percentPrevious", percentPrevious);

    }

    public void setRelatedJournal(String relatedJournal) {

        put("relatedJournal", relatedJournal);

    }
}