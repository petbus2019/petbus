package com.petbus.tj.petbus.middleware;

import java.util.ArrayList;
import java.util.List;

public class middleware_diaryrecode{
    int m_recode_id;
    String m_petname;
    String m_time;
    String m_operation;
    String m_remark;
    ArrayList<String> m_recode_pic;

    public static final int MIDDLEWARE_RETURN_OK = 0;

    public middleware_diaryrecode(){
        m_recode_pic = new ArrayList<String>();
    }
}
