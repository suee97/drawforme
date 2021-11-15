package com.example.drawforme;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PostModel {
    private String title;
    private String desc;
    private String author;

    public PostModel() {
        // Default constructor
    }

    public PostModel(String title, String desc, String author) {
        this.title = title;
        this.desc = desc;
        this.author = author;
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

    public String getDesc() {
        return this.desc;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }
}
