package com.nedfon.nedfon.uiok;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.utils.NetWorkUtils;
import com.nedfon.nedfon.utils.StatusBarCompat;
import com.nedfon.nedfon.utils.ToastUtils;

public abstract class BaseBottomActivity extends AppCompatActivity {

    public Context context;

    private LinearLayout ll_datasearch;
    private LinearLayout ll_setinternet;
    private LinearLayout ll_devicebind;
    private LinearLayout ll_personalcenter;

    private ImageView mDataQueryIv,mDeviceSetInternetIv,mDeviceBindIv,mPersonalInfoIv;

    public static String NAME = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //4.4以上的版本都可以将状态栏设置成透明颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            StatusBarCompat.setStatusBardown5AndSetColor(this,"#21D2B5");
        }
        setContentView(getLayoutRes());


        ll_datasearch = (LinearLayout) findViewById(R.id.ll_datasearch);
        ll_setinternet = (LinearLayout) findViewById(R.id.ll_setinternet);
        ll_devicebind = (LinearLayout) findViewById(R.id.ll_devicebind);
        ll_personalcenter = (LinearLayout) findViewById(R.id.ll_personalcenter);

        mDataQueryIv = this.findViewById(R.id.data_query_iv);
        mDeviceSetInternetIv = this.findViewById(R.id.device_set_internet_iv);
        mDeviceBindIv = this.findViewById(R.id.device_bind_iv);
        mPersonalInfoIv = this.findViewById(R.id.personal_info_iv);

        ll_datasearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ll_setinternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NAME.equals(DeviceInternetHotOkActivity.class.getSimpleName()) && !NAME.equals(DeviceInternetWifiOkActivity.class.getSimpleName()))
                    isWIFIOrOther();
            }
        });
        ll_devicebind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ooooooooooooooooo","base = "+NAME+"  devicebind = "+DeviceBindOkActivity.class.getSimpleName());
                if (NAME.equals(DeviceBindOkActivity.class.getSimpleName())){
                    return;
                }
                Intent deviceBind = new Intent(BaseBottomActivity.this,DeviceBindOkActivity.class);
                startActivity(deviceBind);
                BaseBottomActivity.this.finish();
            }
        });
        ll_personalcenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ooooooooooooooooo","base = "+NAME+"  devicebind = "+PersonalCenterOkActivity.class.getSimpleName());
                if (NAME.equals(PersonalCenterOkActivity.class.getSimpleName())){
                    return;
                }
                Intent deviceBind = new Intent(BaseBottomActivity.this,PersonalCenterOkActivity.class);
                startActivity(deviceBind);
                BaseBottomActivity.this.finish();
            }
        });

        viewdefualtStatus();
    }

    public void setImage(int id){
        switch (id){
            case 1:
                mDataQueryIv.setImageResource(R.drawable.data_query_selected);
                break;
            case 2:
                mDeviceSetInternetIv.setImageResource(R.drawable.set_internet_selected);
                break;
            case 3:
                mDeviceBindIv.setImageResource(R.drawable.device_bind_selected);
                break;
            case 4:
                mPersonalInfoIv.setImageResource(R.drawable.my_icon_selected);
                break;
            default:
                break;
        }
    }

    protected abstract int getLayoutRes();

    private void isWIFIOrOther(){
        int type = NetWorkUtils.getAPNType(BaseBottomActivity.this);
        if (type == 1) {
            ToastUtils.show(BaseBottomActivity.this, "当前属于连接WiFi状态");
            Intent wifi = new Intent(BaseBottomActivity.this, DeviceInternetWifiOkActivity.class);
            startActivity(wifi);
            BaseBottomActivity.this.finish();
            return;
        } else if(type == 0) {
            ToastUtils.show(BaseBottomActivity.this, "当前网络不可用");//TODO 使用该软件需要连接网络
            AlertDialog.Builder builder = new AlertDialog.Builder(BaseBottomActivity.this);
            builder.setTitle("提示").setMessage("您已经进入到了没有网络的异次元，请打开您的网络或者连接WiFi。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (type == 2 || type == 3){
            ToastUtils.show(BaseBottomActivity.this, "3G网络 或者 2G网络");
            Intent hot = new Intent(BaseBottomActivity.this, DeviceInternetHotOkActivity.class);
            startActivity(hot);
            BaseBottomActivity.this.finish();
        }else {
            ToastUtils.show(BaseBottomActivity.this,"当前不知道什么网络");
        }
    }

    private void viewdefualtStatus(){
        mDataQueryIv.setImageResource(R.drawable.data_query);
        mDeviceSetInternetIv.setImageResource(R.drawable.set_internet_unselect);
        mDeviceBindIv.setImageResource(R.drawable.device_bind_unselect);
        mPersonalInfoIv.setImageResource(R.drawable.my_icon_unselect);
    }

    protected abstract void setBackOnClick();

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        System.out.println("按下了back键   onBackPressed()");
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
//            System.out.println("按下了back键   onKeyDown()");
            setBackOnClick();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
