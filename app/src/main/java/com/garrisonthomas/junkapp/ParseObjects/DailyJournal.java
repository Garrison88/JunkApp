package com.garrisonthomas.junkapp.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("DailyJournal")

public class DailyJournal extends ParseObject {

//    public String getJournalID() {
//
//        return getString("journalID");
//
//    }

    public String getDriver() {

        return getString("driver");

    }

    public String getDriverStartTime() {

        return getString("driverStartTime");

    }

    public String getDriverEndTime() {

        return getString("driverEndTime");

    }

    public String getNavigator() {

        return getString("navigator");

    }

    public String getNavStartTime() {

        return getString("navStartTime");

    }

    public String getNavEndTime() {

        return getString("navEndTime");

    }

    public int getTruckNumber() {

        return getInt("truckNumber");

    }

    public String getEndOfDayNotes() {

        return getString("endOfDayNotes");
    }

    public int getPercentOfGoal() {

        return getInt("percentOfGoal");

    }

    public int getPercentOnDump() {

        return getInt("percentOnDump");

    }

    public boolean getArchived() {

        return getBoolean("archived");

    }

//    public void setJournalID(String journalID) {
//
//        put("journalID", journalID);
//
//    }

    public void setDate(String date) {

        put("date", date);

    }

    public void setDriver(String driver) {

        put("driver", driver);

    }

    public void setDriverStartTime(String driverStartTime) {

        put("driverStartTime", driverStartTime);

    }

    public void setDriverEndTime(String driverEndTime) {

        put("driverEndTime", driverEndTime);

    }

    public void setNavigator(String navigator) {

        put("navigator", navigator);

    }

    public void setNavStartTime(String navStartTime) {

        put("navStartTime", navStartTime);

    }

    public void setNavEndTime(String navEndTime) {

        put("navEndTime", navEndTime);

    }

    public void setEndPhoto(ParseFile endPhoto) {

        put("endPhoto", endPhoto);

    }

    public void setTruckNumber(int truckNumber) {

        put("truckNumber", truckNumber);

    }

    public void setEndOfDayNotes(String endOfDayNotes) {

        put("endOfDayNotes", endOfDayNotes);

    }

    public void setPercentOfGoal(int percentOfGoal) {

        put("percentOfGoal", percentOfGoal);

    }

    public void setPercentOnDump(int percentOnDump) {

        put("percentOnDump", percentOnDump);

    }

    public void setArchived(boolean archived) {

        put("archived", archived);

    }
}