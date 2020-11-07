package com.tmgreyhat.esbobi.controllers;

import com.tmgreyhat.esbobi.mappers.RCMapper;
import com.tmgreyhat.esbobi.models.RCPT101;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Controller
@Slf4j
public class TESTG {

    @Autowired
    JdbcTemplate jdbcTemplate;


    DBCON dbcon = new DBCON();
    @GetMapping("/obi-test")
    private void mockUpload(){



        List<RCPT101> rcpt101List;
        rcpt101List=  jdbcTemplate.query("select * FROM RCPT101   ", new RCMapper());

        String feetype, binprefix;

        try {


          log.info("  heeeeeeeeeeeeeeeeeeeey "+dbcon.getConnection());

            Statement statement = dbcon.getConnection().createStatement();

            String sql = "SELECT * FROM AUTOPAYMENT";


            ResultSet resultSet = statement.executeQuery(sql);


            while (resultSet.next()){

                log.info("Hey we have "+ resultSet.getString(1));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /*rcpt101List.forEach(rcpt101 ->


                log.info("WE got "+rcpt101.getREFERENCE_REF())
                log.info("WE got "+rcpt101.getREFERENCE_REF())

        );*/
    }
}
