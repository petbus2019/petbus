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

public class petbus_welcome extends FragmentActivity implements OnClickListener {
    private middleware m_middleware;

    private Fragment m_welcomeframgent_welcome;
    private Fragment m_welcomeframgent_petadd;

    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {      
                case 1:      
                    Intent intent = new Intent();
                    intent.setClass(petbus_welcome.this, petbus_firstvisit.class);
                    startActivity(intent);
                    break;
                }
                super.handleMessage(msg);
            }
    };

    Timer m_timer = new Timer();
    TimerTask m_timertask = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int re = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_welcome);
        m_middleware = middleware_impl.getInstance();
        // Example of a call to a native method

        if( 0 == m_middleware.get_petnumber() )
        {
            //chang to addpet page
            re = 1;
        }
        else
        {
            //jump to petbus_acton...activty after 3 second
            re = 0;
            m_timer.schedule(m_timertask,3000);
        }

        active_fragment( re );
    }

    public void onClick(View view) {
        Log.i( "PetBusApp", "PetBus:onClick" );
        switch( view.getId() )
        {
            case R.id.btn_profileAdd:
                // try{
                    //https://www.jianshu.com/p/19147a69e970
                    Intent intent = new Intent();
                    intent.setClass(petbus_welcome.this, petbus_profile.class);
                    startActivity(intent);
                // }
                // catch(ActivityNotFoundException e){
                //     Log.d("PetBusApp","找不到该活动");
                // }
                break;
        }
    }


    private void active_fragment(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );

        switch(i) {
            case 0:
                if (m_welcomeframgent_welcome == null) {
                    m_welcomeframgent_welcome = new welcomefragment_welcome();
                    transaction.add(R.id.welcome_fragment, m_welcomeframgent_welcome);
                } else {
                    transaction.show(m_welcomeframgent_welcome);
                }
                break;
            case 1:
                if ( m_welcomeframgent_petadd == null ) {
                    m_welcomeframgent_petadd = new welcomefragment_petadd();
                    transaction.add(R.id.welcome_fragment, m_welcomeframgent_petadd);
                }
                else
                {
                    transaction.show( m_welcomeframgent_petadd );
                }
                break;
        }
        transaction.commit();
    }

    private void hide_fragment(FragmentTransaction transaction) {
        if (m_welcomeframgent_welcome != null) {
            transaction.hide(m_welcomeframgent_welcome);
        }
        if (m_welcomeframgent_petadd != null) {
            transaction.hide(m_welcomeframgent_petadd);
        }
    }

}
