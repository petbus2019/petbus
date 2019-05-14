package com.petbus.tj.petbus.ui;
//https://blog.csdn.net/chaoshenzhaoxichao/article/details/79871145
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ImageButton;
import android.widget.TextView;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

public class petbus_action extends FragmentActivity implements OnClickListener {
    private Fragment m_action_fragment_recode;
    private Fragment m_action_fragment_overview;
    private Fragment m_action_fragment_diary;
    private Fragment m_action_fragment_alarm;


    private ImageButton m_button_actionrecode;
    private ImageButton m_button_actionoverview;
    private ImageButton m_button_actiondiary;
    private ImageButton m_button_actionalarm;

    private middleware m_middleware;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_action);

        m_middleware = middleware_impl.getInstance();

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setOnClickListener(this);

        init_button();
        active_fragment( 0 );
    }

    public void onClick(View view) {
        Log.i( "PetBusApp", "PetBus:onClick" );
        switch( view.getId() )
        {
            case R.id.sample_text:
                Intent intent = new Intent();
                intent.setClass(petbus_action.this, petbus_welcome.class);
                startActivity(intent);
                break;
            case R.id.action_recode:
                Log.i( "PetBusApp", "PetBus:action_recode" );
                active_fragment(0);
                break;
            case R.id.action_overview:
                active_fragment(1);
                Log.i( "PetBusApp", "PetBus:action_overview" );
                break;
            case R.id.action_diary:
                active_fragment(2);
                Log.i( "PetBusApp", "PetBus:action_overview" );
                break;
            case R.id.action_alarm:
                active_fragment(3);
                Log.i( "PetBusApp", "PetBus:action_overview" );
                break;
        }

        m_middleware.get_petnumber();
    }

    private void init_button()
    {
        m_button_actionrecode = (ImageButton) findViewById(R.id.action_recode);
        m_button_actionoverview = (ImageButton) findViewById(R.id.action_overview);
        m_button_actiondiary = (ImageButton) findViewById(R.id.action_diary);
        m_button_actionalarm = (ImageButton) findViewById(R.id.action_alarm);

        m_button_actionrecode.setOnClickListener(this);
        m_button_actionoverview.setOnClickListener(this);
        m_button_actiondiary.setOnClickListener(this);
        m_button_actionalarm.setOnClickListener(this);
    }

    private void active_fragment(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );
        switch (i) {
            case 0:
                if (m_action_fragment_recode == null) {
                    m_action_fragment_recode = new actionfragment_actionrecode();
                    //m_action_fragment_recode.setOnClickListener(this);
                    transaction.add(R.id.fragment, m_action_fragment_recode);
                } else {
                    transaction.show(m_action_fragment_recode);
                }
                break;
            case 1:
                if ( m_action_fragment_overview == null )
                {
                    m_action_fragment_overview = new actionfragment_overview();
                    // m_action_fragment_overview.setOnClickListener(this);
                    transaction.add(R.id.fragment, m_action_fragment_overview);
                }
                else
                {
                    transaction.show( m_action_fragment_overview );
                }
                break;
            case 2:
                if ( m_action_fragment_diary == null )
                {
                    m_action_fragment_diary = new actionfragment_diary();
                    transaction.add(R.id.fragment, m_action_fragment_diary);
                }
                else
                {
                    transaction.show( m_action_fragment_diary );
                }
                break;
            case 3:
                if ( m_action_fragment_alarm == null )
                {
                    m_action_fragment_alarm = new actionfragment_alarm();
                    transaction.add(R.id.fragment, m_action_fragment_alarm);
                }
                else
                {
                    transaction.show( m_action_fragment_alarm );
                }
                break;

        }
        transaction.commit();
    }

    private void reset_actionbutton( int id )
    {
        switch (id) {
            case 0:
                m_button_actionrecode.setImageResource( R.mipmap.recode_pressed );
                break;
            case 1:
                m_button_actionoverview.setImageResource( R.mipmap.overview_pressed );
                break;
            case 2:
                m_button_actiondiary.setImageResource( R.mipmap.diary_pressed );
                break;
            case 3:
                m_button_actionalarm.setImageResource( R.mipmap.alarm_presssed );
                break;
        }
        m_button_actionrecode.setImageResource( R.mipmap.recode_normal );
        m_button_actionoverview.setImageResource( R.mipmap.overview_normal );
        m_button_actiondiary.setImageResource( R.mipmap.diary_normal );
        m_button_actionalarm.setImageResource( R.mipmap.alarm_normal );
    }

    private void hide_fragment(FragmentTransaction transaction) {
        if (m_action_fragment_recode != null) {
            transaction.hide(m_action_fragment_recode);
        }
        if (m_action_fragment_overview != null) {
            transaction.hide(m_action_fragment_overview);
        }
        if (m_action_fragment_diary != null) {
            transaction.hide(m_action_fragment_diary);
        }
        if (m_action_fragment_alarm != null) {
            transaction.hide(m_action_fragment_alarm);
        }

    }

}
