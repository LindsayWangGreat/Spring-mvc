package com.sap.bulletinboard.ads.models;

import java.beans.ConstructorProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Advertisement {
    //@JsonProperty("title")
    @NotBlank
    private String title;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
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
