package com.nedfon.nedfon.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;

public class SexChoicePopupWindow extends PopupWindow {

    private Context mContext;
    private View view;
    private TextView mSureTv;
    private TextView mManTv;
    private TextView mWomanTv;
    public static int sex = 0;


    public SexChoicePopupWindow(final Context mContext,int sexs) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.sex_choice_pop, null);
        mSureTv = (TextView) view.findViewById(R.id.sex_choice_pop_sure_tv);
        mManTv = view.findViewById(R.id.sex_choice_pop_man_tv);
        mWomanTv = view.findViewById(R.id.sex_choice_pop_woman_tv);
        if (sexs == 0){
            sex = 1;
            mManTv.setTextColor(Color.parseColor("#B9B9B9"));
            mWomanTv.setTextColor(Color.parseColor("#000000"));
        } else {
            sex = 0;
            mManTv.setTextColor(Color.parseColor("#000000"));
            mWomanTv.setTextColor(Color.parseColor("#B9B9B9"));
        }
        // 确定按钮
        mSureTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mManTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 0;
                mManTv.setTextColor(Color.parseColor("#000000"));
                mWomanTv.setTextColor(Color.parseColor("#B9B9B9"));
            }
        });
        mWomanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = 1;
                mManTv.setTextColor(Color.parseColor("#B9B9B9"));
                mWomanTv.setTextColor(Color.parseColor("#000000"));
            }
        });

        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                int height = view.findViewById(R.id.sex_choice_pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x77000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.sex_choice_pop);

    }
}
