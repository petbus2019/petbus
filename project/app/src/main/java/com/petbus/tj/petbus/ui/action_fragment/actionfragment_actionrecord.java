package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

class action_record{
    public action_record( int id, int type ){
        m_id = id;
        m_type = type;
        Log.i( "PetBusApp", "PetBus:new action_record id:" + id + "type:" + type );
    }

    public void set_record( String time, List<Integer> list, String action, String remark, String picture_path ){
        m_time = time;
        m_petlist = list;
        m_action = action;
        m_remark = remark;
        m_picture_path = picture_path;
        // Log.i( "PetBusApp", "record:" + time + "--" + nickname + "--" + action + "--" + remark + "--" + picture_path );
    }

    public String get_full_time(){
        return m_time;
    }

    public String get_date(){
        DateFormat format = new SimpleDateFormat( middleware.DATE_FORMAT_DATE );
        java.util.Date date = null;
        try{
            date = format.parse(m_time);
        }
        catch( ParseException e ){
            e.printStackTrace();
        }
        String str = format.format(date);
        Log.i( "PetBusApp", "get_date:" + str );

        return str;
    }

    public String get_time(){
        DateFormat format = new SimpleDateFormat( middleware.DATE_FORMAT_FULL );
        java.util.Date date = null;
        try{
            date = format.parse(m_time);
        }
        catch( ParseException e ){
            e.printStackTrace();
            return "0000";
        }
        DateFormat time_format = new SimpleDateFormat( middleware.DATE_FORMAT_TIME );
        String str = time_format.format(date);
        Log.i( "PetBusApp", "get_time:" + str );

        return str;
    }
    public List<Integer> get_petlist(){
        return m_petlist;
    }

    public String get_nickphoto(){
        return m_picture_path;
    }
    public String get_action(){
        return m_action;
    }
    public String get_remark(){
        return m_remark;
    }
    public String get_remark_picture(){
        return m_picture_path;
    }

    public int get_record_type(){
        return m_type;
    }
    public int get_record_id(){
        return m_id;
    }
    
    private int m_id;
    private int m_type;
    private String m_time;
    private List<Integer> m_petlist;
    private String m_action;
    private String m_remark;
    private String m_picture_path;
    
}

class record_daily_listview extends ArrayAdapter<action_record> {

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

