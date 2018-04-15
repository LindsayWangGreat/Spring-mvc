package com.sap.bulletinboard.ads.models;

import java.beans.ConstructorProperties;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.logging.LogSource;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.internal.util.logging.Log;

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
    
    @Column(updatable = false)
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    
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

    public void setId(Long id) {
        // TODO Auto-generated method stub
        this.id = id;
    }
    
    public Timestamp getCreatedAt() {
        // TODO Auto-generated method stub
        if(this.createdAt!=null) {
            return createdAt;
        }
        return null; // use java.util.Date
    }
    @PrePersist
    protected void onPersist() {
        setCreatedAt(now());
    }
    private void setCreatedAt(Timestamp now) {
        // TODO Auto-generated method stub
        createdAt = now;
    }
    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = now();
    }
    protected Timestamp now() {                       // use java.sql.Timestamp
        return new Timestamp((new Date()).getTime()); // use java.util.Date
    }

    public Timestamp getUpdatedAt() {
        // TODO Auto-generated method stub
        return modifiedAt;
    } 
}
