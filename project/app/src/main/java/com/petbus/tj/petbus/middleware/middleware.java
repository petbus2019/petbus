package com.petbus.tj.petbus.middleware;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public interface middleware{
    public static final int MIDDLEWARE_RETURN_OK = 0;
    public static final int RECORD_TYPE_DATE = 0;
    public static final int RECORD_TYPE_RECORD = 1;

    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_TIME = "hh:mm:ss";

    int get_petnumber();

    ArrayList<String> get_action_list();
    ArrayList<String> get_petname_list();

    int get_record_count();
    int get_last_three_record( Map<String,String> record_map );
    int new_record( String time, String petname, String action, String remark, ArrayList<String> record_pic );
    int get_record( int id, StringBuffer time, StringBuffer petname, StringBuffer action, StringBuffer remark, ArrayList<String> record_pic );
}
