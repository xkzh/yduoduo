/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.share.open_source.widget;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.share.open_source.R;


public class SharePopupWindow extends PopupWindow implements View.OnTouchListener {

    private View mContentView;
    private Activity mActivity;


    ImageView iv_qq;
    ImageView iv_qq_zone;
    ImageView iv_wx_circle;
    ImageView iv_wxchat;

    ShareTypeListener listener;


    public SharePopupWindow(Activity activity) {
        mActivity = activity;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mContentView = LayoutInflater.from(activity).inflate(R.layout.layout_share_popup_window, null);
        setContentView(mContentView);


        iv_qq = (ImageView) mContentView.findViewById(R.id.iv_qq);
        iv_qq_zone = (ImageView) mContentView.findViewById(R.id.iv_qq_zone);
        iv_wx_circle = (ImageView) mContentView.findViewById(R.id.iv_wx_circle);
        iv_wxchat = (ImageView) mContentView.findViewById(R.id.iv_wxchat);


        iv_qq.setOnTouchListener(this);
        iv_qq_zone.setOnTouchListener(this);
        iv_wx_circle.setOnTouchListener(this);
        iv_wxchat.setOnTouchListener(this);

        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        setAnimationStyle(R.style.LoginPopup);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });
    }

    private void scale(View v, boolean isDown) {
        if (v.getId() == iv_qq.getId() || v.getId() == iv_qq_zone.getId() || v.getId() == iv_wx_circle.getId() || v.getId() == iv_wxchat.getId()) {
            if (isDown) {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_down);
                v.startAnimation(testAnim);
            } else {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_up);
                v.startAnimation(testAnim);
            }
        }

        if (!isDown && listener!=null) {
            switch (v.getId()) {
                case R.id.iv_qq:
                    listener.onShare(iv_qq, "qq");
                    break;
                case R.id.iv_qq_zone:
                    listener.onShare(iv_qq_zone, "qq_zone");
                    break;
                case R.id.iv_wxchat:
                    listener.onShare(iv_wxchat, "weixin");
                    break;
                case R.id.iv_wx_circle:
                    listener.onShare(iv_wx_circle, "wx_circle");
                    break;
            }

            iv_qq.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 500);

        }
    }

    private void lighton() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        lightoff();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        lightoff();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scale(v, true);
                break;
            case MotionEvent.ACTION_UP:
                scale(v, false);
                break;
        }
        return false;
    }

    public interface ShareTypeListener {
        void onShare(ImageView view, String type);
    }

    public void setShareTypeListener(ShareTypeListener listener){
        this.listener = listener;
    }

}
