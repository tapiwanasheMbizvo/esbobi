package com.tmgreyhat.esbobi;

import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@javax.persistence.Table(name = "EQTRANSFER",schema = "DB2INST1")
public class OBIFILE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ID;
    private String FILENAME;

    @Generated(GenerationTime.INSERT)
    private  java.util.Date UPLOADEDON;


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

    public Date getUPLOADEDON() {
        return UPLOADEDON;
    }

    public void setUPLOADEDON(Date UPLOADEDON) {
        this.UPLOADEDON = UPLOADEDON;
    }
}
