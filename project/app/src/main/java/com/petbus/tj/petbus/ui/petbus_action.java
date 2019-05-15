package com.petbus.tj.petbus.ui;

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
import android.view.LayoutInflater;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

public class petbus_action extends FragmentActivity implements OnClickListener {
    private Fragment m_action_fragment_recode;
    private Fragment m_action_fragment_overview;
    private Fragment m_action_fragment_diary;
    private Fragment m_action_fragment_alarm;

    private ImageButton m_button_actionbutton;
    private ImageButton m_button_overviewbutton;
    private ImageButton m_button_settingbutton;
    private ImageButton m_button_addbutton;
    private TextView m_title_view;

    private middleware m_middleware;


    private static final int MAINFRAMGENT_ID = 0;
    private static final int OVERVIEWFRAMGENT_ID = 1;
    private static final int SETTINGFRAMGENT_ID = 2;
    private static final int PETMANAGEFRAMGENT_ID = 3;
    private int m_fragment_id = MAINFRAMGENT_ID;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_action);

        m_middleware = middleware_impl.getInstance();

        init_view();
        active_fragment( 0 );
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "PetBus:onClick" );
        switch( view.getId() )
        {
            case R.id.leftreturn_button:
                Log.i( "PetBusApp", "PetBus:action_recode" );
                active_fragment(MAINFRAMGENT_ID);
                break;
            case R.id.overview_button:
                active_fragment(OVERVIEWFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:overview_button" );
                break;
            case R.id.setting_button:
                active_fragment(SETTINGFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:setting_button" );
                break;
        }

        m_middleware.get_petnumber();
    }

    private void init_view()
    {
        m_title_view = (TextView) findViewById( R.id.title_text );
        m_button_actionbutton = (ImageButton) findViewById(R.id.leftreturn_button);
        m_button_overviewbutton = (ImageButton) findViewById(R.id.overview_button);
        m_button_settingbutton = (ImageButton) findViewById(R.id.setting_button);
        m_button_addbutton = (ImageButton) findViewById(R.id.add_button);

        m_button_actionbutton.setOnClickListener(this);
        m_button_overviewbutton.setOnClickListener(this);
        m_button_settingbutton.setOnClickListener(this);
        m_button_addbutton.setOnClickListener(this);

        m_button_actionbutton.setVisibility(View.INVISIBLE);
        m_button_overviewbutton.setVisibility(View.INVISIBLE);
        m_button_settingbutton.setVisibility(View.INVISIBLE);
        m_button_addbutton.setVisibility(View.INVISIBLE);
    }

    private void reset_actionbutton( int id ){
        m_button_actionbutton.setVisibility(View.INVISIBLE);
        m_button_overviewbutton.setVisibility(View.INVISIBLE);
        m_button_settingbutton.setVisibility(View.INVISIBLE);
        m_button_addbutton.setVisibility(View.INVISIBLE);

        switch( id ) {
            case MAINFRAMGENT_ID:
                m_title_view.setText( R.string.petdiary );
                m_button_overviewbutton.setVisibility(View.VISIBLE);
                m_button_settingbutton.setVisibility(View.VISIBLE);
                break;
            case OVERVIEWFRAMGENT_ID:
                m_title_view.setText( R.string.overview );
                m_button_actionbutton.setVisibility(View.VISIBLE);
                break;
            case SETTINGFRAMGENT_ID:
                m_button_actionbutton.setVisibility(View.VISIBLE);
                m_title_view.setText( R.string.settting );
                break;
        }
    }

    private void active_fragment( int i ) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );
        switch (i) {
            case MAINFRAMGENT_ID:
                if (m_action_fragment_recode == null) {
                    m_action_fragment_recode = new actionfragment_actionrecode();
                    transaction.add(R.id.fragment, m_action_fragment_recode);
                } else {
                    transaction.show(m_action_fragment_recode);
                }
                break;
            case OVERVIEWFRAMGENT_ID:
                if ( m_action_fragment_overview == null )
                {
                    m_action_fragment_overview = new actionfragment_overview();
                    transaction.add(R.id.fragment, m_action_fragment_overview);
                }
                else
                {
                    transaction.show( m_action_fragment_overview );
                }
                break;
            case SETTINGFRAMGENT_ID:
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

        }
        transaction.commit();
        reset_actionbutton( i );
        m_fragment_id = i;
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
