package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.widget.AdapterView; 
import android.widget.AdapterView.OnItemClickListener;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.Resources;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

class string_bitmap_pair{
    public string_bitmap_pair( String text , Bitmap pic ){
        m_text = text;
        m_bitmap = pic;
    }

    Bitmap get_bitmap() {
        return m_bitmap;
    }

    String get_text() {
        return m_text;
    }

    private String m_text;
    private Bitmap m_bitmap;
}

class action_record{
    public action_record( int id, int type ){
        m_id = id;
        m_type = type;
        Log.i( "PetBusApp", "PetBus:new action_record id:" + id + "type:" + type );
    }

    public void set_record( String time, List<string_bitmap_pair> list, string_bitmap_pair action, string_bitmap_pair remark ){
        Log.i( "PetBusApp", "record:" + time + "--" + action + "--" + remark );
        m_fulltime = time;

        DateFormat format_date = new SimpleDateFormat( middleware.DATE_FORMAT_DATE );
        java.util.Date date = null;
        try{
            date = format_date.parse(m_fulltime);
        }
        catch( ParseException e ){
            e.printStackTrace();
        }
        m_date = format_date.format(date);

        DateFormat format_time = new SimpleDateFormat( middleware.DATE_FORMAT_FULL );
        java.util.Date date_time = null;
        try{
            date_time = format_time.parse(m_fulltime);
        }
        catch( ParseException e ){
            e.printStackTrace();
        }
        DateFormat time_format = new SimpleDateFormat( "HH:mm" );
        m_time = time_format.format(date_time);

        m_action = action;
        m_petlist = list;
        m_remark = remark;

        return ;
    }

    public String get_full_time(){
        return m_fulltime;
    }

    public String get_action_text(){
        return m_action.get_text();
    }

    public String get_remark_text(){
        return m_remark.get_text();
    }

    public Bitmap get_remark_image(){
        return m_remark.get_bitmap();
    }

    public List<string_bitmap_pair> get_petlist(){
        return m_petlist;
    }

    public String get_date(){
        return m_date;
    }

    public String get_time(){
        return m_time;
    }

    public int get_record_type(){
        return m_type;
    }
    public int get_record_id(){
        return m_id;
    }
    
    private int m_id;
    private int m_type;
    private String m_fulltime;
    private String m_time;
    private String m_date;
    private string_bitmap_pair m_action;
    private string_bitmap_pair m_remark;
    List<string_bitmap_pair> m_petlist;
}

class record_daily_listadapter extends ArrayAdapter<action_record> {

    private int resourceID;
    private Context m_Context;
    private DisplayMetrics mDisplayMetrics;
    private List<action_record> mList;

    public record_daily_listadapter( Context context, int textViewResourceId,List<action_record>objects ){
        super(context,textViewResourceId,objects);
        m_Context = context;
        resourceID = textViewResourceId;
        mDisplayMetrics = m_Context.getResources().getDisplayMetrics();
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

    private View get_view_for_date( action_record record, ViewGroup parent ) {
        View convertView;
        view_holder_date date_view = null;
        date_view = new view_holder_date();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_date
                                                               , parent, false);
        date_view.m_date_text = (TextView) convertView.findViewById(R.id.daily_text);
        convertView.setTag(R.layout.record_date, date_view);

        return convertView;
    }
    private View get_view_for_record( action_record record ,ViewGroup parent ) {
        View convertView;
        view_holder_record record_view = null;
        record_view = new view_holder_record();
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.record_record
                                                               , parent, false);
        convertView.setTag(R.layout.record_record, record_view);
        record_view.m_record_times = (TextView) convertView.findViewById(R.id.record_times);
        record_view.m_record_action = (TextView) convertView.findViewById(R.id.recode_actioin_text);
        record_view.m_record_image = (ImageView) convertView.findViewById(R.id.record_action_icon);

        ImageView image_1 = (ImageView) convertView.findViewById(R.id.recode_image_1);
        record_view.m_image_list.add( image_1 );
        ImageView image_2 = (ImageView) convertView.findViewById(R.id.recode_image_2);
        record_view.m_image_list.add( image_2 );

        ImageView image_3 = (ImageView) convertView.findViewById(R.id.recode_image_3);
        record_view.m_image_list.add( image_3 );

        ImageView image_4 = (ImageView) convertView.findViewById(R.id.recode_image_4);
        record_view.m_image_list.add( image_4 );

        record_view.m_remark_text = (TextView) convertView.findViewById(R.id.recode_remark_text);
        record_view.m_remark_image = ( ImageView )convertView.findViewById( R.id.recode_petportrait_image );

