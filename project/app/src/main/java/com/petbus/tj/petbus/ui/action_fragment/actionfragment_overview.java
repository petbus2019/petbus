package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

public class actionfragment_overview extends Fragment implements OnItemSelectedListener
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
        m_ylist.add(3.08);
        m_ylist.add(4.32);
        m_ylist.add(2.0);
        m_ylist.add(5.0);
        m_ylist.add(5.0);
 


        ArrayList<String> xRawDatas = new ArrayList<String>();
        xRawDatas.add("2019-01");
        xRawDatas.add("2019-02");
        xRawDatas.add("2019-03");
        xRawDatas.add("2019-04");
        xRawDatas.add("2019-05");
        xRawDatas.add("2019-06");
        xRawDatas.add("2019-07");
        xRawDatas.add("2019-08");
        xRawDatas.add("2019-09");
        xRawDatas.add("2019-10");
        xRawDatas.add("2019-11");
        xRawDatas.add("2019-12");
        tabview.setData(m_ylist, xRawDatas, 8, 2);

        action_adapter arr_adapter = new action_adapter(this.getActivity(), m_middleware.get_action_list());
        m_spinner.setAdapter(arr_adapter);
        m_spinner.setOnItemSelectedListener( this );
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
        //获取选择的项的值 
        Toast.makeText( getActivity(), "你点击的是:" + position, 2000).show();
        Map<String, Integer> result = m_middleware.get_statistics( "2019-06", m_spinner.getSelectedItem().toString() );
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        String sInfo="什么也没选！";
        Toast.makeText(getActivity(),sInfo, Toast.LENGTH_LONG).show();
    }
}
