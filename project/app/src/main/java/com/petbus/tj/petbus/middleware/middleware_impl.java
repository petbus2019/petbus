package com.petbus.tj.petbus.middleware;

import com.petbus.tj.petbus.dbmanager.dbmanager;
import com.petbus.tj.petbus.dbmanager.dbmanager_impl;

import android.content.Context;
import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class middleware_impl extends Application implements middleware {
    private dbmanager m_database = null;
    private static middleware_impl m_instance = null;
    private ArrayList<String> m_action_list = new ArrayList<String>();
    private ArrayList<String> m_petname_list = new ArrayList<String>();
    public middleware_impl(){
        Log.i( "PetBusApp", "PetBusBusiness:middleware_impl" );
        if( null == m_instance )
        {
            m_instance = this;
        }
    }

    public static middleware_impl getInstance(){
        return m_instance;
    }

    public int new_recode( String time, String petname, String action, String remark, ArrayList<String> m_recode_pic ){
        String patten = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateFormatStr = format.format(new Date());

        Log.i( "PetBusApp", "PetBusBusiness:new_recode(" + time + ")-(" + petname + ")-(" + action + ")-(" + remark + ")-(" );
        String sql = "INSERT INTO " + dbmanager_impl.TABLE_RECODE + "(pet_id,picture,operation,time)"
                   + " values( 1," + "\"picture_name\"," + "\'喂食\',\'" + dateFormatStr + "\');";
        m_database.execute_sql( sql );
        return middleware_diaryrecode.MIDDLEWARE_RETURN_OK;
    }
    public ArrayList<String> get_action_list()
    {
        return m_action_list;
    }
    public ArrayList<String> get_petname_list()
    {
        return m_petname_list;
    }

    @Override
    public int get_petnumber()
    {
        Log.i( "PetBusApp", "PetBusBusiness:get_petnumber" );
        int re = m_database.get_petnumber();
        re = 10;
        return re;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String packageName = getPackageName();
        Log.d("PetBusApp", "PetBusBusiness:package name is " + packageName);
        m_database = new dbmanager_impl( getApplicationContext() ) ;
        Log.d("PetBusApp", "PetBusBusiness:the database is " + m_database );

        m_action_list.add("喂食");
        m_action_list.add("铲屎");
        m_action_list.add("洗澡");
        m_action_list.add("遛弯");

        m_petname_list.add( "喵喵1" );
        m_petname_list.add( "喵喵2" );
        m_petname_list.add( "喵喵3" );
    }

    @Override  
    protected void attachBaseContext(Context base) {  
        Log.d("PetBusApp", "PetBusBusiness:attachBaseContext");
        super.attachBaseContext(base);  
    }
}
