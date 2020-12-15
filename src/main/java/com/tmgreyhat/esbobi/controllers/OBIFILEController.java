package com.tmgreyhat.esbobi.controllers;

import com.jcraft.jsch.*;
import com.tmgreyhat.esbobi.mappers.AUTOPAYMapper;
import com.tmgreyhat.esbobi.mappers.OBIFILEMapper;
import com.tmgreyhat.esbobi.mappers.RCMapper;
import com.tmgreyhat.esbobi.models.AUTOPAY;
import com.tmgreyhat.esbobi.models.BULK;
import com.tmgreyhat.esbobi.models.OBIFILE;
import com.tmgreyhat.esbobi.models.RCPT101;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.cs.StandardCharsets;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CoderMalfunctionError;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

@Controller
@Slf4j
public class OBIFILEController {


    private String remoteHost = "10.136.192.172";
    private String username = "root";
    private String password = "root";
    // private String hosts_dir = "C:\\Users\\gaswaj\\.ssh\\known_hosts";
    //  private String hosts_dir = "C:\\Users\\tapiwanashem\\.ssh\\known_hosts";
   // private String upload_dir = "C:\\ESB\\upload\\";
    private String upload_dir = "/home/ESBOBI/upload/";



    private String remoteDirOBI = "/var/ESBUPLOADS/OBI/";
    private String remoteDirAUTOPAY = "/var/ESBUPLOADS/AUTOPAY/";


    @Autowired
    JdbcTemplate jdbcTemplate;

    Connection connection = new DBCON().getConnection();



    @GetMapping("obi-files")
    String fileList(Model model){

        List<OBIFILE> obifiles;


        obifiles= jdbcTemplate.query("SELECT * FROM FILEUPLOADS WHERE FILE_TYPE = 'OBI'  ", new OBIFILEMapper());

        model.addAttribute("obifiles", obifiles);


        return "allfiles";
    }



//charge-history/OBI_20200317_120031_0002/FEEC320

