package com.garrisonthomas.junkapp.parseobjects;

/**
 * Created by Garrison on 2016-08-07.
 */
public class DailyJournalObject {

    private String driver, driverStartTime, driverEndTime, navigator, navStartTime, navEndTime,
            endOfDayNotes, date, truckNumber;
    private int percentOfGoal, percentOnDump;
    private boolean isArchived;

    public DailyJournalObject() {

        // empty default constructor, necessary for Firebase to be able to deserialize

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverStartTime() {
        return driverStartTime;
    }

    public void setDriverStartTime(String driverStartTime) {
        this.driverStartTime = driverStartTime;
    }

    public String getDriverEndTime() {
        return driverEndTime;
    }

    public void setDriverEndTime(String driverEndTime) {
        this.driverEndTime = driverEndTime;
    }

    public String getNavigator() {
        return navigator;
    }

    public void setNavigator(String navigator) {
        this.navigator = navigator;
    }

    public String getNavStartTime() {
        return navStartTime;
    }

    public void setNavStartTime(String navStartTime) {
        this.navStartTime = navStartTime;
    }

    public String getNavEndTime() {
        return navEndTime;
    }

    public void setNavEndTime(String navEndTime) {
        this.navEndTime = navEndTime;
    }

    public String getEndOfDayNotes() {
        return endOfDayNotes;
    }

    public void setEndOfDayNotes(String endOfDayNotes) {
        this.endOfDayNotes = endOfDayNotes;
    }

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public int getPercentOfGoal() {
        return percentOfGoal;
    }

    public void setPercentOfGoal(int percentOfGoal) {
        this.percentOfGoal = percentOfGoal;
    }

    public int getPercentOnDump() {
        return percentOnDump;
    }

    public void setPercentOnDump(int percentOnDump) {
        this.percentOnDump = percentOnDump;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

}
