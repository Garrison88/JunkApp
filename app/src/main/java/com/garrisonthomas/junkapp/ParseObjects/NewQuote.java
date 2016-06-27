package com.garrisonthomas.junkapp.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewQuote")

public class NewQuote extends ParseObject {

    public int getQuoteSID() {

        return getInt("quoteSID");

    }

    public double getLowEnd() {

        return getDouble("lowEnd");

    }

    public double getHighEnd() {

        return getDouble("highEnd");

    }

    public String getQuoteStartTime() {

        return getString("quoteStartTime");

    }

    public String getQuoteEndTime() {

        return getString("quoteEndTime");

    }

    public String getQuoteNotes() {

        return getString("quoteNotes");

    }

    public String getRelatedJournal() {

        return getString("relatedJournal");

    }

    public void setQuoteSID(int quoteSID) {

        put("quoteSID", quoteSID);

    }

    public void setLowEnd(double lowEnd) {

        put("lowEnd", lowEnd);

    }

    public void setHighEnd(double highEnd) {

        put("highEnd", highEnd);

    }

    public void setQuoteStartTime(String quoteStartTime) {

        put("quoteStartTime", quoteStartTime);

    }

    public void setQuoteEndTime(String quoteEndTime) {

        put("quoteEndTime", quoteEndTime);

    }

    public void setQuoteNotes(String quoteNotes) {

        put("quoteNotes", quoteNotes);

    }

    public void setRelatedJournal(String relatedJournal) {

        put("relatedJournal", relatedJournal);

    }

}