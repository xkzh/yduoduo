package com.share.open_source.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.share.open_source.R;
import com.share.open_source.application.AppApplication;
import com.share.open_source.constants.Constants;
import com.share.open_source.model.app.AppData;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.utils.AppCommonUtils;
import com.share.open_source.utils.SpUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class YddSettingActivity extends BaseActivity {

    private AppData appData;
    private String download;


    @Bind({R.id.ll_version})
    RelativeLayout ll_version;
    @Bind({R.id.tv_curr_version})
    TextView tv_curr_version;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }



    @OnClick(R.id.tv_about)
    public void about() {
        startActivity(new Intent(this, YddAboutActivity.class));
    }

    @OnClick(R.id.ll_version)
    public void check() {
      showToast(mContext,"已是最新版本");
    }


    @OnClick(R.id.tv_contact)
    public void contact() {
        if (!TextUtils.isEmpty(Constants.QQ_KEY)) {
            AppApplication.joinQQGroup(mContext,Constants.QQ_KEY);//加入QQ群
        }
    }





    @OnClick(R.id.tv_clear)
    public void clear() {
        showToast(mContext, "缓存清理成功");
    }

    @Override
    public void initView() {

        tv_curr_version.setText(" " + AppCommonUtils.getAppVersionName(mContext));
    }

    /**
     * 复制下载地址
     */
    @OnClick(R.id.tv_download)
    public void download() {
        if (!TextUtils.isEmpty(download)) {
            ClipboardManager myClipboard;
            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData myClip;
            myClip = ClipData.newPlainText("text", download);//text是内容
            myClipboard.setPrimaryClip(myClip);
            showToast(mContext, "下载链接已复制，去浏览器粘贴打开，或发送给好友！");
        }
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.ydd_setting_activity;
    }


}
