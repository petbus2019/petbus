package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class actionfragment_diary extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.actionfragment_diary, container, false);
        return view;
    }
}
