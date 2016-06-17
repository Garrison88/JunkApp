package com.garrisonthomas.junkapp.parseobjects;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("NewQuote")

public class NewQuote extends ParseObject {

    public int getQuoteSID() {

        return getInt("quoteSID");

    }

    public int getLowEnd() {

        return getInt("lowEnd");

    }

    public int getHighEnd() {

        return getInt("highEnd");

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

    public void setLowEnd(int lowEnd) {

        put("lowEnd", lowEnd);

    }

    public void setHighEnd(int highEnd) {

        put("highEnd", highEnd);

    }

    public void setQuoteNotes(String quoteNotes) {

        put("quoteNotes", quoteNotes);

    }

    public void setRelatedJournal(String relatedJournal) {

        put("relatedJournal", relatedJournal);

    }

}