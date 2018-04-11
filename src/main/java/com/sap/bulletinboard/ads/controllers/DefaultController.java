package com.sap.bulletinboard.ads.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class DefaultController {

    // Move the context definition up using ALT+Up
    @Inject
    ServletContext context;

    @GetMapping
    public String get() throws IOException {
       
        return "OK";
        
    }
    
    @RequestMapping(path="/hello/{firstname}/{lastname}")
    public String sayHello(@PathVariable("firstname") String name1,@PathVariable("lastname") String name2) {
        return "hello"+name1+name2;
    }
    
   
}