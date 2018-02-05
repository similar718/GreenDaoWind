package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;

public class PersonalCenterOkActivity extends BaseTopBottomActivity implements View.OnClickListener {


    private RelativeLayout mExitRl;
    private ImageView mHeadIconIv;
    private TextView mUserNameTv;
    private TextView mPhoneNumberTv;
    private RelativeLayout mInfoRl,mMsgRl,mUpdatePwdRl;
    private ImageView mInfoIv,mMsgIv,mUpdatePwdIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_personal_center_ok;
    }

    @Override
    protected void setBackOnClick() {
        Intent deviceIntent = new Intent(PersonalCenterOkActivity.this,DeviceBindOkActivity.class);
        startActivity(deviceIntent);
        PersonalCenterOkActivity.this.finish();
    }

    public void initview() {
        mExitRl = this.findViewById(R.id.fragment_personal_center_exit_rl);
        mHeadIconIv = this.findViewById(R.id.fragment_personal_center_head_img);
        mUserNameTv = this.findViewById(R.id.fragment_personal_center_name_tv);
        mPhoneNumberTv = this.findViewById(R.id.fragment_personal_center_phone_number_tv);

        mInfoRl = this.findViewById(R.id.fragment_personal_center_info_rl);
        mMsgRl = this.findViewById(R.id.fragment_personal_center_msg_rl);
        mUpdatePwdRl = this.findViewById(R.id.fragment_personal_center_update_pwd_rl);

        mInfoIv = this.findViewById(R.id.fragment_personal_center_info_right_iv);
        mMsgIv = this.findViewById(R.id.fragment_personal_center_msg_right_iv);
        mUpdatePwdIv = this.findViewById(R.id.fragment_personal_center_update_pwd_right_iv);

        mExitRl.setOnClickListener(this);

        mInfoRl.setOnClickListener(this);
        mMsgRl.setOnClickListener(this);
        mUpdatePwdRl.setOnClickListener(this);
        mMsgIv.setOnClickListener(this);
        mUpdatePwdIv.setOnClickListener(this);
        mInfoIv.setOnClickListener(this);

        setTitleText(getResources().getString(R.string.fragment_personal_center_title_txt));
        NAME = PersonalCenterOkActivity.class.getSimpleName();
        setImage(4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_personal_center_exit_rl://退出 //TODO 我这边做数据库的操作
                break;
            case R.id.fragment_personal_center_info_rl://个人信息RL TODO
                Intent information = new Intent(PersonalCenterOkActivity.this, MyInformationOkActivity.class);
                startActivity(information);
                break;
            case R.id.fragment_personal_center_msg_rl://消息中心RL TODO
                Intent msgcenter = new Intent(PersonalCenterOkActivity.this, MessageCenterOkActivity.class);
                startActivity(msgcenter);
                break;
            case R.id.fragment_personal_center_update_pwd_rl://修改密码RL TODO
                break;
            case R.id.fragment_personal_center_info_right_iv://个人中心IV
                Intent information1 = new Intent(PersonalCenterOkActivity.this, MyInformationOkActivity.class);
                startActivity(information1);
                break;
            case R.id.fragment_personal_center_msg_right_iv://消息中心IV
                Intent msgcenter1 = new Intent(PersonalCenterOkActivity.this, MessageCenterOkActivity.class);
                startActivity(msgcenter1);
                break;
            case R.id.fragment_personal_center_update_pwd_right_iv://修改密码IV
                break;
            default:
                break;
        }
    }
}
