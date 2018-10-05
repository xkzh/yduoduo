package com.share.open_source.model.app;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

public class AppData implements Serializable {

    public List<HotkeywordBean> hotkeyword;
    public ShareBean share;
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
