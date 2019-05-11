package com.petbus.tj.petbus;

import com.petbus.tj.petbus.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class actionfragment_overview extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        // TODO Auto-generated method stub return inflater.inflate(R.layout.fragment1, container, false);
        View view = inflater.inflate(R.layout.actionfragment_overview, container, false);
        return view;
    }
}