        return convertView;
    }

    private void update_holder_by_record( view_holder_record holder, action_record record ) {
        String action_text = record.get_action_text(); 
        holder.m_record_times.setText( record.get_time() );
        holder.m_record_action.setText( record.get_action_text() );
        if( action_text.equals("喂食") ) {
            holder.m_record_image.setImageDrawable(m_Context.getResources().getDrawable((R.mipmap.fade)));
        }
        else if( action_text.equals("铲屎") ) {
            holder.m_record_image.setImageDrawable(m_Context.getResources().getDrawable((R.mipmap.shit)));
        }
        else if( action_text.equals("洗澡") ) {
            holder.m_record_image.setImageDrawable(m_Context.getResources().getDrawable((R.mipmap.shower)));
        }
        else if( action_text.equals("遛弯") ) {
            holder.m_record_image.setImageDrawable(m_Context.getResources().getDrawable((R.mipmap.running)));
        }

        holder.disable_all_list();
        List<string_bitmap_pair> list = record.get_petlist();
        for( int i = 0;i < list.size();i ++ ) {
            ImageView pet_photo_view = holder.m_image_list.get(i);
            Bitmap bitmap = list.get(i).get_bitmap();
            pet_photo_view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            pet_photo_view.setImageBitmap(bitmap);
            pet_photo_view.setVisibility( View.VISIBLE );
        }

        holder.m_remark_text.setText( record.get_remark_text() );
        holder.m_remark_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        holder.m_remark_image.setImageBitmap(record.get_remark_image());

        return;
    }
    
    // http://www.360doc.com/content/14/0917/15/15077656_410189820.shtml
    // https://cloud.tencent.com/developer/article/1331002
    // https://www.cnblogs.com/snake-hand/p/3206655.html
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        action_record record = getItem(position);

        switch( record.get_record_type() ){
            case middleware.RECORD_TYPE_DATE:
                if( null == convertView ) {
                    convertView = get_view_for_date( record, parent );
                } else {
                    Object test = convertView.getTag( R.layout.record_date );
                    if( null == test ) {
                        convertView = get_view_for_date( record, parent );
                    }
                }

                view_holder_date viewHolder = (view_holder_date)convertView.getTag( R.layout.record_date );
                String full_date_text = record.get_date() + "  " + getWeek( record.get_full_time() );
                viewHolder.m_date_text.setText( full_date_text );
//                getListView().setDividerHeight(30);
                break;
            case middleware.RECORD_TYPE_RECORD:
                if( null == convertView ) {
                    convertView = get_view_for_record( record, parent );
                } else {
                    Object test = convertView.getTag( R.layout.record_record );
                    if( null == test ) {
                        convertView = get_view_for_record( record, parent );
                    }
                }
                view_holder_record holder_record = (view_holder_record)convertView.getTag( R.layout.record_record );
                update_holder_by_record( holder_record, record );
                break;
        }

        return convertView;
    }
    @Override public int getCount() { 
        return mList.size();
    }
    @Override public action_record getItem(int position) { 
        return mList.get( position );
    }
    @Override public long getItemId(int position) {
        return position;
    }

    private class view_holder_date {
        TextView m_date_text;
    }
    
    private class view_holder_record{
        public void disable_all_list(){

            int size = m_image_list.size();
            for (int i = 0; i < size; i++) {
                m_image_list.get(i).setVisibility( View.INVISIBLE );
            }
            return ;
        }
        TextView m_record_times;
        TextView m_record_action;
        ImageView m_record_image;
        TextView m_remark_text;
        ImageView m_remark_image;

        List<ImageView> m_image_list = new ArrayList<ImageView>();
    }
}


