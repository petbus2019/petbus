package com.petbus.tj.petbus.database;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database_impl extends SQLiteOpenHelper implements database
{

    public static final String DB_NAME = "petbus_db.db";
    public static final String TABLE_NAME_PICTURE = "petbus_picture";
    public static final String TABLE_NAME_USERINFO = "petbus_userinfo";
    public static final int DB_VERSION = 1;

    @Override
    public int get_value()
    {
        Log.i( "PetBusApp", "PetBusDatabase:get_value" );
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL( "insert into petbus_picture(picture_file_name) values(\"TeJing\")" );
        db.close();
        return 10;
    }

    public database_impl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table " +
                TABLE_NAME_PICTURE +
                "(id integer primary key autoincrement, " +
                "picture_file_name" + " varchar " + ")";

        db.execSQL(sql);
        Log.i( "PetBusApp", "PetBusDatabase:create the table" );
    }
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i( "PetBusApp", "PetBusDatabase:onUpgrade" );
        String sql = "DROP TABLE " + TABLE_NAME_PICTURE + ";";
        db.execSQL(sql);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("PetBusApp","open db");
        super.onOpen(db);
    }
}
