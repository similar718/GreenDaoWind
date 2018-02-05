//package com.nedfon.nedfon.ui;
//
//import android.content.Intent;
//import android.os.Build;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.TextView;
//
//import com.nedfon.nedfon.R;
//import com.nedfon.nedfon.adapter.MessageCenterAdapter;
//import com.nedfon.nedfon.bean.MessageCenterBean_test;
//import com.nedfon.nedfon.utils.StatusBarCompat;
//
//public class MessageCenterDetailActivity extends AppCompatActivity implements View.OnClickListener {
//
//    private TextView mBackTv;
//    private TextView mTitleTv,mTimeTv,mContentTv;
//
//    private String title,time,content;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //4.4以上的版本都可以将状态栏设置成透明颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else {
//            StatusBarCompat.setStatusBardown5AndSetColor(MessageCenterDetailActivity.this,"#21D2B5");
//        }
//        setContentView(R.layout.activity_message_center_detail);
//
//        initData();
//        initView();
//    }
//
//    private void initData() {
//        Intent intent = getIntent();
//        title = intent.getStringExtra("title");
//        time = intent.getStringExtra("time");
//        content = intent.getStringExtra("content");
//    }
//
//
//    private void initView() {
//        mBackTv = this.findViewById(R.id.activity_message_center_detail_back_tv);
//        mTitleTv = this.findViewById(R.id.activity_message_center_detail_title_tv);
//        mTimeTv = this.findViewById(R.id.activity_message_center_detail_time_tv);
//        mContentTv = this.findViewById(R.id.activity_message_center_detail_content_tv);
//
//        mBackTv.setOnClickListener(this);
//
//        mTitleTv.setText(title);
//        mTimeTv.setText(time);
//        mContentTv.setText(content);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.activity_message_center_detail_back_tv:
//                this.finish();
//                break;
//            default:
//                break;
//        }
//    }
//}
