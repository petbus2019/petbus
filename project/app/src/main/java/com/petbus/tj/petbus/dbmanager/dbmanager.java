package com.petbus.tj.petbus.dbmanager;

import android.database.Cursor;

public interface dbmanager {
    int get_petnumber();
    long get_userid();
    int execute_sql( String sql );
    Cursor get_result( String sql );
}
