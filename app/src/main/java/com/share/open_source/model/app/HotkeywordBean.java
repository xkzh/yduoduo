package com.share.open_source.model.app;

import com.google.gson.Gson;

import java.io.Serializable;

public class HotkeywordBean implements Serializable {
    public String keyword;
    public String playurl;

    public HotkeywordBean(){}

    public HotkeywordBean(String keyword, String playurl) {
        this.keyword = keyword;
        this.playurl = playurl;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
