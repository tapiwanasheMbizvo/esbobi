package com.tmgreyhat.esbobi.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tapiwanashem
 * @Date 25/1/2021
 * @Time 14:49
 * @Year 2021
 */

@RestController
public class IndexError implements ErrorController {

    private static final String PATH = "/";

    @RequestMapping(value = PATH)
    public String error() {
        return "Error handling";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
