package com.petbus.tj.petbus.middleware;

import com.petbus.tj.petbus.dbmanager.dbmanager;
import com.petbus.tj.petbus.dbmanager.dbmanager_impl;

import android.database.Cursor;
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

    private static final String m_get_petid_by_name = "select id from petbus_petinfo where nickname = ";
    public int new_record( String time, String petname, String action, String remark, ArrayList<String> record_pic ){
        String patten = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(patten);
        String dateFormatStr = format.format(new Date());
        String sql = m_get_petid_by_name + "\'" + petname + "\'" + ";";
        String file_name = "";
        Log.i( "PetBusApp", "execSQL: " + sql );
        int pet_id = 1;
        Cursor c = m_database.get_result( sql );
        if (c.moveToFirst()) {
            do {
                pet_id = c.getInt(c.getColumnIndex("id"));
            } while (c.moveToNext());
        }

        if( record_pic.size() > 0 )
        {
            file_name = record_pic.get(0);
            Log.i( "PetBusApp", "PetBusBusiness:picture:" + record_pic );
            Log.i( "PetBusApp", "PetBusBusiness:picture:" + file_name );
        }

        Log.i( "PetBusApp", "PetBusBusiness:new_record(" + time + ")-(" + petname + ")-(" + action + ")-(" + remark + ")-(" );
        sql = "INSERT INTO " + dbmanager_impl.TABLE_RECORD + "(pet_id,picture,operation,remark,time)"
                   + " values( " + String.valueOf(pet_id) + "," + "\"" + file_name
                   + "\",\'" + action + "\',\'" + remark + "\',\'" + dateFormatStr + "\');";
        m_database.execute_sql( sql );
        return middleware.MIDDLEWARE_RETURN_OK;
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

    private static final String m_get_nickname_sql = "select nickname from petbus_petinfo;";
    private static final String m_get_operationname_sql = "select action_name from petbus_operationname;";
    @Override
    public void onCreate() {
        super.onCreate();
        String packageName = getPackageName();
        Log.d("PetBusApp", "PetBusBusiness:package name is " + packageName);
        m_database = new dbmanager_impl( getApplicationContext() ) ;
        Log.d("PetBusApp", "PetBusBusiness:the database is " + m_database );

        Cursor cur = m_database.get_result(m_get_nickname_sql);
        if (cur.moveToFirst()) {
            do {
                String nick_name = cur.getString(cur.getColumnIndex("nickname"));
                m_petname_list.add( nick_name );
                // String name = cur.getString(cur.getColumnIndex("name"));
                // String age = cur.getString(cur.getColumnIndex("age"));
            } while (cur.moveToNext());
        }
        Log.d("PetBusApp", "PetBusBusiness:the petnamelist is " + m_petname_list );

        cur = m_database.get_result(m_get_operationname_sql);
        if (cur.moveToFirst()) {
            do {
                String nick_name = cur.getString(cur.getColumnIndex("action_name"));
                m_action_list.add( nick_name );
                // String name = cur.getString(cur.getColumnIndex("name"));
                // String age = cur.getString(cur.getColumnIndex("age"));
            } while (cur.moveToNext());
        }
        Log.d("PetBusApp", "PetBusBusiness:the m_action_list is " + m_action_list );

    }

    @Override  
    protected void attachBaseContext(Context base) {  
        Log.d("PetBusApp", "PetBusBusiness:attachBaseContext");
        super.attachBaseContext(base);  
    }
}
