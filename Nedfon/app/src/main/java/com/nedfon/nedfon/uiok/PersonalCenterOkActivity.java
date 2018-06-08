package com.nedfon.nedfon.uiok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DbDean;
import com.nedfon.nedfon.bean.DeviceInfo;
import com.nedfon.nedfon.bean.DeviceInfoAll;
import com.nedfon.nedfon.bean.GetPersonInfoAllBean;
import com.nedfon.nedfon.db.MyDBHelper;
import com.nedfon.nedfon.ui.LoginActivity;
import com.nedfon.nedfon.ui.ResetPwdActivity;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.FormEncodingBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PersonalCenterOkActivity extends BaseTopBottomActivity implements View.OnClickListener {


    private RelativeLayout mExitRl;
    private ImageView mHeadIconIv;
    private TextView mUserNameTv;
    private TextView mPhoneNumberTv;
    private RelativeLayout mInfoRl,mMsgRl,mUpdatePwdRl;
    private ImageView mInfoIv,mMsgIv,mUpdatePwdIv;

    private ImageView mRedDot;

    private MyDBHelper myDBHelper;

//    private DeviceInfo info;

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
//        if(null == CommonUtils.bean){
//            doDeviceInfoGet(CommonUtils.token,CommonUtils.bean.deviceid);
//        }
//        info = CommonUtils.bean;

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
        mRedDot = this.findViewById(R.id.msg_info_red_iv);

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

//        mUserNameTv.setText(info.username);
//        mPhoneNumberTv.setText(info.userphone);

        myDBHelper = new MyDBHelper(this);

        doGetPersonInfoGet(CommonUtils.token);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_personal_center_exit_rl://退出
                if (myDBHelper.query().size()>0){
                    DbDean bean = myDBHelper.query().get(0);
                    myDBHelper.delete(bean.phone);
                    Intent login = new Intent(PersonalCenterOkActivity.this, LoginActivity.class);
                    startActivity(login);
                    PersonalCenterOkActivity.this.finish();
                }

                break;
            case R.id.fragment_personal_center_info_rl://个人信息RL
                if (CommonUtils.info == null){
                    ToastUtils.show(this,"当前网络不是很好哦");
                    doGetPersonInfoGet(CommonUtils.token);
                } else {
                    Intent information = new Intent(PersonalCenterOkActivity.this, MyInformationOkActivity.class);
                    startActivity(information);
                }
                break;
            case R.id.fragment_personal_center_msg_rl://消息中心RL
                Intent msgcenter = new Intent(PersonalCenterOkActivity.this, MessageCenterOkActivity.class);
                startActivity(msgcenter);
                this.finish();
                break;
            case R.id.fragment_personal_center_update_pwd_rl://修改密码RL
                Intent resetpwd = new Intent(PersonalCenterOkActivity.this, ResetPwdActivity.class);
                startActivity(resetpwd);
                this.finish();
                break;
            case R.id.fragment_personal_center_info_right_iv://个人中心IV
                Intent information1 = new Intent(PersonalCenterOkActivity.this, MyInformationOkActivity.class);
                startActivity(information1);
                break;
            case R.id.fragment_personal_center_msg_right_iv://消息中心IV
                Intent msgcenter1 = new Intent(PersonalCenterOkActivity.this, MessageCenterOkActivity.class);
                startActivity(msgcenter1);
                this.finish();
                break;
            case R.id.fragment_personal_center_update_pwd_right_iv://修改密码IV
                Intent resetpwd1 = new Intent(PersonalCenterOkActivity.this, ResetPwdActivity.class);
                startActivity(resetpwd1);
                this.finish();
                break;
            default:
                break;
        }
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
    /**
     *   个人信息资料的获取
     */
    private  void doGetPersonInfoGet(String token){
        //1.拿到OkHttpClient对象
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        // http://localhost:9090/mobileapi/getPersonInfo?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTk5NTg5MjksInVzZXJuYW1lIjoi
        // MTM1MTI3NzQ3NjAifQ.DP22dBsyMqnPoQyMw0KV51WN_OImBxI8rfphBS-eWfs
        Request request = builder.url(CommonUtils.localhost+"mobileapi/getPersonInfo?token="+token).get().build();
        executeGetPersonInfoRequest(request);
    }

    private GetPersonInfoAllBean mBean = null;

    private void executeGetPersonInfoRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call request,Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
                if (res.contains(CommonUtils.mSuccess)){
                    mBean = new Gson().fromJson(res,GetPersonInfoAllBean.class);
                    mHandler.sendEmptyMessage(3);
                } else if (res.contains(CommonUtils.mFailed)){
                    mHandler.sendEmptyMessage(1);
                } else {
                    mHandler.sendEmptyMessage(2);
                }
            }
        });
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtils.show(PersonalCenterOkActivity.this,"获取个人信息失败！");
                    break;
                case 2 :
                    ToastUtils.show(PersonalCenterOkActivity.this,"其他错误");
                    break;
                case 3 :
                    initData();
                    break;
            }
        }
    };

    private void initData() {
        CommonUtils.info = mBean.data;
        mUserNameTv.setText(mBean.data.nickname);
        mPhoneNumberTv.setText(mBean.data.phone);

        if (mBean.data.loginCount!=0 && !"".equals(mBean.data.loginCount+"")){
            CommonUtils.mIsShowRedPoint = true;
            setShowRedDot();
        } else {
            CommonUtils.mIsShowRedPoint = false;
            setShowRedDot();
        }

        if (CommonUtils.mIsShowRedPoint){
            mRedDot.setVisibility(View.VISIBLE);
        } else {
            mRedDot.setVisibility(View.GONE);
        }
    }
}
