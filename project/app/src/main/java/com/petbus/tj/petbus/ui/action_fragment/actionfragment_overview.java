package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.BaseAdapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

//https://www.cnblogs.com/guichun/p/4352546.html
class action_adapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
 
    public action_adapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }
 
    @Override
    public int getCount() {
        return mList.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.actionfragment_overview_selectitem, null);
        if( convertView != null )
        {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.action_text);
            _TextView1.setText(mList.get(position));
        }
        return convertView;
    }
}

public class actionfragment_overview extends Fragment
{
    private ArrayList<Double> m_ylist;
    private middleware m_middleware;

    private Spinner m_spinner;
    private List<String> m_data_list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.actionfragment_overview, container, false);
        m_spinner = (Spinner) view.findViewById(R.id.spinner);
        LineGraphicView tabview = (LineGraphicView) view.findViewById(R.id.tabview_id);

        m_middleware = middleware_impl.getInstance();

        m_ylist = new ArrayList<Double>();
        m_ylist.add((double) 2.103);
        m_ylist.add(4.05);
        m_ylist.add(6.60);
        m_ylist.add(3.08);
        m_ylist.add(4.32);
        m_ylist.add(2.0);
        m_ylist.add(5.0);

        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("05-19");
        xRawDatas.add("05-20");
        xRawDatas.add("05-21");
        xRawDatas.add("05-22");
        xRawDatas.add("05-23");
        xRawDatas.add("05-24");
        xRawDatas.add("05-25");
        xRawDatas.add("05-26");
        tabview.setData(m_ylist, xRawDatas, 8, 2);

        action_adapter arr_adapter = new action_adapter(this.getActivity(), m_middleware.get_action_list());
        m_spinner.setAdapter(arr_adapter);
        return view;
    }
}
