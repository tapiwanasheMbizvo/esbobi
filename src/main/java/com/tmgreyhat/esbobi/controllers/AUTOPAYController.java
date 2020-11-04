package com.tmgreyhat.esbobi.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class AUTOPAYController {


    @Autowired
    JdbcTemplate jdbcTemplate;



}
