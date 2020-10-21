package com.tmgreyhat.esbobi;

import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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



}
