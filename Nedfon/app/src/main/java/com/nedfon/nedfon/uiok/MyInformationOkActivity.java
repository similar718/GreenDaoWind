package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.GetPersonInfoAllBean;
import com.nedfon.nedfon.bean.GetPersonInfoBean;
import com.nedfon.nedfon.utils.CommonUtils;
import com.nedfon.nedfon.utils.ToastUtils;
import com.nedfon.nedfon.view.SexChoicePopupWindow;
import com.citypicker.CityPickerActivity;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MyInformationOkActivity extends BaseBottomActivity implements View.OnClickListener {

    private TextView mBackTv,mFinishTv;
    private TextView mPhoneumberTv;
    private EditText mNameEt;
    private TextView mAddressTv,mSexTv;
    private ImageView mAddressIv,mSexIv;

    private GetPersonInfoBean bean = CommonUtils.info;


    private static final int REQUEST_CODE_PICK_CITY = 233;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NAME = DeviceOkActivity.class.getSimpleName();
        setImage(4);

        initView();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_information_ok;
    }

    @Override
    protected void setBackOnClick() {
        this.finish();
    }

    private void initView() {
        mBackTv = this.findViewById(R.id.activity_my_information_back_tv);
        mFinishTv = this.findViewById(R.id.activity_my_information_finish_tv);
        mPhoneumberTv = this.findViewById(R.id.activity_my_information_phone_number_tv);
        mNameEt = this.findViewById(R.id.activity_my_information_name_et);
        mAddressTv = this.findViewById(R.id.activity_my_information_address_tv);
        mSexTv = this.findViewById(R.id.activity_my_information_sex_tv);
        mAddressIv = this.findViewById(R.id.activity_my_information_address_right_iv);
        mSexIv = this.findViewById(R.id.activity_my_information_sex_right_iv);

        mBackTv.setOnClickListener(this);
        mFinishTv.setOnClickListener(this);
        mAddressIv.setOnClickListener(this);
        mSexIv.setOnClickListener(this);

        initData();
    }

    private void initData() {
        mPhoneumberTv.setText(bean.phone);
        mNameEt.setText(bean.nickname);
        mAddressTv.setText(bean.registerIp);
        if (bean.sex==0){ //女
            mSexTv.setText("女");
        } else {
            mSexTv.setText("男");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_my_information_back_tv://返回
                setBackOnClick();
                break;
            case R.id.activity_my_information_finish_tv://完成
                if ("".equals(mAddressTv.getText().toString()) || null == mAddressTv.getText().toString()){
                    ToastUtils.show(MyInformationOkActivity.this,"地址不能为空！");
                    return;
                }
                doUpdatePersonInfoGet(CommonUtils.token,mSexTv.getText().toString().equals("女")?0:1,mNameEt.getText().toString(),mAddressTv.getText().toString());
                break;
            case R.id.activity_my_information_address_right_iv://地址选择
                startActivityForResult(new Intent(MyInformationOkActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);
                break;
            case R.id.activity_my_information_sex_right_iv://性别选择
                showPopFormBottom();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
            if (data != null){
                String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
                mAddressTv.setText(city);
            }
        }
    }

    public void showPopFormBottom() {
        SexChoicePopupWindow takePhotoPopWin = new SexChoicePopupWindow(this,mSexTv.getText().toString().equals("女")?0:1);
        //  设置Popupwindow显示位置（从底部弹出）
        takePhotoPopWin.showAtLocation(findViewById(R.id.my_information_layout), Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);

        final WindowManager.LayoutParams[] lp = {getWindow().getAttributes()};
        lp[0].alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp[0]);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        takePhotoPopWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp[0] = getWindow().getAttributes();
                lp[0].alpha=1f;
                getWindow().setAttributes(lp[0]);

                int sex = SexChoicePopupWindow.sex;
                if (sex == 0) {
                    mSexTv.setText("男");
                } else {
                    mSexTv.setText("女");
                }
            }
        });
    }

    private static OkHttpClient okhttpclient = new OkHttpClient();
    /**
     *   个人信息资料的获取
     */
    private  void doUpdatePersonInfoGet(String token,int sex,String nickname,String address){
        //1.拿到OkHttpClient对象
        FormEncodingBuilder requestBodyBuilder = new FormEncodingBuilder();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
        //http://localhost:9090/mobileapi/updatePersonInfo?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1MTk5NTg5MjksInVzZXJuYW1lIjoiMTM1MTI3NzQ3NjAifQ.DP22dBsyMqnPoQyMw0KV51WN_OImBxI8rfphBS-eWfs
        // &sex=1&
        // nickname=test1&
        // registerIp=深圳
        Request request = builder.url(CommonUtils.localhost+"mobileapi/updatePersonInfo?token="+token+
        "&sex="+sex+"&nickname="+nickname+"&registerIp="+address).get().build();
        executeUpdatePersonInfoRequest(request);
    }

    private void executeUpdatePersonInfoRequest(Request request) {
        //3.将Request封装为Call
        Call call = okhttpclient.newCall(request);
        //异步使用CallBack  同步用call.execute()
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                final String res = response.body().string();
                Log.e("oooooooooo", "onResponse:  res = "+res );
//                if (res.contains(":1,")){
                    mHandler.sendEmptyMessage(3);
//                } else if (res.contains(":0,")){
//                    mHandler.sendEmptyMessage(1);
//                } else {
//                    mHandler.sendEmptyMessage(2);
//                }
            }
        });
    }
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtils.show(MyInformationOkActivity.this,"更改个人信息失败！");
                    break;
                case 2 :
                    ToastUtils.show(MyInformationOkActivity.this,"其他错误");
                    break;
                case 3 :
                    ToastUtils.show(MyInformationOkActivity.this,"更改个人信息成功！");
                    setBackOnClick();
                    break;
            }
        }
    };

}
