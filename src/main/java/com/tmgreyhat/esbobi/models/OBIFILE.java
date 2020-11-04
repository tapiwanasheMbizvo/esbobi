package com.tmgreyhat.esbobi.models;

import java.sql.Timestamp;
import java.util.Date;

public class OBIFILE {

    private long ID;
    private String FILENAME;
    private String FILE_TYPE;

    private Timestamp UPLOADTIME;

    private Timestamp POSTEDON;
    private Long POSTEDBY;
    private Long UPLOADEDBY;

    private long count;
    private String status;
    private double value;


    public String getFILE_TYPE() {
        return FILE_TYPE;
    }

    public void setFILE_TYPE(String FILE_TYPE) {
        this.FILE_TYPE = FILE_TYPE;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCount() {
        return count;
    }

    public void setUPLOADTIME(Timestamp UPLOADTIME) {
        this.UPLOADTIME = UPLOADTIME;
    }

    public Timestamp getPOSTEDON() {
        return POSTEDON;
    }

    public void setPOSTEDON(Timestamp POSTEDON) {
        this.POSTEDON = POSTEDON;
    }

    public Long getPOSTEDBY() {
        return POSTEDBY;
    }

    public void setPOSTEDBY(Long POSTEDBY) {
        this.POSTEDBY = POSTEDBY;
    }

    public Long getUPLOADEDBY() {
        return UPLOADEDBY;
    }

    public void setUPLOADEDBY(Long UPLOADEDBY) {
        this.UPLOADEDBY = UPLOADEDBY;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getUPLOADTIME() {
        return UPLOADTIME;
    }

/*    public void setUPLOADTIME(Timestamp UPLOADTIME) {
        this.UPLOADTIME = UPLOADTIME;
    }*/

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


    public OBIFILE(){}
}

