package com.share.open_source.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.share.open_source.R;
import com.share.open_source.presenter.BasePresenter;
import com.share.open_source.ui.activity.base.BaseActivity;
import com.share.open_source.utils.AppCommonUtils;

import butterknife.Bind;
import butterknife.OnClick;

public class YddAboutActivity extends BaseActivity {

    @Bind(R.id.tv_version_name)
    TextView nameVersion;

    @OnClick(R.id.iv_back)
    public void back() {
        finish();
    }

    @Override
    public void initView() {
        nameVersion.setText("v " + AppCommonUtils.getAppVersionName(mContext));
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.ydd_about_activity;
    }
}
