package com.tmgreyhat.esbobi.mappers;

import com.tmgreyhat.esbobi.ResultExtractors.AUTOPAYResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AUTOPAYMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        AUTOPAYResultSetExtractor resultSetExtractor = new AUTOPAYResultSetExtractor();
        return resultSetExtractor.extractData(resultSet);
    }
}
