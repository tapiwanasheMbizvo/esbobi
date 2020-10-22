package com.tmgreyhat.esbobi;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OBIFILEResultSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        OBIFILE obifile = new OBIFILE();
        //rcpt101.setID(BigInteger.valueOf(resultSet.getLong("ID")));
        obifile.setFILENAME(resultSet.getString("FILENAME"));
        obifile.setUPLOADTIME(resultSet.getDate("UPLOADTIME"));
        obifile.setID(resultSet.getLong("ID"));

        return  obifile;
    }
}
