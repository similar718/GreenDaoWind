package com.nedfon.nedfon.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nedfon.nedfon.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 我的
 * Created by zst on 2017/3/2.
 */

public class DataSearchFragment extends Fragment {
    private List<HashMap<String, Object>> dataSourceList = new ArrayList<HashMap<String, Object>>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data_search, container, false);


        return view;
    }
}
