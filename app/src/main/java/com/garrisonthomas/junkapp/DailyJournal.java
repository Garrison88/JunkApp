package com.garrisonthomas.junkapp;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("DailyJournal")
public class DailyJournal extends ParseObject {

    public String getCrew() {

        return getString("crew");

    }

    public String getTruckNumber() {

        return getString("truckNumber");

    }

    public List<String> getJobs() {

        return getList("jobs");

    }

    public void setCrew(String crew) {

        put("crew", crew);

    }

    public void setJobs(List<String> jobs) {

        put("jobs", jobs);

    }



    public void setTruckNumber(String truckNumber) {

        put("truckNumber", truckNumber);

    }

    @Override
    public String toString() {
        return getString("ssid");
    }
}