package com.share.open_source.listener;

import java.util.List;



public interface PermissionListener {

    void onGranted();

    void onDenied(List<String> deniedPermissionsList);

}
