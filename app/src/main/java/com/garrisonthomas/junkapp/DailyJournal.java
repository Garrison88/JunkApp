package com.garrisonthomas.junkapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("DailyJournal")

public class DailyJournal extends ParseObject {

    public String getCrew() {

        return getString("crew");

    }

    public String getTruckNumber() {

        return getString("truckNumber");

    }

    public void setDate(String date) {

        put("date", date);

    }

    public void setCrew(String crew) {

        put("crew", crew);

    }

    public void setTruckNumber(String truckNumber) {

        put("truckNumber", truckNumber);

    }

    @Override
    public String toString() {
        return getString("ssid");
    }
}