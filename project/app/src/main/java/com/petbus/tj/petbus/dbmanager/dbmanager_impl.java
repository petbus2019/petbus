package com.petbus.tj.petbus.dbmanager;

import android.util.Log;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;

public class dbmanager_impl extends SQLiteOpenHelper implements dbmanager
{


    public static final int DB_VERSION = 1;

    public long get_userid()
    {
        long user_id = 0;
        String sql = "select id from " + TABLE_USERINFO + ";";
        Log.i( "PetBusApp", "execSQL: " + sql );
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery( sql , null );
        if (c.moveToFirst()) {
            do {
                user_id = c.getLong(c.getColumnIndex("id"));
            } while (c.moveToNext());
        }
        return user_id;
    }

    public dbmanager_impl(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        Log.i( "PetBusApp", "PetBusDatabase:create the picture table" );
        String sql = "create table " +
                dbmanager.TABLE_NAME_PICTURE +
                "(" + dbmanager.COLUMN_TEXT_ID + " integer primary key autoincrement, " +
                "picture_file_name" + " varchar " + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);

        Log.i( "PetBusApp", "PetBusDatabase:create the " + dbmanager.TABLE_USERINFO + " table" );
        sql = "create table " +
                dbmanager.TABLE_USERINFO +
                "(" + dbmanager.COLUMN_TEXT_ID +" integer PRIMARY KEY, " +
                dbmanager.COLUMN_TEXT_NICKNAME + " TEXT " + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);

        Log.i( "PetBusApp", "PetBusDatabase:create the " + dbmanager.TABLE_PETNFO + " table" );
        sql = "create table " +
                dbmanager.TABLE_PETNFO +
                "(" + dbmanager.COLUMN_TEXT_ID +" integer PRIMARY KEY autoincrement, " +
                dbmanager.COLUMN_TEXT_USERID + " integer," + 
                "picture" + " TEXT," + 
                dbmanager.COLUMN_TEXT_NICKNAME + " TEXT," +
                dbmanager.COLUMN_TEXT_WEIGHT + " Double," +
                "sex" + " integer," + 
                dbmanager.COLUMN_TEXT_BIRTHDAY + " DATE," + 
                "pettype" + " integer," + 
                "FOREIGN KEY ( " + dbmanager.COLUMN_TEXT_USERID + 
                " ) REFERENCES " + dbmanager.TABLE_USERINFO + "(" + dbmanager.COLUMN_TEXT_ID +"));";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);

        Log.i( "PetBusApp", "PetBusDatabase:create the " + dbmanager.TABLE_RECORD + " table" );
        sql = "create table " +
                dbmanager.TABLE_RECORD +
                "(" + dbmanager.COLUMN_TEXT_ID +" integer PRIMARY KEY autoincrement, " +
                "picture" + " TEXT," + 
                "operation" + " TEXT," + 
                "time" + " DATE," +
                "remark" + " VARCHAR(256)," +
                "type" + " integer )";
                // "FOREIGN KEY ( pet_id ) REFERENCES " + dbmanager.TABLE_PETNFO + "(" + dbmanager.COLUMN_TEXT_ID +"));";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);

        Log.i( "PetBusApp", "PetBusDatabase:create the " + dbmanager.TABLE_RECORD_PETINFO + " table" );
        sql = "create table " +
                dbmanager.TABLE_RECORD_PETINFO +
                "(" + dbmanager.COLUMN_TEXT_ID +" integer PRIMARY KEY autoincrement, " +
                "pet_id" + " integer," + 
                "record_id" + " integer," + 
                "FOREIGN KEY ( pet_id ) REFERENCES " + dbmanager.TABLE_PETNFO + "(" + dbmanager.COLUMN_TEXT_ID +")," +
                "FOREIGN KEY ( record_id ) REFERENCES " + dbmanager.TABLE_RECORD + "(" + dbmanager.COLUMN_TEXT_ID +"));";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);


        Log.i( "PetBusApp", "PetBusDatabase:create the " + dbmanager.TABLE_OPERATIONNAME + " table" );
        sql = "create table " +
                dbmanager.TABLE_OPERATIONNAME +
                "(" + dbmanager.COLUMN_TEXT_ID +" integer PRIMARY KEY autoincrement, " +
                "picture" + " TEXT," + 
                "action_name" + " TEXT" + 
                ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);


        String time_id = String.valueOf( System.currentTimeMillis() );
        sql = "INSERT INTO " + dbmanager.TABLE_USERINFO + " values(" + time_id + ",\'TeJing\'" + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL(sql);

        add_test_data( db );
    }
    @Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i( "PetBusApp", "PetBusDatabase:onUpgrade" );
        String sql = "DROP TABLE " + dbmanager.TABLE_NAME_PICTURE + ";";
        db.execSQL(sql);
        db.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.i("PetBusApp","open db");
        super.onOpen(db);
    }

    public int execute_sql( String sql ){
        SQLiteDatabase db = getWritableDatabase();
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );
        db.close();
        return 0;
    }

    public Cursor get_result( String sql ){
        SQLiteDatabase db = getWritableDatabase();
        Log.i( "PetBusApp", "get_result: " + sql );
        Cursor c = db.rawQuery( sql , null );
        return c;
    }

    private void add_test_data(SQLiteDatabase db){
        //below is test only
        
        //Step1: get the user id
        long user_id = 0;
        String sql = "select id from " + TABLE_USERINFO + ";";
        Log.i( "PetBusApp", "execSQL: " + sql );
        Cursor c = db.rawQuery( sql , null );
        if (c.moveToFirst()) {
            do {
                user_id = c.getLong(c.getColumnIndex("id"));
                // String name = c.getString(c.getColumnIndex("name"));
                // String age = c.getString(c.getColumnIndex("age"));
            } while (c.moveToNext());
        }

        //Step2: add four pet to databse
        sql = "insert into " + TABLE_PETNFO + " (user_id,picture,nickname,weight, sex,birthday,pettype)"
                   + " values(" + String.valueOf(user_id) + ",\'/data/user/0/com.petbus.tj.petbus/cache/pet_1.jpg\'" 
                   + ",\'喵喵\'," + "23"+",1" + ",2019-06-01" + ",1"  + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_PETNFO + " (user_id,picture,nickname,weight,sex,birthday,pettype)"
            + " values(" + String.valueOf(user_id) + ",\'/data/user/0/com.petbus.tj.petbus/cache/pet_2.jpg\'" 
            + ",\'喵喵1\'," + "5"+",2" + ",2019-06-01" + ",1"  + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_PETNFO + " (user_id,picture,nickname,weight,sex,birthday,pettype)"
            + " values(" + String.valueOf(user_id) + ",\'/data/user/0/com.petbus.tj.petbus/cache/pet_3.jpg\'" 
            + ",\'喵喵2\'," + "13" + ",2" + ",2019-06-01" + ",1"  + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_PETNFO + " (user_id,picture,nickname,weight, sex,birthday,pettype)"
            + " values(" + String.valueOf(user_id) + ",\'/data/user/0/com.petbus.tj.petbus/cache/pet_4.jpg\'" 
            + ",\'喵喵3\'," + "6" + ",1" + ",2019-06-01" + ",1"  + ");";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        //Step3: add base operation to databasea
        sql = "insert into " + TABLE_OPERATIONNAME + " (picture,action_name)" 
            + " values(\'test.jpg\'" + ",\'喂食\');";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_OPERATIONNAME + " (picture,action_name)" 
            + " values(\'test.jpg\'" + ",\'铲屎\');";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_OPERATIONNAME + " (picture,action_name)" 
            + " values(\'test.jpg\'" + ",\'洗澡\');";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        sql = "insert into " + TABLE_OPERATIONNAME + " (picture,action_name)" 
            + " values(\'test.jpg\'" + ",\'遛弯\');";
        Log.i( "PetBusApp", "execSQL: " + sql );
        db.execSQL( sql );

        return;
    }
}

// SELECT date(time) from petbus_actionrecord   GROUP BY date(time);
// SELECT count(*) from mytable  GROUP BY strftime('%Y',  datatime);
// select date(time) from petbus_actionrecord;
// select time(time) from petbus_actionrecord;
// SELECT * FROM petbus_actionrecord ORDER BY datetime(time) DESC

// SELECT petbus_actionrecord.picture,petbus_actionrecord.time,petbus_actionrecord.remark,
// petbus_actionrecord.operation,petbus_actionrecord.type,petbus_petinfo.nickname 
// FROM petbus_actionrecord left join petbus_petinfo on 
// petbus_actionrecord.pet_id = petbus_petinfo.id ORDER BY datetime(time) DESC

// SELECT petbus_actionrecord.operation,petbus_actionrecord.time from petbus_actionrecord 
// where petbus_actionrecord.operation != 'null' group by petbus_actionrecord.operation order 
// by petbus_actionrecord.time DESC ;