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

    public void setIsExist(Boolean isExist) {
        this.isExist = isExist;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUuid() {
        return uuid;
    }

    public Boolean getIsExist() {
        return isExist;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("desc", desc);
        result.put("author", author);
        result.put("uuid", uuid);
        result.put("isExist", isExist);

        return result;
    }

}
