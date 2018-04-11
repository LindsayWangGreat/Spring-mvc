package com.sap.bulletinboard.ads.models;

import java.beans.ConstructorProperties;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Advertisement {
    //@JsonProperty("title")
    @NotBlank
    private String title;
    
    public Advertisement() {
        
    }
    
//    public Advertisement(String title) {
//        this.title = title;
//    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
