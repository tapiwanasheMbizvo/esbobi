package com.tmgreyhat.esbobi.controllers;

import com.jcraft.jsch.*;
import com.tmgreyhat.esbobi.mappers.OBIFILEMapper;
import com.tmgreyhat.esbobi.mappers.RCMapper;
import com.tmgreyhat.esbobi.models.OBIFILE;
import com.tmgreyhat.esbobi.models.RCPT101;
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




    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    String getIndexPage(Model model){



        return "index";

    }

    /*@GetMapping("obi-files")
    String fileList(Model model){

        List<OBIFILE> obifiles;


        obifiles= jdbcTemplate.query("SELECT * FROM FILEUPLOADS  ", new OBIFILEMapper());

        model.addAttribute("obifiles", obifiles);


        return "allfiles";
    }*/

    @GetMapping("file-history")
    String fileHistory(Model model){


        return  "fileHistory";
    }

    @GetMapping("upload-obi")
    String getFileUploadPage(Model model){


        return  "upload";

    }

    @GetMapping("obi-history")
    String obiHistory(Model model){


        return  "history";

    }
}

