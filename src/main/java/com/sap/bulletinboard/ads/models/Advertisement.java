package com.sap.bulletinboard.ads.models;

import java.beans.ConstructorProperties;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "advertisements")
public class Advertisement {
    //@JsonProperty("title")
    @NotBlank
    @Column(name = "mytitle")
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

    public Long getId() {
        // TODO Auto-generated method stub
        return id;
    }
}
