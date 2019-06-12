package com.petbus.tj.petbus.middleware;

import java.util.ArrayList;
import java.util.List;

public interface middleware{
    public static final int MIDDLEWARE_RETURN_OK = 0;

    int get_petnumber();

    ArrayList<String> get_action_list();
    ArrayList<String> get_petname_list();
    int new_record( String time, String petname, String action, String remark, ArrayList<String> record_pic );
}