    @GetMapping("/charge-history/{filename}/{feetype}")
    public String getChargeHistory(@PathVariable(name = "filename") String filename, @PathVariable(name = "feetype") String feetype, Model model){


        List<BULK> bulkList = new ArrayList<>();



        String sql = "select * from EQTRANSFER_OFFLINE where PCREF in (select right(REFERENCE_REF, 15) from RCPT101 where FILE_NAME='"+filename+"' and FRONT_END_TRANSACTION_TYPE='"+feetype+"')";
        String sql2 = "select * from EQTRANSFER_OFFLINE where GZNR2='"+filename+"' AND GZNR1='"+feetype+"'";


        try {

            Connection connection = new DBCON().getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);

            if(!resultSet.next()){

                resultSet = connection.createStatement().executeQuery(sql2);
            }
            while (resultSet.next()){
                BULK bulk = new BULK();


                bulk.setPcref(resultSet.getString("PCREF"));
                bulk.setEqMessage(resultSet.getString("MSGTXT"));
                bulk.setResponseCode(resultSet.getString("XRREC"));
                bulk.setCreditAcc(resultSet.getString("GZAB2")+resultSet.getString("GZAN2")+resultSet.getString("GZAS2") );
                bulk.setDebitAcc(resultSet.getString("GZAB1")+resultSet.getString("GZAN1")+resultSet.getString("GZAS1") );


                bulkList.add(bulk);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        model.addAttribute("chargeList", bulkList );
        return "charge_history";
    }

    @GetMapping("/file-history/{id}")
    public String getContents(@PathVariable(name = "id") Long id, Model model) throws SQLException {


        List<OBIFILE> obifileList;
        List<RCPT101> bulked;
        List<RCPT101> nonebulked;



        log.info("Searching for transactions in file  with id  "+id);



        obifileList=  jdbcTemplate.query("select * FROM FILEUPLOADS WHERE ID  ="+id+"  ", new OBIFILEMapper());


        if(obifileList.isEmpty()){

            model.addAttribute("msg", "NO FILES FOUND");
        }

        OBIFILE obifile = obifileList.get(0);

        String file_type = obifile.getFILE_TYPE();

        if (file_type.equalsIgnoreCase("OBI")){

            bulked =  jdbcTemplate.query("select * FROM RCPT101 WHERE FILE_NAME  ='"+obifile.getFILENAME()+" ' AND FRONT_END_TRANSACTION_TYPE IN (select CHARGECODE from OBICHARGE where IS_BULKED=1) ", new RCMapper());
            nonebulked =  jdbcTemplate.query("select * FROM RCPT101 WHERE FILE_NAME  ='"+obifile.getFILENAME()+" ' AND FRONT_END_TRANSACTION_TYPE IN (select CHARGECODE from OBICHARGE where IS_BULKED=0)", new RCMapper());

            //bulked = jdbcTemplate.query("select FRONT_END_TRANSACTION_TYPE, count(id) , SUM(CAST(AMOUNT AS INT)) from RCPT101 WHERE FILE_NAME ='"+obifile.getFILENAME()+"' AND FRONT_END_TRANSACTION_TYPE IN (SELECT CHARGECODE FROM OBICHARGE WHERE  IS_BULKED=1) group by FRONT_END_TRANSACTION_TYPE", new RCMapper());

            log.info("we got bulked "+ bulked.size());
            log.info("we got NONE  bulked "+ nonebulked.size());


            String bulkedSql = "select FRONT_END_TRANSACTION_TYPE, count(id) , SUM(CAST(AMOUNT AS INT)) from RCPT101 WHERE FILE_NAME ='"+obifile.getFILENAME()+"' AND FRONT_END_TRANSACTION_TYPE IN (SELECT CHARGECODE FROM OBICHARGE WHERE  IS_BULKED=1) group by FRONT_END_TRANSACTION_TYPE" ;
            String noneBulkedSql = "select FRONT_END_TRANSACTION_TYPE, count(id) , SUM(CAST(AMOUNT AS INT)) from RCPT101 WHERE FILE_NAME ='"+obifile.getFILENAME()+"' AND FRONT_END_TRANSACTION_TYPE IN (SELECT CHARGECODE FROM OBICHARGE WHERE  IS_BULKED=0) group by FRONT_END_TRANSACTION_TYPE" ;

            ResultSet bulkedset = null;
            ResultSet noneBulkedset  = null;

            try {

                Connection connection = new DBCON().getConnection();

                bulkedset = connection.createStatement().executeQuery(bulkedSql);
                noneBulkedset  = connection.createStatement().executeQuery(noneBulkedSql);


                List<BULK> bulkList = new ArrayList<>();
                List<BULK> noneBulkList = new ArrayList<>();
                while (bulkedset.next()){

                    BULK bulk = new BULK();

                    bulk.setCount(BigInteger.valueOf(bulkedset.getInt(2)));
                    bulk.setFeetype(bulkedset.getString(1));
                    bulk.setSum(BigInteger.valueOf(bulkedset.getInt(3)));

                    bulkList.add(bulk);

                    log.info("  FEE TYPE "+bulkedset.getString(1)+ "  count "+ bulkedset.getInt(2)+ " sum  is "+ bulkedset.getInt(3));
                }
                while (noneBulkedset.next()){

                    BULK bulk = new BULK();

                    bulk.setCount(BigInteger.valueOf(noneBulkedset.getInt(2)));
                    bulk.setFeetype(noneBulkedset.getString(1));
                    bulk.setSum(BigInteger.valueOf(noneBulkedset.getInt(3)));

                    noneBulkList.add(bulk);

                }
                model.addAttribute("bullked", bulked);
                model.addAttribute("nonebulked", nonebulked);
                model.addAttribute("bulkset", bulkList);
                model.addAttribute("noneBulkset", noneBulkList);
                model.addAttribute("fileName", obifileList.get(0).getFILENAME());
                return "fileHistory";

            }finally {
                connection.close();
            }



        }else {

            List<AUTOPAY> autopayList;

            autopayList = jdbcTemplate.query("SELECT * FROM AUTOPAYMENT WHERE FILENAME = '"+obifile.getFILENAME()+"'", new AUTOPAYMapper());

            model.addAttribute("autopays", autopayList);

            return "auto_history";
        }

    }

    @PostMapping("/upload")
    public String uploadToLocalFileSystem(@RequestParam("file") MultipartFile file,@RequestParam("file_type") String file_type, Model model) {


        if (file.isEmpty()){

            model.addAttribute("msg", "No File Selected");

            return "upload";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()).toUpperCase();
        if(fileUploadedBefore(fileName)){

            model.addAttribute("msg", "FILE HAS BEEN UPLOADED BEFORE");

            return "upload";
        }
        Path path = Paths.get(upload_dir + file.getOriginalFilename().toUpperCase());
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            log.info("DONE UPLOADING TO LOCAL DIR");

            if(file_type.equalsIgnoreCase("OBI")){

                splitFiles(fileName.toUpperCase());
            }


            jdbcTemplate.execute("INSERT INTO FILEUPLOADS(FILENAME, FILE_TYPE) VALUES ('"+fileName+"', '"+file_type+"')");
            if(file_type.equalsIgnoreCase("OBI")){
                createTable(fileName);
                log.info("Created database table "+fileName);
                return  "redirect:/obi-files";
            }else{

                return  "redirect:/auto-files";
            }
        } catch (IOException e) {
            model.addAttribute("msg", "File Did not upload ");
            e.printStackTrace();
            return "upload";
        }
    }

