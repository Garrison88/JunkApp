package com.garrisonthomas.junkapp.entryobjects;

/**
 * Created by Garrison on 2016-08-08.
 */
public class DumpObject {

    private String dumpName;
    private double grossCost;
    private float tonnage;
    private int dumpReceiptNumber, percentPrevious;

    public String getDumpName() {
        return dumpName;
    }

    public void setDumpName(String dumpName) {
        this.dumpName = dumpName;
    }

    public double getGrossCost() {
        return grossCost;
    }

    public void setGrossCost(double grossCost) {
        this.grossCost = grossCost;
    }

    public float getTonnage() {
        return tonnage;
    }

    public void setTonnage(float tonnage) {
        this.tonnage = tonnage;
    }

    public int getDumpReceiptNumber() {
        return dumpReceiptNumber;
    }

    public void setDumpReceiptNumber(int dumpReceiptNumber) {
        this.dumpReceiptNumber = dumpReceiptNumber;
    }

    public int getPercentPrevious() {
        return percentPrevious;
    }

    public void setPercentPrevious(int percentPrevious) {
        if(percentPrevious != 0) {
            this.percentPrevious = percentPrevious;
        }
    }
}
