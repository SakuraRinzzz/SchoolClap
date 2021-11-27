package com.example.myapplication.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class News {

    private int id;
    private String imageUrl;
    private String title;
    private String desc;
    private String publishAccount;
    private Date publishTime;

    public News(int id, String imageUrl, String title, String desc, String publishAccount, Date publishTime) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.desc = desc;
        this.publishAccount = publishAccount;
        this.publishTime = publishTime;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getPublishAccount() {
        return publishAccount;
    }

    public Date getPublishTime() {
        return publishTime;
    }

}