    @GetMapping("/post-file/{filename}")
    public String postFile (@PathVariable(name = "filename") String filename, Model model){

        String ogfilename = filename.toUpperCase();
        filename = filename.toUpperCase();
        List<OBIFILE> obifiles;

        String returnPath = null;
        if(postedBefore(ogfilename)){
            model.addAttribute("msg", "FILE HAS BEEN POSTED BEFORE "+filename);

            returnPath = "redirect:/obi-files";
            return returnPath;
        }
        log.info("file dire "+upload_dir+filename);

        obifiles =  jdbcTemplate.query("select * FROM FILEUPLOADS WHERE FILENAME  ='"+ogfilename+"'  ", new OBIFILEMapper());

        String file_type = obifiles.get(0).getFILE_TYPE();


        if (file_type.equalsIgnoreCase("OBI")){

            returnPath =  "redirect:/obi-files";
        }else {

            returnPath=  "redirect:/auto-files";
        }



        //lets take all the files matching the name and



        if(file_type.equalsIgnoreCase("OBI")){
            String filePath = "/var/stage/"+filename+"/";
            File directoryPath = new File(filePath);
            String contents[] = directoryPath.list();

            postmany(contents, filename ,file_type);

        }else{
            doSftpTransfr(filename, file_type);

        }

        jdbcTemplate.execute("UPDATE FILEUPLOADS SET POSTEDON = current_timestamp, STATUS = 'POSTING', POSTEDBY=1 WHERE FILENAME='"+ogfilename+"'");


      /* if(doSftpTransfr(filename, file_type)){

            jdbcTemplate.execute("UPDATE FILEUPLOADS SET POSTEDON = current_timestamp, STATUS = 'POSTING', POSTEDBY=1 WHERE FILENAME='"+ogfilename+"'");


            return  returnPath;
        }else{

            model.addAttribute("msg", "FILE HAS NOT BEEN POSTED "+filename);
           return returnPath;
        }*/

        return returnPath;

    }

    void createTable(String tableName){




        String string = "create table  "+tableName+"(GZAB1 VARCHAR(254),GZAB2 VARCHAR(254), GZAMA1 VARCHAR(254),GZAMA2 VARCHAR(254),GZAN1 VARCHAR(254)," +
                "GZAN2 VARCHAR(254),GZAS1 VARCHAR(254),GZAS2 VARCHAR(254),GZBRNM VARCHAR(254),GZCCY1 VARCHAR(254),GZCCY2 VARCHAR(254),GZDRF1 VARCHAR(254)," +
                "GZDRF2 VARCHAR(254),GZEXRH VARCHAR(254),GZNR1 VARCHAR(254),GZNR2 VARCHAR(254),GZNR3 VARCHAR(254),GZNR4 VARCHAR(254),GZNR5 VARCHAR(254)," +
                "GZNR6 VARCHAR(254),GZNR7 VARCHAR(254),GZNR8 VARCHAR(254),GZPBR VARCHAR(254),GZPSQ7 VARCHAR(254),GZQEVM VARCHAR(254),GZREF VARCHAR(254)," +
                "GZTCD1 VARCHAR(254),GZTCD2 VARCHAR(254),GZTREF VARCHAR(254), GZVFR1 VARCHAR(254),GZVFR2 VARCHAR(254),CHARGE SMALLINT,DATERECEIVED TIMESTAMP," +
                "GROUPREFERENCE VARCHAR(254),NARRATIVE VARCHAR(254),RESPONSECODE VARCHAR(5),REVERSALTCD1 VARCHAR(10),REVERSALTCD2 VARCHAR(10),BRNM VARCHAR(10)," +
                "FORMAT VARCHAR(10),JTT VARCHAR(5),MAPLOCN VARCHAR(5),MSGTYPE VARCHAR(40),NMSGS VARCHAR(10),ORIGIN VARCHAR(10),ORIGTYPE VARCHAR(40),PCREF VARCHAR(40)," +
                "PASSWORD VARCHAR(10),REPLY VARCHAR(5),REPLYMSG VARCHAR(64),RESPMODE VARCHAR(5),RPGM VARCHAR(10),RPYQUEUE VARCHAR(20),SPAREA VARCHAR(254),SPAREB VARCHAR(254)," +
                "SRCSYS VARCHAR(10),TGTSYS VARCHAR(10),TRANSQ VARCHAR(10),TSTAMP VARCHAR(20),USER VARCHAR(10),USID VARCHAR(10),VERSION VARCHAR(10),WARNS VARCHAR(5)," +
                "WSID VARCHAR(15),DRV VARCHAR(10),GZIEA VARCHAR(1),PICKED VARCHAR(25),CHARGEAMOUNT VARCHAR(254),XRREC VARCHAR(254),MSGTXT VARCHAR(78)," +
                "USERDATA  VARCHAR(254),CHARGECODE VARCHAR(254),ACCOUNTBRANCH VARCHAR(255),TERMINAL INTEGER,TERMINALID VARCHAR(255),id integer not null GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),PRIMARY KEY (id) )";

        jdbcTemplate.execute(string);

    }



