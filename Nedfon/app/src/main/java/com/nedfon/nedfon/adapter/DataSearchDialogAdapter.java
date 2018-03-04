package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nedfon.nedfon.R;

public class DataSearchDialogAdapter extends BaseAdapter {

    private String[] mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public DataSearchDialogAdapter(Context context,String[] list){
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mList == null){
            return 0;
        }
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        if (mList == null){
            return null;
        }
        return mList[position];
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
        String mData = mList[position];
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_dialog_data_search,null);
            mHolder = new ViewHolder();
            mHolder.mTime = (TextView) convertView.findViewById(R.id.item_dialog_data_search_tv);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTime.setText(mData);
        return convertView;
    }

    class ViewHolder{
        private TextView mTime;
    }
}
