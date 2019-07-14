package com.petbus.tj.petbus.ui;

//https://blog.csdn.net/chaoshenzhaoxichao/article/details/79871145

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.Timer;
import java.util.TimerTask;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

public class petbus_welcome extends FragmentActivity {
    private middleware m_middleware;

    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {

            Log.i( "PetBusApp", "handleMessage:" + msg.what );
            switch ( msg.what ) {
                case 0:
                    Intent intent_0 = new Intent();
                    intent_0.setClass(petbus_welcome.this, petbus_firstvisit.class);
                    startActivity(intent_0);
                    break;
                case 1:      
                    Intent intent_1 = new Intent();
                    intent_1.setClass(petbus_welcome.this, petbus_action.class);
                    startActivity(intent_1);
                    break;
                }
                super.handleMessage(msg);
            }
    };

    Timer m_timer = new Timer();
    TimerTask m_timertask = new TimerTask() {
        @Override
        public void run() {
            int re = 0;
            if( 0 == m_middleware.get_petnumber() )
            {
                re = 0;
            }
            else
            {
                re = 1;
                
            }
            Message message = new Message();
            message.what = re;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_welcome);
        m_middleware = middleware_impl.getInstance();
        // m_timer.schedule(m_timertask,3000);
    }

    @Override
    public void onResume(){
        super.onResume();
        m_timer.schedule(m_timertask,3000);
    }

}
