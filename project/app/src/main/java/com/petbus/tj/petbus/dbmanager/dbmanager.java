package com.petbus.tj.petbus.dbmanager;

import android.database.Cursor;

public interface dbmanager {
	public static final String DB_NAME = "petbus_db.db";
    public static final String TABLE_NAME_PICTURE = "petbus_picture";
    public static final String TABLE_USERINFO = "petbus_userinfo";
    public static final String TABLE_PETNFO = "petbus_petinfo";
    public static final String TABLE_RECORD = "petbus_actionrecord";
    public static final String TABLE_OPERATIONNAME = "petbus_operationname";
    
    long get_userid();
    int execute_sql( String sql );
    Cursor get_result( String sql );
}
