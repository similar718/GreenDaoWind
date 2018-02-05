package com.nedfon.nedfon.ui;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.StatusBarCompat;

public class FindPwdSuccessActivity extends AppCompatActivity {

    private TextView mBackToLoginTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(FindPwdSuccessActivity.this,"#21D2B5");
        }
        setContentView(R.layout.activity_find_pwd_success);

        mBackToLoginTv = this.findViewById(R.id.back_login_tv);
        mBackToLoginTv.setText(Html.fromHtml("<u>"+getResources().getString(R.string.activity_find_pwd_back_txt)+"</u>"));
        mBackToLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindPwdSuccessActivity.this.finish();
            }
        });
    }
}
