package com.share.open_source.model.app;

import java.io.Serializable;

public class ShareBean implements Serializable{

    private String title;
    private String content;
    private String titleUrl;
    private String imgUrl;

    public ShareBean(){}

    public ShareBean(String title, String content, String titleUrl, String imgUrl) {
        this.title = title;
        this.content = content;
        this.titleUrl = titleUrl;
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUrl() {
        return titleUrl;
    }

    public void setTitleUrl(String titleUrl) {
        this.titleUrl = titleUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
