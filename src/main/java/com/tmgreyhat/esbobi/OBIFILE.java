package com.tmgreyhat.esbobi;

import java.util.Date;

public class OBIFILE {

    private long ID;
    private String FILENAME;

    private java.util.Date UPLOADTIME;

    public Date getUPLOADTIME() {
        return UPLOADTIME;
    }

    public void setUPLOADTIME(Date UPLOADTIME) {
        this.UPLOADTIME = UPLOADTIME;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME;
    }
}

