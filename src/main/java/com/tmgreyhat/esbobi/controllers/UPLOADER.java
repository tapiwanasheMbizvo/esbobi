package com.tmgreyhat.esbobi.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class UPLOADER {


    private String file_source_dr = "C:\\ESB\\upload\\excels\\";



    @Autowired
    JdbcTemplate jdbcTemplate;



    @GetMapping("/terminals-bulk")
    public  void bulkTerminal(){


        String file_name ="Terminals.xls";

        FileInputStream fileInputStream = null;

        try{
            fileInputStream = new FileInputStream(new File(file_source_dr+file_name));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try{

            Workbook workbook = new HSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet){
                if(row.getRowNum()>=1924){
                    DataFormatter formatter = new DataFormatter();
                    String TERMINALID,	ACCOUNTNO,APPLYDISCOUNT 	,DISCOUNT,	IPADDRESS,	MAXIMUMTXNAMOUNT,	NAME,	POSTINGBRANCH,	SOURCE,	STATUS,	TERMINALREPLYQUEUE,	TERMINALSOURCE_SOURCE,	USDACCOUNTNO;
                    TERMINALID = row.getCell(0).getStringCellValue();
                    ACCOUNTNO = row.getCell(1).getStringCellValue();
                    DISCOUNT = formatter.formatCellValue(row.getCell(1));
                    APPLYDISCOUNT = formatter.formatCellValue(row.getCell(1));
                    IPADDRESS = row.getCell(4).getStringCellValue();
                    MAXIMUMTXNAMOUNT = row.getCell(5).getStringCellValue();
                    NAME = row.getCell(6).getStringCellValue();
                    POSTINGBRANCH = row.getCell(7).getStringCellValue();
                    SOURCE = row.getCell(8).getStringCellValue();
                    STATUS = row.getCell(9).getStringCellValue();
                    TERMINALREPLYQUEUE = row.getCell(10).getStringCellValue();
                    TERMINALSOURCE_SOURCE = row.getCell(11).getStringCellValue();
                    USDACCOUNTNO = row.getCell(12).getStringCellValue();
                    log.info("Terminal ID "+TERMINALID+" ACCOUNT NU "+ACCOUNTNO);

                    String sql = "insert into TERMINAL (TERMINALID, ACCOUNTNO, IPADDRESS, MAXIMUMTXNAMOUNT, NAME, POSTINGBRANCH, SOURCE, STATUS, TERMINALREPLYQUEUE, TERMINALSOURCE_SOURCE, USDACCOUNTNO)\n" +
                            "values ('"+TERMINALID+"', '"+ACCOUNTNO+"', '"+IPADDRESS+"', '"+MAXIMUMTXNAMOUNT+"', '"+NAME+"', '"+POSTINGBRANCH+"', '"+SOURCE+"', '"+STATUS+"', '"+TERMINALREPLYQUEUE+"', '"+TERMINALSOURCE_SOURCE+"', '"+USDACCOUNTNO+"')";


                    jdbcTemplate.execute(sql);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @GetMapping("/file-uploader")
    public void postUploader(){

String file_name = "OBICharge281020.xls";
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(file_source_dr+file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Workbook workbook = new HSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            Map<Integer, List<String>> data = new HashMap<>();
            int i = 0;
            for (Row row : sheet) {

                if(row.getRowNum()!=0){

                    //this row represent a whole charge object

                    String FEEID, BINPRIFIX, CHARGECODE, CREDITCODE,DEBITCODE, DESCRIPTION, DRCR, GLACCOUNT, SOURCEACCOUNT, GLACCOUNTUSD;

                    FEEID = row.getCell(0).getStringCellValue();
                    BINPRIFIX = row.getCell(1).getStringCellValue();
                    CHARGECODE = row.getCell(2).getStringCellValue();
                    CREDITCODE = row.getCell(3).getStringCellValue();
                    DEBITCODE = row.getCell(4).getStringCellValue();
                    DESCRIPTION = row.getCell(5).getStringCellValue();
                    DRCR = row.getCell(6).getStringCellValue();
                    GLACCOUNT = row.getCell(7).getStringCellValue();
                    SOURCEACCOUNT = row.getCell(8).getStringCellValue();
                    GLACCOUNTUSD = row.getCell(9).getStringCellValue();

                    String sql = "insert into OBICHARGE (FEEID, BINPRIFIX, CHARGECODE, CREDITCODE, DEBITCODE, DESCRIPTION, DRCR, GLACCOUNT, SOURCEACCOUNT, GLACCOUNTUSD)\n" +
                            "values ('"+FEEID+"', '"+BINPRIFIX+"', '"+CHARGECODE+"', '"+CREDITCODE+"', '"+DEBITCODE+"', '"+DESCRIPTION+"', '"+DRCR+"', '"+GLACCOUNT+"', '"+SOURCEACCOUNT+"', '"+GLACCOUNTUSD+"')";

                            //jdbcTemplate.execute(sql);

                    log.info("FOUND "+FEEID+" "+BINPRIFIX+" "+CHARGECODE);
                }
                /*data.put(i, new ArrayList<String>());
                for (Cell cell : row) {

                    log.info("FOUND "+cell.getStringCellValue());
                }
                i++;*/
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
