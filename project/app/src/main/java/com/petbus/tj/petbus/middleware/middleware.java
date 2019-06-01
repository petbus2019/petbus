package com.petbus.tj.petbus.middleware;

import java.util.ArrayList;
import java.util.List;

public interface middleware{
    int get_petnumber();


    ArrayList<String> get_action_list();
    ArrayList<String> get_petname_list();
    int new_recode( String time, String petname, String action, String remark, ArrayList<String> m_recode_pic );
}
