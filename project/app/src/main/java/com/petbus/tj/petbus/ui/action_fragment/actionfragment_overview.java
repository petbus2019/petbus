package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import android.util.Log;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;

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

    private middleware m_middleware;

    private Spinner m_spinner;
    private List<String> m_data_list;
    private LineGraphicView m_graphic_view;
    private int m_data_type = middleware.OVERVIEW_DATATYPE_YEAR;

    private Map<String,Double> get_overview_data_year(){
        Map<String,Double> result = new ArrayMap<String, Double>();
        String operation = m_spinner.getSelectedItem().toString();
        for( int i = 0;i < 12;i ++ ){
            long sysTime = System.currentTimeMillis();
            SimpleDateFormat sDateFormat = new SimpleDateFormat(String.format( "%02d", i + 1 ) );
            String time = sDateFormat.format(new Date(sysTime));
            Map<String,Integer> re_ = m_middleware.get_statistics( time, operation );
            Integer value = re_.get( operation );
            // Log.i( "PetBusApp", "PetBus:get_overview_data_year  :" + re_ );
            if( null == value )
            {
                result.put( time, 0.0 );
            }
            else
            {
                result.put( time, Double.valueOf( value ) );
            }
        }
        Log.i( "PetBusApp", "PetBus:get_overview_data_year :" + result );
        return result;
    }

    private void update_overview_data(){
        m_graphic_view.clearData();
        m_graphic_view.addData( 2018, 10, 10, 20, 30, 40 );
        m_graphic_view.addData( 2018, 11, 62, 64, 66, 66 );
        m_graphic_view.addData( 2018, 12, 72, 74, 76, 76 );
        m_graphic_view.addData( 2019, 1, 1, 2, 3, 4 );
        m_graphic_view.addData( 2019, 2, 20, 24, 26, 26 );
        m_graphic_view.addData( 2019, 3, 30, 34, 36, 36 );
        m_graphic_view.addData( 2019, 4, 40, 44, 46, 46 );
        m_graphic_view.addData( 2019, 5, 50, 55, 56, 56 );
        m_graphic_view.addData( 2019, 6, 60, 66, 66, 66 );
        return ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        m_middleware = middleware_impl.getInstance();

        View view = inflater.inflate(R.layout.actionfragment_overview, container, false);
        m_spinner = (Spinner) view.findViewById(R.id.spinner);
        m_graphic_view = (LineGraphicView) view.findViewById(R.id.tabview_id);
        List<String> data_list = new ArrayList<String>();
        data_list.add("按月统计");
        data_list.add("按年统计");
        ArrayAdapter arr_adapter = new ArrayAdapter<String>( this.getActivity(), R.layout.spinner_item, data_list );
        m_spinner.setAdapter(arr_adapter);
        m_spinner.setOnItemSelectedListener( this );

        update_overview_data();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
        // Toast.makeText( getActivity(), "你点击的是:" + position, 2000).show();
        update_overview_data();
        m_graphic_view.invalidate();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // String sInfo="什么也没选！";
        // Toast.makeText(getActivity(),sInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i( "PetBusApp", "PetBus:Overview onHiddenChanged :" + hidden );
        if( false == hidden ){
            update_overview_data();
            m_graphic_view.invalidate();
        }
    }
}