    void splitFiles(String fileName ) throws IOException {
        String uploadPath = "/var/stage/"+fileName+"/";
        File dir = new File(uploadPath);

        if(dir.mkdirs()){

            log.info("CREATED DIR"+uploadPath);
        }else{

            log.info("FAILED TO CREATE DR "+uploadPath);
        }


        //Stream stream =  Files.lines(Paths.get(upload_dir+fileName)).filter(str->str.startsWith("RCTP10"));

        //File file = new File(upload_dir+fileName);

        BufferedReader reader;

        reader = new BufferedReader(new FileReader(upload_dir+fileName));

        String line = reader.readLine();
        int fileNumber=0, countperFile =0, rcptCount=0;
        FileOutputStream fileStream = new FileOutputStream(new File(uploadPath+fileName+"FPART"+fileNumber));
        String file_partname = fileName+"FPART"+fileNumber;
        jdbcTemplate.execute("INSERT INTO PART_FILES (FILE_PART_NAME, FILE_NAME) VALUES('"+file_partname+"', '"+fileName+"')");
        OutputStreamWriter myWriter = new OutputStreamWriter(fileStream, "Cp1252");
        while (line!=null){

            System.out.println("WE GOT GENERIC "+ line);

            if (line.startsWith("RCTP10")){
                System.out.println("WE RCPT10 "+ line);
                rcptCount = rcptCount+1;
                try {



                    myWriter.write(line);
                    myWriter.write(System.getProperty("line.separator"));
                    myWriter.flush();

                    if (countperFile>10000){
                        fileNumber = fileNumber+1;
                        fileStream = new FileOutputStream(new File(uploadPath+fileName+"FPART"+fileNumber));

                        myWriter = new OutputStreamWriter(fileStream, "Cp1252");
                        myWriter.write(line);
                        myWriter.write(System.getProperty("line.separator"));
                        myWriter.flush();
                        countperFile = 0;
                        file_partname = fileName+"FPART"+fileNumber;
                        jdbcTemplate.execute("INSERT INTO PART_FILES (FILE_PART_NAME, FILE_NAME) VALUES('"+file_partname+"', '"+fileName+"')");
                    }

                    countperFile =countperFile+1;



                    // save the file and the filename in the part_files and set status as PENDING(DEFAULT)
                    //after reading E.O.D in ESB mark this part file as posted
                    //check if all the part files mathcig file name have been marked as posted and then DO BULK




                    //save  THE FILE PARTS , filename , file path


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            line = reader.readLine();

        }

        System.out.println("WE GOT "+rcptCount);

       /* Iterator<String> iterator = stream.iterator();

        while (iterator.hasNext()){
            log.info("WE GOT  "+iterator.next());
        }*/

        /*int countperFile = 0;
        int fileNumber = 0;

        try {
            //FileWriter myWriter = new FileWriter(uploadPath+fileName+"FPART"+fileNumber);
            FileOutputStream fileStream = new FileOutputStream(new File(uploadPath+fileName+"FPART"+fileNumber));
            OutputStreamWriter myWriter = new OutputStreamWriter(fileStream, "Cp1252");

            //myWriter.getEncoding().replace()
//cs.newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE)


            while (iterator.hasNext()){

                log.info(iterator.next());

                myWriter.write(iterator.next());
                myWriter.write(System.getProperty("line.separator"));
                myWriter.flush();

                if(countperFile>10000){

                    fileNumber = fileNumber+1;
                //    myWriter = new FileWriter(uploadPath+fileName+"FPART"+fileNumber);
                    fileStream = new FileOutputStream(new File(uploadPath+fileName+"FPART"+fileNumber));
                    myWriter = new OutputStreamWriter(fileStream, "Cp1252");
                    myWriter.write(iterator.next());
                    myWriter.write(System.getProperty("line.separator"));
                    myWriter.flush();
                    countperFile = 0;
                }
                countperFile =countperFile+1;
            }

            String file_partname = fileName+"FPART"+fileNumber;

            // save the file and the filename in the part_files and set status as PENDING(DEFAULT)
            //after reading E.O.D in ESB mark this part file as posted
            //check if all the part files mathcig file name have been marked as posted and then DO BULK


            jdbcTemplate.execute("INSERT INTO PART_FILES (FILE_PART_NAME, FILE_NAME) VALUES('"+file_partname+"', '"+fileName+"')");

            //save  THE FILE PARTS , filename , file path


        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }


    boolean  postmany(String dir[], String fileName, String file_type){

        log.info("commence posting ");
        log.info("have "+dir.length+" total files");
        String file_dir;

        file_dir = "/var/stage/"+fileName+"/";
        String remoteDir;
        if(file_type.equalsIgnoreCase("OBI")){

            remoteDir = remoteDirOBI;
        }else{

            remoteDir = remoteDirAUTOPAY;
        }

        int postCount = 0;
        for (String file : dir) {


            try {
                log.info("upload "+file);
                this.getCon().put(file_dir+file, remoteDir+file);
                postCount = postCount+1;
                log.info("DONE COPYING  "+file +" from "+file_dir+file+ "  to "+ remoteDir+file);
                this.getCon().chmod(777, remoteDir+file);
            } catch (JSchException e) {
                e.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            }
        }


        return postCount==dir.length;

        //so we must check if all the files in the dir where posted

    }
    boolean doSftpTransfr(String fileName, String file_type){



        String remoteDir;
        if(file_type.equalsIgnoreCase("OBI")){

            remoteDir = remoteDirOBI;
        }else{

            remoteDir = remoteDirAUTOPAY;
        }
        try {

            this.getCon().put(upload_dir+fileName, remoteDir+fileName);
            log.info("Setting  chmod 777 for uploaded file in remote dir dr ");
            this.getCon().chmod(777, remoteDir+fileName);
            log.info("DONE COPYING FILE FROM  "+ upload_dir+fileName  +" TO  "+remoteHost+remoteDir+fileName);

            return  true;
        } catch (SftpException e) {
            e.printStackTrace();
            return  false;
        } catch (JSchException e) {
            e.printStackTrace();

            return  false;
        }finally {

            try {
                getCon().exit();
            } catch (JSchException e) {
                e.printStackTrace();
            }
        }

    }


    ChannelSftp getCon() throws JSchException {

        JSch jsch = new JSch();
        // jsch.setKnownHosts(hosts_dir);
        Session session = jsch.getSession(username, remoteHost);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(password);
//        session.connect();
        session.connect(300000); // 5 mins

        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        log.info("Attempting connection to "+remoteHost);
        sftpChannel.connect();

        log.info("Connection... Done");
        return  sftpChannel;
    }

    boolean fileUploadedBefore(String file_name){

        List<OBIFILE> obifiles;

        obifiles= jdbcTemplate.query("SELECT * FROM FILEUPLOADS WHERE FILENAME = '"+file_name+"' ", new OBIFILEMapper());


        return !obifiles.isEmpty();
    }

    boolean postedBefore(String filename){

        List<OBIFILE> obifiles;

        obifiles= jdbcTemplate.query("SELECT * FROM FILEUPLOADS WHERE FILENAME = '"+filename+"' and STATUS ='POSTED' ", new OBIFILEMapper());


        return !obifiles.isEmpty();
    }


}
