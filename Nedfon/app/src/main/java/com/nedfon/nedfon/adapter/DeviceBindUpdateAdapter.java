package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceInfo;

import java.util.List;

public class DeviceBindUpdateAdapter extends BaseAdapter {

    private List<DeviceInfo> mList;
    private Context mContext;
    private LayoutInflater mInflater;
    private OnItemDeviceOptionClickListener mClickListener;

    public DeviceBindUpdateAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<DeviceInfo> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList == null){
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (mList == null){
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mList == null){
            return 0;
        }
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        DeviceInfo mData = mList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_device_bind_update,null);
            mHolder = new ViewHolder();
            mHolder.mName = (TextView) convertView.findViewById(R.id.item_device_bind_name_tv);
            mHolder.mOnline = (TextView) convertView.findViewById(R.id.item_device_bind_online_tv);
            mHolder.mWendu = (TextView) convertView.findViewById(R.id.item_device_bind_wendu_tv);
            mHolder.mShidu = (TextView) convertView.findViewById(R.id.item_device_bind_shidu_tv);
            mHolder.mPm25 = (TextView) convertView.findViewById(R.id.item_device_bind_pm25_tv);
            //电源的开关
            mHolder.mFuliziRl = convertView.findViewById(R.id.fulizi_bottom_rl);
            mHolder.mFuliziKai = convertView.findViewById(R.id.activity_device_fulizi_kai_iv);
            mHolder.mFuliziGuan = convertView.findViewById(R.id.activity_device_fulizi_guan_iv);
            mHolder.mFuliziBg = convertView.findViewById(R.id.activity_device_fulizi_kaiguan_iv);
            //风速的高低
            mHolder.mFengRl = convertView.findViewById(R.id.high_di_rl);
            mHolder.mFengKai = convertView.findViewById(R.id.activity_device_high_di_kai_iv);
            mHolder.mFengGuan = convertView.findViewById(R.id.activity_device_high_di_guan_iv);
            mHolder.mFengBg = convertView.findViewById(R.id.activity_device_high_di_kaiguan_iv);
            //设备图片
            mHolder.device_img = convertView.findViewById(R.id.device_img);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mName.setText(mData.deviceid+"  "+mData.terminal);
        mHolder.mOnline.setText(mData.commStatus==0?"离线":"在线  联网中");
        mHolder.mWendu.setText(mData.intmp+"°");
        mHolder.mShidu.setText(mData.insweet+"%");
        mHolder.mPm25.setText(mData.inpm25+"");

        if (!"".equals(mData.useremail) && mData.useremail != null){ //服务器端有图片传过来
            //显示网络的图片
            Glide.with(mContext).load(mData.useremail).error(R.drawable.device_icon).into(mHolder.device_img);
        } else {
            //显示默认的图片
            Glide.with(mContext).load(R.drawable.device_icon).into(mHolder.device_img);
        }

        //电源开关
        if (mData.workmodel == 1){
            mHolder.mFuliziKai.setVisibility(View.VISIBLE);
            mHolder.mFuliziGuan.setVisibility(View.GONE);
            mHolder.mFuliziBg.setImageResource(R.drawable.kai1_icon);
        } else {
            mHolder.mFuliziKai.setVisibility(View.GONE);
            mHolder.mFuliziGuan.setVisibility(View.VISIBLE);
            mHolder.mFuliziBg.setImageResource(R.drawable.on_off_btn_bg);
        }
        //风速开关
        if (mData.workgear == 2){ //高
            mHolder.mFengKai.setVisibility(View.VISIBLE);
            mHolder.mFengGuan.setVisibility(View.GONE);
            mHolder.mFengBg.setImageResource(R.drawable.kai1_icon);
        } else if (mData.workgear == 1){ //低
            mHolder.mFengKai.setVisibility(View.GONE);
            mHolder.mFengGuan.setVisibility(View.VISIBLE);
            mHolder.mFengBg.setImageResource(R.drawable.on_off_btn_bg);
        } else {
            mHolder.mFengKai.setVisibility(View.GONE);
            mHolder.mFengGuan.setVisibility(View.VISIBLE);
            mHolder.mFengBg.setImageResource(R.drawable.on_off_btn_bg);
        }
        mHolder.mFuliziRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //电源开关的点击事件
                mClickListener.onItemPowerClick(position,mData);
            }
        });
        mHolder.mFengRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设备风速的点击事件
                mClickListener.onItemExhuastVentilationClick(position,mData);
            }
        });
        mHolder.device_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemDetailClick(position,mData);
            }
        });
        return convertView;
    }

    class ViewHolder{
        private TextView mName;//名称
        private TextView mOnline; //在线
        private TextView mWendu; //温度
        private TextView mShidu; //湿度
        private TextView mPm25; //PM2.5
        //设备的开关
        private RelativeLayout mFuliziRl;
        private ImageView mFuliziKai,mFuliziGuan,mFuliziBg;
        //设备的速度高低
        private RelativeLayout mFengRl;
        private ImageView mFengKai,mFengGuan,mFengBg;
        //设备的图片
        private ImageView device_img;
    }


    public void setOnItemDeviceOptionClickListener(OnItemDeviceOptionClickListener listener) {
        this.mClickListener = listener;
    }
    public interface OnItemDeviceOptionClickListener {
        void onItemPowerClick(int position,DeviceInfo data);
        void onItemExhuastVentilationClick(int position,DeviceInfo data);
        void onItemDetailClick(int position,DeviceInfo data);
    }
}