public class actionfragment_actionrecord extends Fragment implements OnClickListener
                                                                    ,OnItemClickListener
{
    public static final int SIZE_PER_PAGE = 6;
    private List<action_record> m_daily_record_list = new ArrayList<action_record>();
    private List<action_record> m_added_list = new ArrayList<action_record>();
    private middleware m_middleware;
    private ui_interface m_tigger;
    private record_daily_listadapter m_adapter;
    private ImageButton mBtn_profile;

    private TextView m_lastaction_1;
    private ImageView m_lastaction_1_pic;
    private TextView m_lastaction_2;
    private ImageView m_lastaction_2_pic;
    private TextView m_lastaction_3;
    private ImageView m_lastaction_3_pic;

    private ImageView m_pet_image;
    private TextView  m_pet_name;
    private actionlistview m_listview;

    private int m_load_number = SIZE_PER_PAGE;
    private boolean m_is_finished = false;

    private Handler m_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            boolean result = (boolean) msg.obj;
            // Log.i( "PetBusApp", "handleMessage£º " + result );
            switch(msg.what){
                case 0:
                    break;
                case 1:
                    m_listview.onFinishLoading( result );
                    m_daily_record_list.addAll( m_added_list );
                    m_adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
                }
            }
        };


    public void update_listdata(){
        int i = 0;
        Date now = new Date(System.currentTimeMillis());

        Map<String, String> action_map =  new HashMap<>();
        m_middleware.get_last_three_record( action_map );

        String text_null = getActivity().getResources().getString( R.string.nullstring );
        m_lastaction_1.setText( text_null );
        m_lastaction_2.setText( text_null );
        m_lastaction_3.setText( text_null );
        
        Map<String,Object> pet_info = m_middleware.get_current_pet();

        Bitmap bitmap = BitmapFactory.decodeFile( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_PHOTO ) ) );
        m_pet_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        m_pet_image.setImageBitmap(bitmap);

        m_pet_name.setText(  String.valueOf( pet_info.get( middleware.PETINFO_TYPE_NAME ) ) );

        for (Map.Entry<String, String> entry : action_map.entrySet()) {
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
                if( entry.getKey().equals("喂食") ) {
                    m_lastaction_1_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.food_sign)));
                }
                else if( entry.getKey().equals("铲屎") ) {
                    m_lastaction_1_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shit_sign)));
                }
                else if( entry.getKey().equals("洗澡") ) {
                    m_lastaction_1_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shower_sign)));
                }
                else if( entry.getKey().equals("遛弯") ) {
                    m_lastaction_1_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.foot_sign)));
                }
            }
            else if( 1 == i )
            {
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
                if( entry.getKey().equals("喂食") ) {
                    m_lastaction_2_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.food_sign)));
                }
                else if( entry.getKey().equals("铲屎") ) {
                    m_lastaction_2_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shit_sign)));
                }
                else if( entry.getKey().equals("洗澡") ) {
                    m_lastaction_2_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shower_sign)));
                }
                else if( entry.getKey().equals("遛弯") ) {
                    m_lastaction_2_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.foot_sign)));
                }
            }
            else if( 2 == i )
            {
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
            if( entry.getKey().equals("喂食") ) {
                    m_lastaction_3_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.food_sign)));
                }
                else if( entry.getKey().equals("铲屎") ) {
                    m_lastaction_3_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shit_sign)));
                }
                else if( entry.getKey().equals("洗澡") ) {
                    m_lastaction_3_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.shower_sign)));
                }
                else if( entry.getKey().equals("遛弯") ) {
                    m_lastaction_3_pic.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.foot_sign)));
                }
            }
            i++;
        }
        m_daily_record_list.clear();
        m_listview.reset();
    }

    public void onClick( View view ) {
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

    private String get_remark_thumbnail_picture( String src_path ){
        if( !src_path.isEmpty() || src_path == null ){
            return src_path;
        }
        int dot = src_path.lastIndexOf('.');
        String prefix = src_path.substring( 0,dot );
        String thumbnail_file_name = prefix + "_thumbnail.jpg";
        return thumbnail_file_name;
    }

    private action_record update_item( int position ){
        StringBuffer time = new StringBuffer();
        StringBuffer action = new StringBuffer();
        StringBuffer remark = new StringBuffer();
        List<Integer> pet_list = new ArrayList<Integer>();
        ArrayList<String> record_pic = new ArrayList<String>();
        int type = m_middleware.get_record( position, time, pet_list, action, remark,record_pic );
        
        Log.i( "PetBusApp", "update_item: " + position );
        action_record record = new action_record( position, type );
        List<string_bitmap_pair> pet_info_list = new ArrayList<string_bitmap_pair>();
        for( int i = 0;i < pet_list.size();i ++ )
        {
            Map<String,Object> pet_info = m_middleware.getPetInfo( pet_list.get(i) + 1 );
            // Log.i( "PetBusApp", "pet_list: " + pet_list.get(i) + ":-:" + String.valueOf( pet_info.get( middleware.PETINFO_TYPE_PHOTO ) ) );
            Bitmap bitmap = BitmapFactory.decodeFile( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_PHOTO ) ) );
            String nic_name = String.valueOf( pet_info.get( middleware.PETINFO_TYPE_NAME ) );
            string_bitmap_pair tmp = new string_bitmap_pair( String.valueOf( pet_info.get( middleware.PETINFO_TYPE_NAME ) ), bitmap );
            pet_info_list.add( tmp );
        }

        String src_path = record_pic.get(0);
        Bitmap remark_bitmap = null;
        // if( "" == src_path || "null" == src_path )
        // {
        //     return record;
        // }
        if( src_path.isEmpty() ){
            Log.i("PetBusApp", "PetBus hehe:(" + src_path + ")");
            src_path = "null";
        }
        if( null != get_remark_thumbnail_picture( src_path ) )
        {
            remark_bitmap = BitmapFactory.decodeFile( get_remark_thumbnail_picture( src_path ) );
        }
        string_bitmap_pair remark_pair = new string_bitmap_pair( remark.toString(),remark_bitmap );
        // Log.i( "PetBusApp", "remark: " + remark.toString() + "--" + get_remark_thumbnail_picture( src_path ) );

        /*
        ToDo:这里需要根据不同的action的图标来转换。
        */
        string_bitmap_pair action_pair = new string_bitmap_pair( action.toString(),null );
        record.set_record( time.toString(), pet_info_list, action_pair, remark_pair );
        return record;
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.e("PetBusApp", "PetBus onStart~~~");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.e("PetBusApp", "actionrecord:onPause~~~");
    }
 
    
    @Override
    public void onStop() {
        super.onStop();
        m_middleware.loadrecord( -1 );
        // m_daily_record_list.clear();
        // m_adapter.notifyDataSetChanged();
        Log.e("PetBusApp", "actionrecord:onStop~~~");
    }
 
    
    //销毁Fragment所包含的View组件时，调用
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("PetBusApp", "actionrecord:onDestroyView~~~");
    }
    
 
    //销毁Fragment时调用
    public void onDestroy() {
        super.onDestroy();
        Log.e("PetBusApp", "actionrecord:onDestroy~~~");
    }

    @Override  
    public void onCreate(Bundle savedInstanceState)
    {  
        super.onCreate(savedInstanceState);
        Log.e( "PetBusApp", "actionrecord:onCreate" );
        m_middleware = middleware_impl.getInstance();

        m_adapter = new record_daily_listadapter(this.getActivity(),
                R.layout.record_daily_record,m_daily_record_list);
    }
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState ){
        Log.e( "PetBusApp", "actionrecord:onCreateView" );

        View view = inflater.inflate(R.layout.actionfragment_actionrecord, container, false);

        m_pet_image = (ImageView)view.findViewById(R.id.pet_photo);
        m_pet_name = (TextView)view.findViewById(R.id.pet_nickname_text);
 
        m_lastaction_1 = (TextView)view.findViewById(R.id.text_lastaction1);
        m_lastaction_1_pic = (ImageView)view.findViewById(R.id.image_lastaction1);
        m_lastaction_2 = (TextView)view.findViewById(R.id.text_lastaction2);
        m_lastaction_2_pic = (ImageView)view.findViewById(R.id.image_lastaction2);
        m_lastaction_3 = (TextView)view.findViewById(R.id.text_lastaction3);
        m_lastaction_3_pic = (ImageView)view.findViewById(R.id.image_lastaction3);

        m_listview =(actionlistview)view.findViewById(R.id.list_view);
        m_listview.setOnItemClickListener(this);
        m_listview.setOnPullUpLoadMoreListener(new actionlistview.OnPullUpLoadMoreListener() {
            @Override
            public void onPullUpLoadMore() {
                new Thread(){
                    @Override
                    public void run() {
                        Message message = new Message();
                        int count_org = m_middleware.get_record_count();
                        int load_number = m_middleware.loadrecord( m_load_number );
                        Log.i( "PetBusApp", "1run--(" + m_load_number + ")---(" + load_number + ")(" + count_org + ")" );
                        int count = m_middleware.get_record_count();
                        Log.i( "PetBusApp", "2run--(" + m_load_number + 
                            ")---(" + load_number + ")(" + count_org + ")" + "(" + count + ")" );
                        m_added_list.clear();
                        for( int i = count_org;i < count;i++ ){
                            action_record record = update_item( i );
                            m_added_list.add( record );
                        }
                        if( count != -1 )
                        {
                            m_load_number = m_load_number > count? m_load_number:count;
                        }

                        message.what = 1;
                        message.obj = m_middleware.is_loadover();
                        m_handler.sendMessage(message);
                    }
                }.start();
            }
        });
        m_listview.setAdapter(m_adapter);
        m_listview.setDivider(null);

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
    public void onItemClick(AdapterView<?> var1, View var2, int position, long var4){
        return ;
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
        if( m_middleware.should_loadrecord( m_load_number ) ){
            m_listview.startloading();
        }
    }
}
