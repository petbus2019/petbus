package com.petbus.tj.petbus.ui;


public interface ui_interface {

    public static final int MAINFRAMGENT_ID = 0;
    public static final int OVERVIEWFRAMGENT_ID = 1;
    public static final int SETTINGFRAMGENT_ID = 2;
    public static final int PETMANAGEFRAMGENT_ID = 3;

    void trigger_change( int fragment );

    void trigger_camera();
}