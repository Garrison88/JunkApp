package com.garrisonthomas.junkapp.entryobjects;

/**
 * Created by Garrison on 2016-08-06.
 */

public class JobObject {

    private int SID, receiptNumber;
    private double grossSale, netSale;
    private String startTime;
    private String endTime;
    private String payType;
    private String jobNotes;

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    private String jobType;

    public JobObject() {

        // empty default constructor, necessary for Firebase to be able to deserialize
    }

    public int getSID() {

        return SID;

    }

    public double getGrossSale() {

        return grossSale;

    }

    public double getNetSale() {

        return netSale;

    }

    public String getStartTime() {

        return startTime;

    }

    public String getEndTime() {

        return endTime;

    }

    public int getReceiptNumber() {

        return receiptNumber;

    }

    public String getPayType() {

        return payType;

    }

    public String getJobNotes() {

        return jobNotes;

    }

    public void setSID(int SID) {
        this.SID = SID;
    }

    public void setGrossSale(double grossSale) {

        this.grossSale = grossSale;

    }


    public void setNetSale(double netSale) {

        this.netSale = netSale;

    }

    public void setStartTime(String startTime) {

        this.startTime = startTime;

    }

    public void setEndTime(String endTime) {

        this.endTime = endTime;

    }

    public void setReceiptNumber(int receiptNumber) {

        this.receiptNumber = receiptNumber;

    }

    public void setPayType(String payType) {

        this.payType = payType;

    }

    public void setJobNotes(String jobNotes) {

        if (!jobNotes.equals("")) {
            this.jobNotes = jobNotes;
        }

    }

}
