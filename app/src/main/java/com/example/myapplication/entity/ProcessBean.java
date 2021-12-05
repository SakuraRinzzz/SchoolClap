package com.example.myapplication.entity;

import java.util.Date;

public class ProcessBean {

    private int id;
    private int feedBackId;
    private String desc;
    private Date time;
    private String imageUrl;

    @Override
    public String toString() {
        return "ProcessBean{" +
                "id=" + id +
                ", feedBackId=" + feedBackId +
                ", desc='" + desc + '\'' +
                ", time=" + time +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeedBackId() {
        return feedBackId;
    }

    public void setFeedBackId(int feedBackId) {
        this.feedBackId = feedBackId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
