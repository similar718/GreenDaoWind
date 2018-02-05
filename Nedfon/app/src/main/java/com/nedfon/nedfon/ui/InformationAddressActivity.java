package com.nedfon.nedfon.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.StatusBarCompat;

public class InformationAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mBackTv,mCancelTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(InformationAddressActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_information_address);

        initViewx();
    }

    private void initViewx() {
        mBackTv = this.findViewById(R.id.activity_information_back_tv);
        mCancelTv = this.findViewById(R.id.activity_information_cancel_tv);

        mBackTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_information_back_tv:
                this.finish();
                break;
            case R.id.activity_information_cancel_tv:
                this.finish();
            default:
                break;
        }
    }
}
