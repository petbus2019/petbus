package com.petbus.tj.petbus.dbmanager;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbmanager_impl extends SQLiteOpenHelper implements dbmanager
{

    public static final String DB_NAME = "petbus_db.db";
    public static final String TABLE_NAME_PICTURE = "petbus_picture";
    public static final String TABLE_NAME_USERINFO = "petbus_userinfo";
    public static final int DB_VERSION = 1;

    @Override
    public int get_petnumber()
    {
        Log.i( "PetBusApp", "PetBusDatabase:get_petnumber" );
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "insert into petbus_picture(picture_file_name) values(\"TeJing\")" );
        db.close();
        return 0;
    }

    public dbmanager_impl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        Log.i( "PetBusApp", "PetBusDatabase:create the picture table" );
        String sql = "create table " +
                TABLE_NAME_PICTURE +
                "(id integer primary key autoincrement, " +
                "picture_file_name" + " varchar " + ")";

        db.execSQL(sql);
        db.close();
    }
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i( "PetBusApp", "PetBusDatabase:onUpgrade" );
        String sql = "DROP TABLE " + TABLE_NAME_PICTURE + ";";
        db.execSQL(sql);
        db.close();
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("PetBusApp","open db");
        super.onOpen(db);
    }
}
