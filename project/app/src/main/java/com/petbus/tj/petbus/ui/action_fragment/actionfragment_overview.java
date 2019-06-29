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
import android.util.Log;

import java.util.ArrayList;
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
        Map<String,Double> result = new HashMap<String, Double>();
        String operation = m_spinner.getSelectedItem().toString();
        for( int i = 0;i < 12;i ++ ){
            String month = "2019-06";
            Map<String,Integer> re_ = m_middleware.get_statistics( month, operation);
            Integer value = re_.get( operation );
            // Log.i( "PetBusApp", "PetBus:get_overview_data_year  :" + re_ );
            if( null == value )
            {
                result.put( month, 0.0 );
            }
            else
            {
                result.put( month, Double.valueOf( value ) );
            }
        }
        return result;
    }

    private void update_overview_data(){
        Map<String, Double> data = get_overview_data_year();
        ArrayList<Double> ylist = new ArrayList<Double>( data.values() );
        ArrayList<String> xRawDatas = new ArrayList<String>( data.keySet() );

        Log.i( "PetBusApp", "PetBus:update_overview_data  :" + ylist + "111: " + xRawDatas );
        // ylist.add(2.0);
        // ylist.add(4.0);
        // ylist.add(6.0);
        // ylist.add(3.0);
        // ylist.add(4.0);
        // ylist.add(2.0);
        // ylist.add(5.0);
        // ylist.add(3.0);
        // ylist.add(4.0);
        // ylist.add(2.0);
        // ylist.add(5.0);
        // ylist.add(5.0);

        // xRawDatas.add("2019-01");
        // xRawDatas.add("2019-02");
        // xRawDatas.add("2019-03");
        // xRawDatas.add("2019-04");
        // xRawDatas.add("2019-05");
        // xRawDatas.add("2019-06");
        // xRawDatas.add("2019-07");
        // xRawDatas.add("2019-08");
        // xRawDatas.add("2019-09");
        // xRawDatas.add("2019-10");
        // xRawDatas.add("2019-11");
        // xRawDatas.add("2019-12");
        m_graphic_view.setData(ylist, xRawDatas, 8, 2);
        return ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        m_middleware = middleware_impl.getInstance();

        View view = inflater.inflate(R.layout.actionfragment_overview, container, false);
        m_spinner = (Spinner) view.findViewById(R.id.spinner);
        m_graphic_view = (LineGraphicView) view.findViewById(R.id.tabview_id);
        action_adapter arr_adapter = new action_adapter(this.getActivity(), m_middleware.get_action_list());
        m_spinner.setAdapter(arr_adapter);
        m_spinner.setOnItemSelectedListener( this );

        update_overview_data();

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter,View view,int position,long id){
        //获取选择的项的值 
        Toast.makeText( getActivity(), "你点击的是:" + position, 2000).show();
        update_overview_data();
        m_graphic_view.invalidate();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        String sInfo="什么也没选！";
        Toast.makeText(getActivity(),sInfo, Toast.LENGTH_LONG).show();
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
