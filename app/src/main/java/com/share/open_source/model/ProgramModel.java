package com.share.open_source.model;

import com.google.gson.Gson;
import java.io.Serializable;

public class ProgramModel implements Serializable{


    public String type;
    public String title;
    public String images;
    public String name;
    public String translateTitle;
    public String area;
    public String style;
    public String publishDate;
    public String minutes;
    public String director;
    public String actor;
    public String describtion;
    public String playSource;
    public int viewType;

    public ProgramModel() {

    }

    public ProgramModel(int viewType) {
        this.viewType = viewType;
    }




    @Override
    public String toString() {
        return new Gson().toJson(this);
    }



}
