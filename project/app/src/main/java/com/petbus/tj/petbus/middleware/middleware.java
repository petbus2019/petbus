package com.petbus.tj.petbus.middleware;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public interface middleware{
    public static final int MIDDLEWARE_RETURN_OK = 0;
    public static final int RECORD_TYPE_DATE = 0;
    public static final int RECORD_TYPE_RECORD = 1;

    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_DATE = "yyyy-MM-dd";
    public static final String DATE_FORMAT_TIME = "HH:mm:ss";

    public static final String PETINFO_TYPE_NAME = "name";
    public static final String PETINFO_TYPE_PHOTO = "photo";
    public static final String PETINFO_TYPE_WEIGHT = "weight";
    public static final String PETINFO_TYPE_BIRTH = "birth";
    public static final String PETINFO_TYPE_AGE = "age";
    public static final String PETINFO_TYPE_SPECIES = "species";
    public static final String PETINFO_TYPE_GENDER = "gender";
    public static final String PETINFO_TYPE_ID = "id";

    public static final int OVERVIEW_DATATYPE_YEAR = 0;
    public static final int OVERVIEW_DATATYPE_MONTH = 1;
    public static final int OVERVIEW_DATATYPE_DAY = 2;


    int get_petnumber();

    ArrayList<String> get_action_list();
    ArrayList<String> get_petname_list();
    boolean should_loadrecord( int shoudl_load );
    boolean is_loadover();
    int loadrecord( int count );
    int get_record_count();
    int get_last_three_record( Map<String,String> record_map );
    int new_record( String time, List<Integer> list, String action, String remark, ArrayList<String> record_pic );
    int get_record( int id, StringBuffer time, List<Integer> list, StringBuffer action, StringBuffer remark, ArrayList<String> record_pic );
    int newPet(final String name, final String photoPath, final String birth,
               final double weight, final int gender, final int species);
    int editPet(final int id, final String name, final String photoPath, final String birth,
                final double weight, final int gender, final int species);
    int delPet(int id);
    List<Integer> getPetIds();
    Map<String,Object> get_current_pet();
    int set_current_pet( int id );
    Map<String,Object> getPetInfo(int id);
    Map<String,Integer> get_statistics( String condition,String operation );
    int get_overview_value( String year, String month, Map<String,Integer> value );
}
