package com.tmgreyhat.esbobi.ResultExtractors;

import com.tmgreyhat.esbobi.models.OBIFILE;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OBIFILEResultSetExtractor implements ResultSetExtractor {
    @Override
    public Object extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        OBIFILE obifile = new OBIFILE();

        obifile.setFILENAME(resultSet.getString("FILENAME"));
        obifile.setUPLOADTIME(resultSet.getTimestamp("UPLOADTIME"));
        obifile.setID(resultSet.getLong("ID"));
        obifile.setPOSTEDON(resultSet.getTimestamp("POSTEDON"));
        obifile.setUPLOADEDBY(resultSet.getLong("UPLOADEDBY"));
        obifile.setPOSTEDBY(resultSet.getLong("POSTEDBY"));
        obifile.setStatus(resultSet.getString("STATUS"));
        obifile.setFILE_TYPE(resultSet.getString("FILE_TYPE"));

        return  obifile;
    }
}
