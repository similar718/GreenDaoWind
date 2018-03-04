package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DataSearchLeftBean;
import com.nedfon.nedfon.bean.DeviceBindBean_test;
import com.nedfon.nedfon.bean.SensorCollectionBean;

import java.util.List;

public class DataSearchLeftAdapter extends BaseAdapter {

    private List<SensorCollectionBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DataSearchLeftAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<SensorCollectionBean> list){
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
        SensorCollectionBean mData = mList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_data_search_left,null);
            mHolder = new ViewHolder();
            mHolder.mTime = (TextView) convertView.findViewById(R.id.dialog_left_time_tv);
            mHolder.mWendu = (TextView) convertView.findViewById(R.id.dialog_left_teampareture_tv);
            mHolder.mShidu = (TextView) convertView.findViewById(R.id.dialog_left_humidity_tv);
            mHolder.mPm25 = (TextView) convertView.findViewById(R.id.dialog_left_pm25_tv);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTime.setText(mData.datatime);
        mHolder.mWendu.setText(mData.temp+"°");
        mHolder.mShidu.setText(mData.sweet+"%");
        mHolder.mPm25.setText(mData.pm25);
        return convertView;
    }

    class ViewHolder{
        private TextView mWendu;
        private TextView mShidu;
        private TextView mPm25;
        private TextView mTime;
    }
}
