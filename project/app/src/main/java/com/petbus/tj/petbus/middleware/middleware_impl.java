package com.petbus.tj.petbus.middleware;

import com.petbus.tj.petbus.dbmanager.dbmanager;
import com.petbus.tj.petbus.dbmanager.dbmanager_impl;

import android.database.Cursor;
import android.content.Context;
import android.app.Application;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class middleware_impl extends Application implements middleware {
    private dbmanager m_database = null;
    private static middleware_impl m_instance = null;
    private ArrayList<String> m_action_list = new ArrayList<String>();
    private ArrayList<String> m_petname_list = new ArrayList<String>();
    public middleware_impl(){
        Log.i( "PetBusApp", "PetBusBusiness:middleware_impl" );
        if( null == m_instance ){
            m_instance = this;
        }
    }

    public static middleware_impl getInstance(){
        return m_instance;
    }

    public int get_last_three_record( Map<String,String> record_map ){
        String sql = "SELECT petbus_actionrecord.operation,petbus_actionrecord.time " + 
                     "from petbus_actionrecord where petbus_actionrecord.operation " + 
                     "!= 'null' group by petbus_actionrecord.operation order " + 
                     "by petbus_actionrecord.time DESC limit 3;";
        Cursor sql_result = m_database.get_result( sql );
        if (sql_result.moveToFirst()) {
            do {
                String operation = sql_result.getString(sql_result.getColumnIndex("operation"));
                String time = sql_result.getString(sql_result.getColumnIndex("time"));
                record_map.put( operation, time );
            } while (sql_result.moveToNext());
        }
        return 0;
    }
    public int get_record_count(){
        String sql = "SELECT * FROM petbus_actionrecord;";
        Cursor sql_result = m_database.get_result( sql );
        return sql_result.getCount();
    }

    public int get_record( int position, StringBuffer time, StringBuffer petname, StringBuffer action, StringBuffer remark, ArrayList<String> record_pic ){
        int re = middleware.RECORD_TYPE_RECORD;

        String sql = "SELECT petbus_actionrecord.picture,petbus_actionrecord.time,petbus_actionrecord.remark," +
                     "petbus_actionrecord.operation,petbus_actionrecord.type,petbus_petinfo.nickname " +
                     "FROM petbus_actionrecord left join petbus_petinfo on " +
                     "petbus_actionrecord.pet_id = petbus_petinfo.id ORDER BY datetime(time) DESC";
        Cursor sql_result = m_database.get_result( sql );
        sql_result.moveToPosition( position );
        time.append(sql_result.getString(sql_result.getColumnIndex("time")));
        petname.append(sql_result.getString(sql_result.getColumnIndex("nickname")));
        action.append(sql_result.getString(sql_result.getColumnIndex("operation")));
        remark.append(sql_result.getString(sql_result.getColumnIndex("remark")));
        record_pic.add( sql_result.getString(sql_result.getColumnIndex("picture")) );
        re = sql_result.getInt(sql_result.getColumnIndex("type"));

        return re;
    }

    private static final String m_get_petid_by_name = "select id from petbus_petinfo where nickname = ";
    public int new_record( String time, String petname, String action, String remark, ArrayList<String> record_pic ){

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

        sql = "SELECT date(time) from petbus_actionrecord where date(time) = date(\"" + time + "\")";
        Cursor sql_result = m_database.get_result( sql );
        if( 0 == sql_result.getCount() )
        {
            Log.i( "PetBusApp", "middleware empty" );
            String patten = "yyyy-MM-dd 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat(patten);
            String dateFormatStr = format.format(new Date());
            
            sql = "INSERT INTO " + dbmanager_impl.TABLE_RECORD + "(pet_id,picture,operation,remark,time,type)"
                   + " values( " + String.valueOf(pet_id) + "," + "\"" + "null"
                   + "\",\'" + "null" + "\',\'" + "remark" + "\',\'" + dateFormatStr + "\'," + middleware.RECORD_TYPE_DATE + ");";
            m_database.execute_sql( sql );
        }
        else
        {
            Log.i( "PetBusApp", "middleware is notTTTTTTTTTT empty" );
        }

        if( record_pic.size() > 0 )
        {
            file_name = record_pic.get(0);
            Log.i( "PetBusApp", "PetBusBusiness:picture:" + record_pic );
            Log.i( "PetBusApp", "PetBusBusiness:picture:" + file_name );
        }

        Log.i( "PetBusApp", "PetBusBusiness:new_record(" + time + ")-(" + petname + ")-(" + action + ")-(" + remark + ")-(" );
        sql = "INSERT INTO " + dbmanager_impl.TABLE_RECORD + "(pet_id,picture,operation,remark,time,type)"
                   + " values( " + String.valueOf(pet_id) + "," + "\"" + file_name
                   + "\",\'" + action + "\',\'" + remark + "\',\'" + time + "\'," + middleware.RECORD_TYPE_RECORD + ");";
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
