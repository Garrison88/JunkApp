package com.garrisonthomas.junkapp.entryobjects;

/**
 * Created by Garrison on 2016-09-05.
 */
public class RebateObject {

    private String rebateLocation, materialType;
    private int rebateWeight, receiptNumber;
    private double rebateAmount;

    public RebateObject() {

        // empty default constructor, necessary for Firebase to be able to deserialize
    }

    public String getRebateLocation() {
        return rebateLocation;
    }

    public void setRebateLocation(String rebateLocation) {
        this.rebateLocation = rebateLocation;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public int getRebateWeight() {
        return rebateWeight;
    }

    public void setRebateWeight(int rebateWeight) {
        this.rebateWeight = rebateWeight;
    }

    public double getRebateAmount() {
        return rebateAmount;
    }

    public void setRebateAmount(double rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    public int getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(int receiptNumber) {
        this.receiptNumber = receiptNumber;
    }
}
