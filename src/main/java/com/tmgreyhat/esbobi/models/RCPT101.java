package com.tmgreyhat.esbobi.models;

import java.math.BigInteger;

public class RCPT101 {

    private BigInteger ID;
    private String REFERENCE_REF;
    private String ACCOUNT_NUMBER;
    private String EFFECTIVE_DATE;
    private String FRONT_END_TRANSACTION_TYPE;
    private String TRANSACTION_PLACE;
    private String ISO_TERMINAL_ID;
    private String ISO_MERCHANT_ID;
    private String RETRIEVAL_REFERENCE_NUMBER;
    private String EQ_RESPONSE;
    private String AMOUNT;

    public RCPT101() {
    }

    public String getACCOUNT_NUMBER() {
        return ACCOUNT_NUMBER;
    }

    public void setACCOUNT_NUMBER(String ACCOUNT_NUMBER) {
        this.ACCOUNT_NUMBER = ACCOUNT_NUMBER;
    }

    public String getEFFECTIVE_DATE() {
        return EFFECTIVE_DATE;
    }

    public void setEFFECTIVE_DATE(String EFFECTIVE_DATE) {
        this.EFFECTIVE_DATE = EFFECTIVE_DATE;
    }

    public String getFRONT_END_TRANSACTION_TYPE() {
        return FRONT_END_TRANSACTION_TYPE;
    }

    public void setFRONT_END_TRANSACTION_TYPE(String FRONT_END_TRANSACTION_TYPE) {
        this.FRONT_END_TRANSACTION_TYPE = FRONT_END_TRANSACTION_TYPE;
    }

    public String getTRANSACTION_PLACE() {
        return TRANSACTION_PLACE;
    }

    public void setTRANSACTION_PLACE(String TRANSACTION_PLACE) {
        this.TRANSACTION_PLACE = TRANSACTION_PLACE;
    }

    public String getISO_TERMINAL_ID() {
        return ISO_TERMINAL_ID;
    }

    public void setISO_TERMINAL_ID(String ISO_TERMINAL_ID) {
        this.ISO_TERMINAL_ID = ISO_TERMINAL_ID;
    }

    public String getISO_MERCHANT_ID() {
        return ISO_MERCHANT_ID;
    }

    public void setISO_MERCHANT_ID(String ISO_MERCHANT_ID) {
        this.ISO_MERCHANT_ID = ISO_MERCHANT_ID;
    }

    public String getRETRIEVAL_REFERENCE_NUMBER() {
        return RETRIEVAL_REFERENCE_NUMBER;
    }

    public void setRETRIEVAL_REFERENCE_NUMBER(String RETRIEVAL_REFERENCE_NUMBER) {
        this.RETRIEVAL_REFERENCE_NUMBER = RETRIEVAL_REFERENCE_NUMBER;
    }

    public String getEQ_RESPONSE() {
        return EQ_RESPONSE;
    }

    public String computeResponse(){


        if(getEQ_RESPONSE().isEmpty()){

            return "FAILED";
        }else {

            if(getEQ_RESPONSE().contains("success")){

                return  "SUCCESS";
            }else{


                return "FAILED";
            }
        }

    }

    public void setEQ_RESPONSE(String EQ_RESPONSE) {
        this.EQ_RESPONSE = EQ_RESPONSE;
    }

    public BigInteger getID() {
        return ID;
    }

    public void setID(BigInteger ID) {
        this.ID = ID;
    }

    public String getREFERENCE_REF() {
        return REFERENCE_REF;
    }

    public void setREFERENCE_REF(String REFERENCE_REF) {
        this.REFERENCE_REF = REFERENCE_REF;
    }

    public String getAMOUNT() {

        return String.valueOf(Integer.valueOf(AMOUNT)/100);
    }

    public void setAMOUNT(String AMOUNT) {
        this.AMOUNT = AMOUNT;
    }
}
