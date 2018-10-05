package com.share.open_source.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.share.open_source.R;


public class SearchView extends FrameLayout {

    private EditText mEtSearch;
    private ImageView mIvClear;
    private TextView mTvClose;
    private OnTextChangeListener mOnTextChangeListener;

    public EditText getEtSearch() {
        return mEtSearch;
    }

    public void setEtSearch(EditText etSearch) {
        this.mEtSearch = etSearch;
    }

    public ImageView getIvClear() {
        return mIvClear;
    }

    public void setIvClear(ImageView ivClear) {
        this.mIvClear = ivClear;
    }

    public TextView getTvClose() {
        return mTvClose;
    }

    public void setTvClose(TextView tvClose) {
        this.mTvClose = tvClose;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.mOnTextChangeListener = onTextChangeListener;
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.search_view, null);
        mEtSearch = (EditText) rootView.findViewById(R.id.et_search);
        mIvClear = (ImageView) rootView.findViewById(R.id.iv_clear);
        mTvClose = (TextView) rootView.findViewById(R.id.tv_close);
        addView(rootView, new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        mIvClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtSearch.setText("");
            }
        });
        mTvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mIvClear.setVisibility(View.VISIBLE);
                } else {
                    mIvClear.setVisibility(View.GONE);
                }
                doTextChange(s);

            }
        });
    }

    private void doTextChange(Editable s) {
        if (mOnTextChangeListener != null) {
            mOnTextChangeListener.onTextChange(s);
        }
    }

    public interface OnTextChangeListener {
        void onTextChange(Editable s);
    }
}
