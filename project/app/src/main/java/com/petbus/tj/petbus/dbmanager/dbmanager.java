package com.petbus.tj.petbus.dbmanager;

import android.database.Cursor;

public interface dbmanager {
    int get_petnumber();
    int execute_sql( String sql );
    Cursor get_result( String sql );
}
