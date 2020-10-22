package com.tmgreyhat.esbobi;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OBIFILEMapper  implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {

        OBIFILEResultSetExtractor extractor= new OBIFILEResultSetExtractor() ;
        return  extractor.extractData(resultSet);
    }
}
