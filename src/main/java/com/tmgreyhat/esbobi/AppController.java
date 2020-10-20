package com.tmgreyhat.esbobi;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Properties;

@Controller
@Slf4j
public class AppController {


    private String remoteHost = "10.136.192.172";
    private String username = "root";
    private String password = "root";
    // private String hosts_dir = "C:\\Users\\gaswaj\\.ssh\\known_hosts";
    //  private String hosts_dir = "C:\\Users\\tapiwanashem\\.ssh\\known_hosts";
   //private String upload_dir = "C:\\ESB\\upload\\";
    private String upload_dir = "/home/ESBOBI/upload/";



    private String remoteDir = "/balance_enq/OBI/upload/";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    String getIndexPage(Model model){



        return "index";

    }

    @GetMapping("file-history")
    String fileHistory(){

        return "fileHistory";
    }
    @GetMapping("upload-obi")
    String getFileUploadPage(Model model){


        return  "upload";

    }

    @GetMapping("obi-history")
    String obiHistory(Model model){


        return  "history";

    }

    @PostMapping("/file-history")
    public String getContents(@RequestParam("file") MultipartFile file, Model model){


        List<RCPT101> rcpt101List;


        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //lets remove underscores  here
        fileName = fileName.replaceAll("_", "");

        String tableName = fileName.substring(0, fileName.length() - 4);
        tableName = tableName.replaceAll("_", "");
        log.info("Searching for transactions in file   "+tableName);



        rcpt101List=  jdbcTemplate.query("select * FROM RCPT101 WHERE FILE_NAME  ='"+tableName+" ' ", new RCMapper());

        rcpt101List.forEach(rcpt101 -> log.info("WE got "+rcpt101.getREFERENCE_REF()));
        model.addAttribute("transactins", rcpt101List);


        return "fileHistory";
    }
    @PostMapping("/upload")
    public ResponseEntity uploadToLocalFileSystem(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()){


            return ResponseEntity.ok("No FIle Selected");
        }



        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //lets remove underscores  here
        fileName = fileName.replaceAll("_", "");
        Path path = Paths.get(upload_dir + file.getOriginalFilename().replaceAll("_", ""));
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("DONE UPLOADING TO LOCAL DIR");
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(upload_dir)
                .path(fileName)
                .toUriString();
        //lets take the the file name and create a table remove file extension

        String tableName = fileName.substring(0, fileName.length() - 4);

       /* if(!saveOBIRecord(tableName)){

            return  ResponseEntity.ok("File has been uploaded before ");
        }
*/
        createTable(tableName);
        log.info("Created database table "+tableName);
        doSftpTransfr(fileName);
        return ResponseEntity.ok(fileDownloadUri);
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

    void doSftpTransfr(String fileName){

        try {


            this.getCon().put(upload_dir+fileName, remoteDir+fileName);
            log.info("DONE COPYING FILE FROM  "+ upload_dir+fileName  +" TO  "+remoteHost+remoteDir+fileName);

        } catch (SftpException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
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
        session.connect();
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
        log.info("Attempting connection to "+remoteHost);
        sftpChannel.connect();

        log.info("Connection... Done");
        return  sftpChannel;
    }




}


