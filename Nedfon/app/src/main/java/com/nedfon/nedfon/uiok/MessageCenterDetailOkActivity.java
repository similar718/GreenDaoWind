package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nedfon.nedfon.R;

public class MessageCenterDetailOkActivity extends BaseTopBottomActivity implements View.OnClickListener {

    private TextView mTitleTv,mTimeTv,mContentTv;

    private String title,time,content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleText("消息中心");
        setImage(4);
        NAME = MessageCenterDetailOkActivity.class.getSimpleName();

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        time = intent.getStringExtra("time");
        content = intent.getStringExtra("content");
    }


    private void initView() {
        mTitleTv = this.findViewById(R.id.activity_message_center_detail_title_tv);
        mTimeTv = this.findViewById(R.id.activity_message_center_detail_time_tv);
        mContentTv = this.findViewById(R.id.activity_message_center_detail_content_tv);


        mTitleTv.setText(title);
        mTimeTv.setText(time);
        mContentTv.setText(content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            default:
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_message_center_detail_ok;
    }

    @Override
    protected void setBackOnClick() {
        this.finish();
    }
}
