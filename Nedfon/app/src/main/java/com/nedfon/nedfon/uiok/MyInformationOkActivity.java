package com.nedfon.nedfon.uiok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.view.SexChoicePopupWindow;
import com.citypicker.CityPickerActivity;

public class MyInformationOkActivity extends BaseBottomActivity implements View.OnClickListener {

    private TextView mBackTv,mFinishTv;
    private TextView mPhoneumberTv;
    private EditText mNameEt;
    private TextView mAddressTv,mSexTv;
    private ImageView mAddressIv,mSexIv;


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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_my_information_back_tv://返回
                setBackOnClick();
                break;
            case R.id.activity_my_information_finish_tv://完成
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
        SexChoicePopupWindow takePhotoPopWin = new SexChoicePopupWindow(this);
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
}
