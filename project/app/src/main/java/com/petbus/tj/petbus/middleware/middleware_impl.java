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
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

public class middleware_impl extends Application implements middleware {
    private dbmanager m_database = null;
    private static middleware_impl m_instance = null;
    private ArrayList<String> m_action_list = new ArrayList<String>();
    private ArrayList<String> m_petname_list = new ArrayList<String>();
    private int m_current_petid = 1;

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
        String sql = "SELECT " + dbmanager.TABLE_RECORD + ".operation," + dbmanager.TABLE_RECORD + ".time " + 
                     "from " + dbmanager.TABLE_RECORD + " where " + dbmanager.TABLE_RECORD + ".operation " + 
                     "!= 'null' group by " + dbmanager.TABLE_RECORD + ".operation order " + 
                     "by " + dbmanager.TABLE_RECORD + ".time DESC limit 3;";
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
        String sql = "SELECT * FROM " + dbmanager.TABLE_RECORD + ";";
        Cursor sql_result = m_database.get_result( sql );
        return sql_result.getCount();
    }

    public int get_record( int position, StringBuffer time, List<Integer> list, StringBuffer action, StringBuffer remark, ArrayList<String> record_pic ){
        int re = middleware.RECORD_TYPE_RECORD;

        String sql = "SELECT " + dbmanager.TABLE_RECORD + ".picture," + dbmanager.TABLE_RECORD
                   + ".time," + dbmanager.TABLE_RECORD + ".remark,"+ dbmanager.TABLE_RECORD + ".id,"
                   + "" + dbmanager.TABLE_RECORD + ".operation," + dbmanager.TABLE_RECORD
                   + ".type " + "FROM " + dbmanager.TABLE_RECORD + " ORDER BY datetime(time) DESC";
        Cursor sql_result = m_database.get_result( sql );
        sql_result.moveToPosition( position );
        time.append(sql_result.getString(sql_result.getColumnIndex("time")));
        // petname.append(sql_result.getString(sql_result.getColumnIndex("nickname")));
        action.append(sql_result.getString(sql_result.getColumnIndex("operation")));
        remark.append(sql_result.getString(sql_result.getColumnIndex("remark")));
        record_pic.add( sql_result.getString(sql_result.getColumnIndex("picture")) );
        re = sql_result.getInt(sql_result.getColumnIndex("type"));
        if( middleware.RECORD_TYPE_RECORD == re )
        {
            sql = "SELECT " + dbmanager.TABLE_RECORD_PETINFO + ".pet_id" + " FROM "
                + dbmanager.TABLE_RECORD_PETINFO + " WHERE " + dbmanager.TABLE_RECORD_PETINFO
                + ".record_id = " + sql_result.getString(sql_result.getColumnIndex("id")) + ";";
            Cursor query_result = m_database.get_result( sql );
            Log.i( "PetBusApp", "middleware is result" + query_result );
            if (query_result.moveToFirst()) {
                do {
                    int id = query_result.getInt( 0 );
                    list.add( id );
                } while (query_result.moveToNext());
            }
        }
        return re;
    }

    public List<Integer> getPetIds()
    {
        List<Integer> ids = new ArrayList<Integer>();

        String sql = "SELECT id FROM petbus_petinfo;";
        Cursor c = m_database.get_result( sql );
        if (c.moveToFirst()) {
            do {
                ids.add(c.getInt(c.getColumnIndex("id")));
            } while (c.moveToNext());
        }
        return ids;
    }

    public int new_record( String time, List<Integer> pet_list, String action
                         , String remark, ArrayList<String> record_pic ){

        String file_name = "";

        String sql = "SELECT date(time) from " + dbmanager.TABLE_RECORD + 
                     " where date(time) = date(\"" + time + "\")";
        Cursor sql_result = m_database.get_result( sql );
        if( 0 == sql_result.getCount() )
        {
            Log.i( "PetBusApp", "middleware empty" );
            String patten = "yyyy-MM-dd 23:59:59";
            SimpleDateFormat format = new SimpleDateFormat(patten);
            String dateFormatStr = format.format(new Date());
            
            sql = "INSERT INTO " + dbmanager_impl.TABLE_RECORD
                   + "(picture,operation,remark,time,type)"
                   + " values( " + "\"" + "null"
                   + "\",\'" + "null" + "\',\'" + "remark" + "\',\'" + dateFormatStr
                   + "\'," + middleware.RECORD_TYPE_DATE + ");";
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

        Log.i( "PetBusApp", "PetBusBusiness:new_record(" + time + ")-(" + action + ")-(" + remark + ")-(" );
        sql = "INSERT INTO " + dbmanager_impl.TABLE_RECORD + "(picture,operation,remark,time,type)"
                   + " values( "  + "\"" + file_name
                   + "\",\'" + action + "\',\'" + remark + "\',\'" + time + "\'," + middleware.RECORD_TYPE_RECORD + ");";
        m_database.execute_sql( sql );

        sql = "select MAX(id) from " + dbmanager.TABLE_RECORD + ";";
        Cursor cur = m_database.get_result( sql );
        int strid = 0;
        if (cur.moveToFirst()) {
            strid = cur.getInt(cur.getColumnIndex("MAX(id)"));
        }

        for (int i = 0; i < pet_list.size(); i++){
            int index = pet_list.get(i);
            Log.i( "PetBusApp", "PetBusBusiness: index is " + index + " record_id " + strid );
            sql = "INSERT INTO " + dbmanager.TABLE_RECORD_PETINFO + "(pet_id,record_id)"
                   + " values( "  + "\"" + index
                   + "\",\'" + strid + "\'" + ");";
            m_database.execute_sql( sql );
        }
        return middleware.MIDDLEWARE_RETURN_OK;
    }
    public ArrayList<String> get_action_list(){
        return m_action_list;
    }

    public ArrayList<String> get_petname_list(){
        return m_petname_list;
    }

    @Override
    public int get_petnumber()
    {
        String sqlStr = "SELECT * from petbus_petinfo ;";
        Log.i( "PetBusApp", "execSQL: " + sqlStr );
        Cursor sql_result = m_database.get_result( sqlStr );
        return sql_result.getCount();
    }

    public int newPet(String name, String photoPath, String birth, double weight, int gender, int species)
    {
        Log.i( "PetBusApp", "PetBusBusiness:add new pet." );
        //check whether there is the same pet name in database
        String sqlStr = "SELECT nickname from petbus_petinfo WHERE nickname = \"" + name + "\";";
        Cursor sql_result = m_database.get_result( sqlStr );
        if( 0 == sql_result.getCount() )
        {
            Log.i( "PetBusApp", "there's no same pet name." );
            sqlStr = "INSERT INTO " + dbmanager_impl.TABLE_PETNFO + "(user_id,picture,nickname,weight,sex,birthday,pettype)"
                    + " values(" + String.valueOf(m_database.get_userid() ) + ", \"" + photoPath
                    + "\", \"" + name + "\", " + String.valueOf(weight) + "," +
                    String.valueOf(gender) + ", \"" + birth + "\", " + String.valueOf(species) + ");";
            m_database.execute_sql( sqlStr );
        }
        else
        {
            Log.i( "PetBusApp", "Same pet name, cannot insert new one" );
        }

        return middleware.MIDDLEWARE_RETURN_OK;
    }

    public Map<String,Object> get_current_pet() {
        Map<String, Object> result = getPetInfo( m_current_petid );
        return result;
    }

    public Map<String, Object> getPetInfo(int id)
    {
        Log.i( "PetBusApp", "PetBusBusiness:get pet information,id:" + id );
        Map<String, Object> petinfo = new HashMap<String, Object>();
        String sqlStr = "SELECT nickname,picture,weight,sex,birthday,pettype" +
                " from petbus_petinfo WHERE id = " + id + ";";
        Cursor cur = m_database.get_result( sqlStr );
        if( 0 == cur.getCount() )
        {
            Log.i( "PetBusApp", "there's no pet which's id=" + id );
        }
        else
        {
            if (cur.moveToFirst()) {
                do {
                    String getName = cur.getString(cur.getColumnIndex( dbmanager.COLUMN_TEXT_NICKNAME ));
                    petinfo.put( middleware.PETINFO_TYPE_NAME, getName);
                    String picture = cur.getString(cur.getColumnIndex( dbmanager.COLUMN_TEXT_PICTURE ));
                    petinfo.put( middleware.PETINFO_TYPE_PHOTO, picture);
                    double getWeight = cur.getDouble(cur.getColumnIndex( dbmanager.COLUMN_TEXT_WEIGHT ));
                    petinfo.put(middleware.PETINFO_TYPE_WEIGHT, getWeight);
                    String getBirth = cur.getString(cur.getColumnIndex( dbmanager.COLUMN_TEXT_BIRTHDAY ));
                    petinfo.put( middleware.PETINFO_TYPE_AGE, getBirth);
                } while (cur.moveToNext());
            }
        }
        return petinfo;
    }

    public boolean setCurrentPet(int id)
    {
        m_current_petid = id;
        return true;
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
            } while (cur.moveToNext());
        }
        Log.d("PetBusApp", "PetBusBusiness:the petnamelist is " + m_petname_list );

        cur = m_database.get_result(m_get_operationname_sql);
        if (cur.moveToFirst()) {
            do {
                String nick_name = cur.getString(cur.getColumnIndex("action_name"));
                m_action_list.add( nick_name );
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
