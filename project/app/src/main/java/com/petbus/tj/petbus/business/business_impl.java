package com.petbus.tj.petbus.business;

import com.petbus.tj.petbus.database.database;
import com.petbus.tj.petbus.database.database_impl;

import android.content.Context;
import android.app.Application;
import android.util.Log;

public class business_impl extends Application implements business {
    private database m_database = null;
    private static business_impl m_instance = null;//new business_impl();
    public business_impl(){
        Log.i( "PetBusApp", "PetBusBusiness:business_impl" );
        if( null == m_instance )
        {
            m_instance = this;
        }
    }

    public static business_impl getInstance(){
        return m_instance;
    }
    @Override
    public int get_petnumber()
    {
        Log.i( "PetBusApp", "PetBusBusiness:get_petnumber" );
        m_database.get_value();
        return 10;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String packageName = getPackageName();
        Log.d("PetBusApp", "PetBusBusiness:package name is " + packageName);
        m_database = new database_impl( getApplicationContext() ) ;
        Log.d("PetBusApp", "PetBusBusiness:the database is " + m_database );
    }

    @Override  
    protected void attachBaseContext(Context base) {  
        Log.d("PetBusApp", "PetBusBusiness:attachBaseContext");
        super.attachBaseContext(base);  
    }
}
