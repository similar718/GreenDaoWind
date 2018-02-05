package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DeviceBindBean_test;
import com.nedfon.nedfon.bean.MessageCenterBean_test;

import java.util.List;

public class DeviceBindAdapter extends BaseAdapter {

    private List<DeviceBindBean_test> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DeviceBindAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<DeviceBindBean_test> list){
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
        DeviceBindBean_test mData = mList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_device_bind,null);
            mHolder = new ViewHolder();
            mHolder.mName = (TextView) convertView.findViewById(R.id.item_device_bind_name_tv);
            mHolder.mOnline = (TextView) convertView.findViewById(R.id.item_device_bind_online_tv);
            mHolder.mWendu = (TextView) convertView.findViewById(R.id.item_device_bind_wendu_tv);
            mHolder.mShidu = (TextView) convertView.findViewById(R.id.item_device_bind_shidu_tv);
            mHolder.mPm25 = (TextView) convertView.findViewById(R.id.item_device_bind_pm25_tv);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mName.setText(mData.name);
        mHolder.mOnline.setText(mData.online);
        mHolder.mWendu.setText(mData.wendu);
        mHolder.mShidu.setText(mData.shidu);
        mHolder.mPm25.setText(mData.pm25);
        return convertView;
    }

    class ViewHolder{
        private TextView mName;
        private TextView mOnline;
        private TextView mWendu;
        private TextView mShidu;
        private TextView mPm25;
    }
}
