package com.example.drawforme;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/* 사용 할지 안할지 모름 */

public class PostModel {
    private String title;
    private String desc;

    public PostModel() {
        // Default constructor
    }

    public PostModel(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getTitle() {
        return this.title;
    }
}
