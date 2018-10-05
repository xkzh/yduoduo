package com.share.open_source.widget;



import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.share.open_source.R;
import com.share.open_source.model.app.HotkeywordBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * 标签布局（比如热搜等等）
 */
public class FlowLayout extends ViewGroup {

    private LayoutInflater mInflater;
    private boolean isColorful;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrapContent
        int width = 0;
        int height = 0;

        // 记录每一行的宽和高
        int lineWidth = 0;
        int lineHeight = 0;

        // 得到内部元素的个数
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            // 子view的占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 子view占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            // 换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft()
                    - getPaddingRight()) {

                // 对比得到最大的宽度
                width = Math.max(width, lineWidth);
                // 重置lineWidth
                lineWidth = childWidth;
                // 记录行高
                height += lineHeight;
                lineHeight = childHeight;
            } else {
                // 未换行
                // 叠加行宽
                lineWidth += childWidth;
                // 得到当前最大高度
                lineHeight = Math.max(lineHeight, childHeight);
            }
            // 最后一个控件
            if (i == count - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        //        Log.i("test", "sizeWidth" + sizeWidth);
        //        Log.i("test", "sizeHeight" + sizeHeight);

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth
                        : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height
                        + getPaddingTop() + getPaddingBottom());
        setPadding(dp2px(20), dp2px(10), dp2px(20), dp2px(10));
    }

    // 储存所有的View
    private ArrayList<ArrayList<View>> mAllViews = new ArrayList<>();
    // 储存每一行的高度
    private ArrayList<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

        // 清除一下list集合
        mAllViews.clear();
        mLineHeight.clear();

        // 得到viewGroup当前宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        ArrayList<View> lineViews = new ArrayList<>();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width
                    - getPaddingLeft() - getPaddingRight()) {
                // 记录当前行高
                mLineHeight.add(lineHeight);
                // 记录当前行的view
                mAllViews.add(lineViews);

                // 重置行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置lineViews集合
                lineViews = new ArrayList<>();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin
                    + lp.bottomMargin);
            lineViews.add(child);
        }
        // 处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子view的位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        // 有多少行
        int lineNum = mLineHeight.size();

        for (int i = 0; i < lineNum; i++) {

            // 获取当前行的view
            lineViews = mAllViews.get(i);
            // 当前行高
            lineHeight = mLineHeight.get(i);
            int lineViewSize = lineViews.size();
            for (int j = 0; j < lineViewSize; j++) {
                View child = lineViews.get(j);
                // 判断子view的状态
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + childWidth;
                int bc = tc + childHeight;

                // 为子view布局
                child.layout(lc, tc, rc, bc);

                // 同一行view坐起点坐标的变换
                left += childWidth + lp.leftMargin + lp.rightMargin;
            }
            // 换行时将left重置
            left = getPaddingLeft();
            // top要加上上一行的行高
            top += lineHeight;
        }
    }

    /**
     * 默认返回的LayoutParams
     */
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        // TODO Auto-generated method stub
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 设置数据
     */
    public void setData(final List<HotkeywordBean> tags) {

        int count = tags.size();
        for (int i = 0; i < count; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_textview, this,false);
            final HotkeywordBean bean = tags.get(i);
            tv.setText(bean.keyword);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null)
                        onTagClickListener.TagClick(bean);
                }
            });

            if(isColorful){
                Random random = new Random();
                int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                tv.setBackgroundColor(ranColor);
            }

            this.addView(tv);
        }
    }


    /**
     * 设置数据
     */
    public void setListData(final List<HotkeywordBean> list) {

        int count = list.size();
        for (int i = 0; i < count; i++) {
            final TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_textview, this,false);
            final HotkeywordBean bean = list.get(i);
            tv.setText(list.get(i).keyword);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagClickListener != null)
                        onTagClickListener.TagClick(bean);
                }
            });

            this.addView(tv);
        }
    }

    /**
     * 添加标签
     *
     * @param bean
     */
    public void addTag(final HotkeywordBean bean) {
        final TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_textview, this,
                false);
        tv.setText(bean.keyword);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTagClickListener != null)
                    onTagClickListener.TagClick(bean);
            }
        });

        if(isColorful){
            Random random = new Random();
            int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
            tv.setBackgroundColor(ranColor);
        }
        this.addView(tv);
    }

    /**
     * 设置多彩颜色
     * @param isColorful
     */
    public void setColorful(boolean isColorful) {
        this.isColorful = isColorful;
    }

    /**
     * 删除所有标签
     */
    public void cleanTag() {
        this.removeAllViews();
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    private OnTagClickListener onTagClickListener;

    public void setOnTagClickListener(OnTagClickListener onTagClickListener) {
        this.onTagClickListener = onTagClickListener;
    }


    public interface OnTagClickListener {
        void TagClick(HotkeywordBean model);
    }
}

