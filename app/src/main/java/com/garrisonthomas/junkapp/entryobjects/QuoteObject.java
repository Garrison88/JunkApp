package com.garrisonthomas.junkapp.entryobjects;

/**
 * Created by Garrison on 2016-08-09.
 */
public class QuoteObject {

    private int quoteSID;
    private double lowEnd, highEnd;
    private String quoteStartTime, quoteEndTime, quoteNotes;

    public int getQuoteSID() {
        return quoteSID;
    }

    public void setQuoteSID(int quoteSID) {
        this.quoteSID = quoteSID;
    }

    public double getLowEnd() {
        return lowEnd;
    }

    public void setLowEnd(double lowEnd) {
        this.lowEnd = lowEnd;
    }

    public double getHighEnd() {
        return highEnd;
    }

    public void setHighEnd(double highEnd) {
        if (highEnd != 0.0) {
            this.highEnd = highEnd;
        }
    }

    public String getQuoteStartTime() {
        return quoteStartTime;
    }

    public void setQuoteStartTime(String quoteStartTime) {
        this.quoteStartTime = quoteStartTime;
    }

    public String getQuoteEndTime() {
        return quoteEndTime;
    }

    public void setQuoteEndTime(String quoteEndTime) {
        this.quoteEndTime = quoteEndTime;
    }

    public String getQuoteNotes() {
        return quoteNotes;
    }

    public void setQuoteNotes(String quoteNotes) {
        if (!quoteNotes.equals("")) {
            this.quoteNotes = quoteNotes;
        }
    }
}