    private String getWeek(String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat( middleware_impl.DATE_FORMAT_DATE );
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += m_Context.getResources().getString( R.string.sunday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += m_Context.getResources().getString( R.string.monday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += m_Context.getResources().getString( R.string.tuesday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += m_Context.getResources().getString( R.string.wednesday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += m_Context.getResources().getString( R.string.thursday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += m_Context.getResources().getString( R.string.friday );
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += m_Context.getResources().getString( R.string.saturday );
        }
        return Week;
    } 

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        action_record record = getItem(position);
        Log.i( "PetBusApp", "PetBus:getView " + record );

        view_holder_date date_view = null;
        view_holder_record record_view = null;

        switch( record.get_record_type() ){
            case middleware.RECORD_TYPE_DATE:
                date_view = new view_holder_date();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_date
                                                                       , parent, false);
                TextView date = (TextView) convertView.findViewById(R.id.daily_text);
                convertView.setTag(R.layout.record_date, date_view);
                String full_date_text = record.get_date() + "  " + getWeek( record.get_full_time() );
                date.setText( full_date_text );
                break;
            case middleware.RECORD_TYPE_RECORD:
                record_view = new view_holder_record();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_record
                                                                       , parent, false); 
                convertView.setTag(R.layout.record_record, record_view); 
                TextView record_times = (TextView) convertView.findViewById(R.id.record_times);
                record_times.setText( record.get_time() );

                TextView record_action = (TextView) convertView.findViewById(R.id.recode_actioin_text);
                record_action.setText( record.get_action() );
                
                List<Integer> pet_list = record.get_petlist();
                List<TextView> textview_list = new ArrayList<TextView>();
                List<ImageView> image_list = new ArrayList<ImageView>();


                Log.i( "PetBusApp", "PetBus:pet_list " + pet_list );

                TextView nickname_text_1 = (TextView) convertView.findViewById(R.id.nickname_text_1);
                textview_list.add( nickname_text_1 );
                ImageView nickname_image_1 = (ImageView) convertView.findViewById(R.id.recode_image_1);
                image_list.add( nickname_image_1 );
                nickname_image_1.setVisibility( View.INVISIBLE );
                nickname_text_1.setVisibility( View.INVISIBLE );
                
                TextView nickname_text_2 = (TextView) convertView.findViewById(R.id.nickname_text_2);
                textview_list.add( nickname_text_2 );
                ImageView nickname_image_2 = (ImageView) convertView.findViewById(R.id.recode_image_2);
                image_list.add( nickname_image_2 );
                nickname_image_2.setVisibility( View.INVISIBLE );
                nickname_text_2.setVisibility( View.INVISIBLE );

                TextView nickname_text_3 = (TextView) convertView.findViewById(R.id.nickname_text_3);
                textview_list.add( nickname_text_3 );
                ImageView nickname_image_3 = (ImageView) convertView.findViewById(R.id.recode_image_3);
                image_list.add( nickname_image_3 );
                nickname_image_3.setVisibility( View.INVISIBLE );
                nickname_text_3.setVisibility( View.INVISIBLE );

                TextView nickname_text_4 = (TextView) convertView.findViewById(R.id.nickname_text_4);
                textview_list.add( nickname_text_4 );
                ImageView nickname_image_4 = (ImageView) convertView.findViewById(R.id.recode_image_4);
                image_list.add( nickname_image_4 );
                nickname_image_4.setVisibility( View.INVISIBLE );
                nickname_text_4.setVisibility( View.INVISIBLE );


                for( int i = 0;i < pet_list.size();i ++ )
                {
                   Log.i( "PetBusApp", "PetBus:pet_list )))) " + i );
                    Map<String,Object> pet_info = m_middleware.getPetInfo( pet_list.get(i) + 1 );
                    TextView nickname = textview_list.get(i);
                    nickname.setText( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_NAME ) ) );

                    ImageView pet_photo_view = image_list.get(i);
                    Bitmap bitmap = BitmapFactory.decodeFile( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_PHOTO ) ) );
                    pet_photo_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    pet_photo_view.setImageBitmap(bitmap);
                    pet_photo_view.setVisibility( View.VISIBLE );
                    nickname.setVisibility( View.VISIBLE );
                }

                TextView remark_text = (TextView) convertView.findViewById(R.id.recode_remark_text);
                remark_text.setText( record.get_remark() );

                ImageView imageview_picture = ( ImageView )convertView.findViewById( R.id.recode_petportrait_image );
                if( null != record.get_remark_picture() )
                {
                    Bitmap bitmap = BitmapFactory.decodeFile(record.get_remark_picture());
                    imageview_picture.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageview_picture.setImageBitmap(bitmap);
                }
                break;
        }

        return convertView;
    }
    @Override public int getCount() { 
        return m_middleware.get_record_count();
    }

    @Override public action_record getItem(int position) { 
        
        StringBuffer time = new StringBuffer();
        StringBuffer action = new StringBuffer();
        StringBuffer remark = new StringBuffer();
        List<Integer> list = new ArrayList<Integer>();
        ArrayList<String> record_pic = new ArrayList<String>();
        int type = m_middleware.get_record( position, time, list, action, remark,record_pic );
        action_record record = new action_record( position, type );

        Log.i( "PetBusApp", "pet_list:" + list );

        record.set_record( time.toString(), list, action.toString(), remark.toString(), record_pic.get(0) );

        return record;
    }
    @Override public long getItemId(int position) {
        return position;
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
    private ArrayAdapter<action_record> m_adapter;
    private ImageButton mBtn_profile;

    private TextView m_lastaction_1;
    private TextView m_lastaction_2;
    private TextView m_lastaction_3;

    private ImageView m_pet_image;
    private TextView  m_pet_name;


    public void update_listdata(){
        int i = 0;
        Log.i( "PetBusApp", "update_listdata" );
        m_adapter.notifyDataSetChanged();
        Date now = new Date(System.currentTimeMillis());

        Map<String, String> action_map =  new HashMap<>();
        m_middleware.get_last_three_record( action_map );
        Log.i( "PetBusApp", "actionfragment_actionrecord:update_listdata" + action_map );

        String text_null = getActivity().getResources().getString( R.string.nullstring );
        m_lastaction_1.setText( text_null );
        m_lastaction_2.setText( text_null );
        m_lastaction_3.setText( text_null );
        
        Map<String,Object> pet_info = m_middleware.get_current_pet();
        Log.i( "PetBusApp","info:" + pet_info );

        Bitmap bitmap = BitmapFactory.decodeFile( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_PHOTO ) ) );
        m_pet_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        m_pet_image.setImageBitmap(bitmap);

        m_pet_name.setText(  String.valueOf( pet_info.get( middleware.PETINFO_TYPE_NAME ) ) );

        for (Map.Entry<String, String> entry : action_map.entrySet()) {
            Log.i("PetBusApp",entry.getKey() + ":" + entry.getValue());
            SimpleDateFormat format = new SimpleDateFormat( middleware_impl.DATE_FORMAT_FULL );
            Date d2 = null;
            try{
                d2 = format.parse(entry.getValue());
            }
            catch( ParseException e )
            {
                e.printStackTrace();
                return;
            }


            long diff = now.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
            long minutes = (diff-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);

            if( 0 == i )
            {
                Log.i("PetBusApp","actioind-1");
                if( days > 0 ){
                    String text = getActivity().getResources().getString( R.string.day_before );
                    m_lastaction_1.setText( entry.getKey() + ":" + String.valueOf(days) + text );
                }
                else if( hours > 0 ){
                    String text = getActivity().getResources().getString( R.string.hour_before );
                    m_lastaction_1.setText( entry.getKey() + ":" + String.valueOf(hours) + text );
                }
                else if( minutes > 0 ){
                    String text = getActivity().getResources().getString( R.string.mini_before );
                    m_lastaction_1.setText( entry.getKey() + ":" + String.valueOf(minutes) + text );
                }
                else{
                    String text = getActivity().getResources().getString( R.string.rightnow );
                    m_lastaction_1.setText( entry.getKey() + ":" + text );
                }
            }
            else if( 1 == i )
            {
                Log.i("PetBusApp","actioind-2");
                if( days > 0 ){
                    String text = getActivity().getResources().getString( R.string.day_before );
                    m_lastaction_2.setText( entry.getKey() + ":" + String.valueOf(days) + text );
                }
                else if( hours > 0 ){
                    String text = getActivity().getResources().getString( R.string.hour_before );
                    m_lastaction_2.setText( entry.getKey() + ":" + String.valueOf(hours) + text );
                }
                else if( minutes > 0 ){
                    String text = getActivity().getResources().getString( R.string.mini_before );
                    m_lastaction_2.setText( entry.getKey() + ":" + String.valueOf(minutes) + text );
                }
                else{
                    String text = getActivity().getResources().getString( R.string.rightnow );
                    m_lastaction_2.setText( entry.getKey() + ":" + text );
                }
            }
            else if( 2 == i )
            {
                Log.i("PetBusApp","actioind-3");
                if( days > 0 ){
                    String text = getActivity().getResources().getString( R.string.day_before );
                    m_lastaction_3.setText( entry.getKey() + ":" + String.valueOf(days) + text );
                }
                else if( hours > 0 ){
                    String text = getActivity().getResources().getString( R.string.hour_before );
                    m_lastaction_3.setText( entry.getKey() + ":" + String.valueOf(hours) + text );
                }
                else if( minutes > 0 ){
                    String text = getActivity().getResources().getString( R.string.mini_before );
                    m_lastaction_3.setText( entry.getKey() + ":" + String.valueOf(minutes) + text );
                }
                else{
                    String text = getActivity().getResources().getString( R.string.rightnow );
                    m_lastaction_3.setText( entry.getKey() + ":" + text );
                }
            }
            i++;
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
            case R.id.profile_buttom:
                Log.i( "PetBusApp", "petprofile_button" );
                Intent intent = new Intent(getActivity().getApplicationContext(),petbus_profile.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState ){
        m_middleware = middleware_impl.getInstance();

        View view = inflater.inflate(R.layout.actionfragment_actionrecord, container, false);
        m_adapter = new record_daily_listview(this.getActivity(),
                R.layout.record_daily_record,m_daily_record_list);

        m_pet_image = (ImageView)view.findViewById(R.id.pet_photo);
        m_pet_name = (TextView)view.findViewById(R.id.pet_nickname_text);
 
        m_lastaction_1 = (TextView)view.findViewById(R.id.text_lastaction1);
        m_lastaction_2 = (TextView)view.findViewById(R.id.text_lastaction2);
        m_lastaction_3 = (TextView)view.findViewById(R.id.text_lastaction3);

        ListView listView =(ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(m_adapter);
        Log.i( "PetBusApp", "PetBus:getView " + m_daily_record_list );

        Button button = ( Button )view.findViewById( R.id.add_action_button );
        button.setOnClickListener(this);
        mBtn_profile = (ImageButton) view.findViewById(R.id.profile_buttom);
        mBtn_profile.setOnClickListener(this);

        m_pet_image.setAdjustViewBounds(true);
        m_pet_image.setMaxHeight(200);
        m_pet_image.setMaxWidth(200);

        update_listdata();
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

    @Override
    public void onResume(){
        super.onResume();

        Log.i( "PetBusApp", "PetBus:onResume " );
    }
}
