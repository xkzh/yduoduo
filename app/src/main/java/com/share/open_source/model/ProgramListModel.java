package com.share.open_source.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;



public class ProgramListModel implements Serializable {
    public List<ProgramModel> list;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
