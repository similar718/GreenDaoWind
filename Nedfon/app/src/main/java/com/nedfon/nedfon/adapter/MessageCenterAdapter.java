package com.nedfon.nedfon.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nedfon.nedfon.R;
import com.nedfon.nedfon.bean.MessageCenterBean_test;

import java.util.List;

public class MessageCenterAdapter extends BaseAdapter {

    private List<MessageCenterBean_test> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public MessageCenterAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<MessageCenterBean_test> list){
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
        MessageCenterBean_test mData = mList.get(position);
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_message_center,null);
            mHolder = new ViewHolder();
            mHolder.mTitle = (TextView) convertView.findViewById(R.id.item_message_center_title_txt);
            mHolder.mTime = (TextView) convertView.findViewById(R.id.item_message_center_time_txt);
            mHolder.mContent = (TextView) convertView.findViewById(R.id.item_message_center_content_txt);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTitle.setText(mData.title);
        mHolder.mTime.setText(mData.time);
        mHolder.mContent.setText(mData.content);
        return convertView;
    }

    class ViewHolder{
        private TextView mTitle;
        private TextView mTime;
        private TextView mContent;
    }
}
