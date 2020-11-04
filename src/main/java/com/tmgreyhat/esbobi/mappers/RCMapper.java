package com.tmgreyhat.esbobi.mappers;

import com.tmgreyhat.esbobi.ResultExtractors.RCPT101ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RCMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        RCPT101ResultSetExtractor extractor= new RCPT101ResultSetExtractor() ;
        return  extractor.extractData(resultSet);
    }
}
