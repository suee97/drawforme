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

    public PostModel() {
        // Default constructor
    }

    public PostModel(String title, String desc, String author, String uuid) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.uuid = uuid;
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

    public String getDesc() {
        return this.desc;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getUuid() {return this.uuid;}
}
