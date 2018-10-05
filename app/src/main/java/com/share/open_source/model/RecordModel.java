package com.share.open_source.model;

import com.google.gson.Gson;

import java.io.Serializable;

public class RecordModel implements Serializable{

    public String title;
    public String playLink;
    public String origin;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
