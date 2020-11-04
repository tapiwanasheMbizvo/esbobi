package com.tmgreyhat.esbobi.ResultExtractors;

import com.tmgreyhat.esbobi.models.AUTOPAY;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AUTOPAYResultSetExtractor implements ResultSetExtractor {

    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        AUTOPAY autopay = new AUTOPAY();

        autopay.setCBSACCOUNT(resultSet.getString("CBSACCOUNT"));
        autopay.setAVAILABLELIMIT(resultSet.getString("AVAILABLELIMIT"));
        autopay.setCARDEXPIRYDATE(resultSet.getString("CARDEXPIRYDATE"));
        autopay.setCARDNUMBER(resultSet.getString("CARDNUMBER"));
        autopay.setCARDSTATUS(resultSet.getString("CARDSTATUS"));
        autopay.setCREDITLIMIT(resultSet.getString("CREDITLIMIT"));
        autopay.setCURRENTBALANCE(resultSet.getString("CURRENTBALANCE"));
        autopay.setCUSTOMERNUMBER(resultSet.getString("CUSTOMERNUMBER"));
        autopay.setDATEOPENED(resultSet.getString("DATEOPENED"));
        autopay.setDUEDATE(resultSet.getString("DUEDATE"));
        autopay.setLASTSTAMENTDATE(resultSet.getString("LASTSTAMENTDATE"));
        autopay.setOVERLIMIT(resultSet.getString("OVERLIMIT"));
        autopay.setPASTDUE(resultSet.getString("PASTDUE"));
        autopay.setPAYMENTDAYS(resultSet.getString("PAYMENTDAYS"));
        autopay.setSVBOACCOUNT(resultSet.getString("SVBOACCOUNT"));
        autopay.setTOTDUEPLUSLIMIT(resultSet.getString("TOTDUEPLUSLIMIT"));
        autopay.setTOTALAMOUNTDUE(resultSet.getString("TOTALAMOUNTDUE"));
        autopay.setTOTALDUEALLDAYS(resultSet.getString("TOTALDUEALLDAYS"));


        return  autopay;
    }
}
