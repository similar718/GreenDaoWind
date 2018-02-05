package com.nedfon.nedfon.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nedfon.nedfon.R;

public class PersonalCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mBackTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_center);

        initView();
    }

    private void initView() {
        mBackTv = this.findViewById(R.id.activity_personal_center_back_tv);

        mBackTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_personal_center_back_tv:
                PersonalCenterActivity.this.finish();
                break;
            default:
                break;
        }
    }
}
