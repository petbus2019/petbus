package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

class action_record{
    public action_record( int id, int type ){
        m_id = id;
        m_type = type;
    }

    int get_record_type(){
        return m_type;
    }
    int get_record_id(){
        return m_id;
    }
    
    private int m_id;
    private int m_type;
}

class record_daily_listview extends ArrayAdapter<action_record> {
    public static final int RECORD_TYPE_DATE = 0;
    public static final int RECORD_TYPE_RECORD = 1;

    private middleware m_middleware;
    private int resourceID;
    private Context m_Context;
    private DisplayMetrics mDisplayMetrics;
    private List<action_record> mList;

    public record_daily_listview(Context context, int textViewResourceId,List<action_record>objects){
        super(context,textViewResourceId,objects);
        m_Context = context;
        resourceID = textViewResourceId;
        mDisplayMetrics = m_Context.getResources().getDisplayMetrics();
        m_middleware = middleware_impl.getInstance();
        mList = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        action_record record = getItem(position);
        Log.i( "PetBusApp", "PetBus:getView " + record );

        view_holder_date date_view = null;
        view_holder_record record_view = null;

        if( convertView == null ){
            switch( record.get_record_type() ){
                case RECORD_TYPE_DATE:
                    date_view = new view_holder_date();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_date
                                                                           , parent, false);
                    TextView date = (TextView) convertView.findViewById(R.id.daily_text);
                    convertView.setTag(R.layout.record_date, date_view);
                    date.setText( "2019052" + record.get_record_id() + " XX" );
                    break;
                case RECORD_TYPE_RECORD:
                    record_view = new view_holder_record();
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_record
                                                                           , parent, false); 
                    convertView.setTag(R.layout.record_record, record_view); 
                    TextView name = (TextView) convertView.findViewById(R.id.record_times);
                    break;
            }
        } else {
            switch( record.get_record_type() ){
                case RECORD_TYPE_DATE:
                    date_view = (view_holder_date)convertView.getTag(R.layout.record_date);
                    break;
                case RECORD_TYPE_RECORD:
                    record_view = (view_holder_record)convertView.getTag(R.layout.record_record);
                    break;
            }
        }

        return convertView;
    }

    private class view_holder_date {
    }
    private class view_holder_record{
    }
} 


public class actionfragment_actionrecord extends Fragment implements OnClickListener
{
    private List<action_record> m_daily_record_list = new ArrayList<>();
    private middleware m_middleware;
    private ui_interface m_tigger;

    private void init_list_data(){
        int i = 0;

        for( i = 0;i < 7;i += 3  )
        {
            action_record tmp = new action_record( i, 0 );
            m_daily_record_list.add(tmp);
            tmp = new action_record( i + 1, 1 );
            m_daily_record_list.add(tmp);
            tmp = new action_record( i + 2, 1 );
            m_daily_record_list.add(tmp);
        }
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "actionfragment_actionrecord:onClick" );
        switch( view.getId() )
        {
            case R.id.add_action_button:
                Log.i( "PetBusApp", "add_action_button" );
                m_tigger.trigger_change( ui_interface.SETTINGFRAMGENT_ID );
                break;
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState ){
        m_middleware = middleware_impl.getInstance();
        init_list_data();

        View view = inflater.inflate(R.layout.actionfragment_actionrecord, container, false);
        ArrayAdapter<action_record> adapter = new record_daily_listview(this.getActivity(),
                R.layout.record_daily_record,m_daily_record_list);
 
        ListView listView =(ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        Log.i( "PetBusApp", "PetBus:getView " + m_daily_record_list );

        Button button = ( Button )view.findViewById( R.id.add_action_button );
        button.setOnClickListener(this);

        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_tigger = (ui_interface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
