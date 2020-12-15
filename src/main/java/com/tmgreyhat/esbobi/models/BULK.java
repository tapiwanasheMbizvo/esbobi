package com.tmgreyhat.esbobi.models;

import java.math.BigInteger;

public class BULK {

     //log.info("  FEE TYPE "+bulkedset.getString(1)+ "  count "+ bulkedset.getInt(2)+ " sum  is "+ bulkedset.getInt(3));

    private String feetype;
    private BigInteger count;
    private BigInteger sum;
    private  String responseCode;
    private  String eqMessage;
    private  String pcref;

    private String  creditAcc;
    private String  debitAcc;
    private String retrevial_ref;

    private  BigInteger id;


    public String getRetrevial_ref() {
        return retrevial_ref;
    }

    public void setRetrevial_ref(String retrevial_ref) {
        this.retrevial_ref = retrevial_ref;
    }

    public String getCreditAcc() {
        return creditAcc;
    }

    public void setCreditAcc(String creditAcc) {
        this.creditAcc = creditAcc;
    }

    public String getDebitAcc() {
        return debitAcc;
    }

    public void setDebitAcc(String debitAcc) {
        this.debitAcc = debitAcc;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getEqMessage() {
        return eqMessage;
    }

    public void setEqMessage(String eqMessage) {
        this.eqMessage = eqMessage;
    }

    public String getPcref() {
        return pcref;
    }

    public void setPcref(String pcref) {
        this.pcref = pcref;
    }

    public String getFeetype() {
        return feetype;
    }

    public void setFeetype(String feetype) {
        this.feetype = feetype;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public BigInteger getSum() {
        return sum;
    }

    public void setSum(BigInteger sum) {
        this.sum = sum;
    }
}
