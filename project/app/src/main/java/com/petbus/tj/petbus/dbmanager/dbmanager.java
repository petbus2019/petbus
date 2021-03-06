package com.petbus.tj.petbus.dbmanager;

import android.database.Cursor;

public interface dbmanager {
	public static final String DB_NAME = "petbus_db.db";
    public static final String TABLE_NAME_PICTURE = "petbus_picture";
    public static final String TABLE_USERINFO = "petbus_userinfo";
    public static final String TABLE_PETNFO = "petbus_petinfo";
    public static final String TABLE_RECORD = "petbus_record";
    public static final String TABLE_RECORD_PETINFO = "petbus_record_petinfo";
    public static final String TABLE_OPERATIONNAME = "petbus_operationname";


    public static final String COLUMN_TEXT_ID = "id";
    public static final String COLUMN_TEXT_NICKNAME = "nickname";
    public static final String COLUMN_TEXT_USERID = "user_id";
    public static final String COLUMN_TEXT_PICTURE = "picture";
    public static final String COLUMN_TEXT_WEIGHT = "weight";
    public static final String COLUMN_TEXT_BIRTHDAY = "birthday";
    public static final String COLUMN_TEXT_SEX = "sex";
    public static final String COLUMN_TEXT_PETTYPE = "pettype";
    
    long get_userid();
    int execute_sql( String sql );
    Cursor get_result( String sql );
}
