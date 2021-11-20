package com.example.drawforme;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PostModel {
    private String title;
    private String desc;
    private String author;
    private String uuid;
    private Boolean isExist;

    public PostModel() {
        // Default constructor
    }

    public PostModel(String title, String desc, String author, String uuid, Boolean isExist) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.uuid = uuid;
        this.isExist = isExist;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setIsExist(Boolean isExist) { this.isExist = isExist; }

    public String getDesc() {
        return this.desc;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getUuid() { return this.uuid; }

    public Boolean getIsExist() { return this.isExist; }
}
