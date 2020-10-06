package com.tmgreyhat.esbobi;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;


public class RCPT101ResultSetExtractor implements ResultSetExtractor {

    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        RCPT101 rcpt101 = new RCPT101();

        rcpt101.setID(BigInteger.valueOf(resultSet.getLong("ID")));
        rcpt101.setREFERENCE_REF(resultSet.getString("REFERENCE_REF"));
        rcpt101.setACCOUNT_NUMBER(resultSet.getString("ACCOUNT_NUMBER"));
        rcpt101.setEFFECTIVE_DATE(resultSet.getString("EFFECTIVE_DATE"));
        rcpt101.setFRONT_END_TRANSACTION_TYPE(resultSet.getString("FRONT_END_TRANSACTION_TYPE"));
        rcpt101.setTRANSACTION_PLACE(resultSet.getString("TRANSACTION_PLACE"));
        rcpt101.setISO_TERMINAL_ID(resultSet.getString("ISO_TERMINAL_ID"));
        rcpt101.setISO_MERCHANT_ID(resultSet.getString("ISO_MERCHANT_ID"));
        rcpt101.setRETRIEVAL_REFERENCE_NUMBER(resultSet.getString("RETRIEVAL_REFERENCE_NUMBER"));
        rcpt101.setEQ_RESPONSE(resultSet.getString("EQ_RESPONSE"));


        return  rcpt101;

    }
}
