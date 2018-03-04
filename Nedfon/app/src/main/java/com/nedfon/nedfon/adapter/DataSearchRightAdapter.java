package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.DataSearchLeftBean;
import com.nedfon.nedfon.bean.DataSearchRightBean;
import com.nedfon.nedfon.bean.SensorDataBean;

import java.util.List;

public class DataSearchRightAdapter extends BaseAdapter {

    private List<SensorDataBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DataSearchRightAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<SensorDataBean> list){
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
        SensorDataBean mData = mList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_data_search_right,null);
            mHolder = new ViewHolder();
            mHolder.mTime = (TextView) convertView.findViewById(R.id.dialog_right_time_tv);
            mHolder.mDesc = (TextView) convertView.findViewById(R.id.dialog_right_desc_tv);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTime.setText(mData.sensorDataTime);
        mHolder.mDesc.setText(mData.sensorData);
        return convertView;
    }

    class ViewHolder{
        private TextView mDesc;
        private TextView mTime;
    }
}
