package com.example.myapplication.entity;

import java.util.Date;

/**
 * 反馈的历史记录
 */
public class UploadHistoryFeedback {

    private int id;         //处理信息id
    private int feedBackId; //编号id
    private String desc;    //处理状态
    private Date time;      //处理时间
    private String imageUrl;//处理的图片

    @Override
    public String toString() {
        return "UploadHistoryFeedback{" +
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
