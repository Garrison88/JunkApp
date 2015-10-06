package com.garrisonthomas.junkapp.ParseObjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("DailyJournal")

public class DailyJournal extends ParseObject {

    public String getCrew() {

        return getString("crew");

    }

    public int getTruckNumber() {

        return getInt("truckNumber");

    }

    public void setDate(String date) {

        put("date", date);

    }

    public void setCrew(String crew) {

        put("crew", crew);

    }

    public void setTruckNumber(int truckNumber) {

        put("truckNumber", truckNumber);

    }

    @Override
    public String toString() {
        return getString("ssid");
    }
}