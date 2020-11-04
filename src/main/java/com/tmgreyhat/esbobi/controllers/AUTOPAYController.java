package com.tmgreyhat.esbobi.controllers;


import com.tmgreyhat.esbobi.mappers.OBIFILEMapper;
import com.tmgreyhat.esbobi.models.OBIFILE;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@Slf4j
public class AUTOPAYController {

    private String upload_dir = "C:\\ESB\\upload\\";
    //private String upload_dir = "/home/ESBOBI/upload/";

    @Autowired
    JdbcTemplate jdbcTemplate;



    @GetMapping("/upload-autopay")
    private String getform (){



        return "upload_auto";
    }
    @PostMapping("/upload-autopay")
    public String uploadToLocalFileSystem(@RequestParam("file") MultipartFile file, Model model) {

        if (file.isEmpty()){

            model.addAttribute("msg", "No File Selected");

            return "upload_auto";
        }


        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        fileName = fileName.replaceAll("_", "");

        String tableName = fileName.substring(0, fileName.length() - 4);
        tableName = tableName.toUpperCase();
        tableName = tableName.replaceAll("_", "");

        if(fileUploadedBefore(tableName)){

            model.addAttribute("msg", "FILE HAS BEEN UPLOADED BEFORE");

            return "upload_auto";
        }

        jdbcTemplate.execute("INSERT INTO FILEUPLOADS(FILENAME, FILE_TYPE) VALUES ('"+tableName+"', 'AUTOPAY')");


        Path path = Paths.get(upload_dir + file.getOriginalFilename().replaceAll("_", "").toUpperCase());
        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("DONE UPLOADING TO LOCAL DIR");




        model.addAttribute("msg", "File Uploaded");

        return "upload_auto";

    }

/*    @GetMapping("/post-file/{filename}")
    public String postFile (@PathVariable(name = "filename") String filename, Model model){

        String ogfilename = filename;
        filename = filename.toUpperCase();
        filename = filename+".TXT";


        if(postedBefore(ogfilename)){
            model.addAttribute("msg", "FILE HAS BEEN POSTED BEFORE "+filename);

            return "redirect:/obi-files";
        }
        log.info("file dire "+upload_dir+filename);



        doSftpTransfr(filename);
        jdbcTemplate.execute("UPDATE FILEUPLOADS SET POSTEDON = current_timestamp, STATUS = 'POSTED', POSTEDBY=1 WHERE FILENAME='"+ogfilename+"'");

        return  "redirect:/obi-files";
    }*/



    boolean fileUploadedBefore(String file_name){

        List<OBIFILE> obifiles;

        obifiles= jdbcTemplate.query("SELECT * FROM FILEUPLOADS WHERE FILENAME = '"+file_name+"' ", new OBIFILEMapper());


        return !obifiles.isEmpty();
    }


}
