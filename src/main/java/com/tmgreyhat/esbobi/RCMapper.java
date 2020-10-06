package com.tmgreyhat.esbobi;

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
