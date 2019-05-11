package com.petbus.tj.petbus;

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


public class petbus_action extends FragmentActivity implements OnClickListener {
    private Fragment m_action_fragment_actionrecode;
    private Fragment m_action_fragment_overview;

    private ImageButton m_button_actionrecode;
    private ImageButton m_button_actionoverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_action);

        active_fragment( 0 );
        init_button();
    }

    public void onClick(View view) {
        Log.i( "PetBusApp", "PetBus:onClick" );
        switch( view.getId() )
        {
            case R.id.sample_text:
                // try{
                    //https://www.jianshu.com/p/19147a69e970
                    Intent intent = new Intent();
                    intent.setClass(petbus_action.this, petbus_welcome.class);
                    startActivity(intent);
                // }
                // catch(ActivityNotFoundException e){
                //     Log.d("PetBusApp","找不到该活动");
                // }
                break;
            case R.id.action_recode:
                Log.i( "PetBusApp", "PetBus:action_recode" );
                active_fragment(0);
                break;
            case R.id.action_overview:
                active_fragment(1);
                Log.i( "PetBusApp", "PetBus:action_overview" );
                break;
            case R.id.fragment:
                Log.i( "PetBusApp", "PetBus:fragment" );
                break;
        }
    }

    private void init_button()
    {
        m_button_actionrecode = (ImageButton) findViewById(R.id.action_recode);
        m_button_actionoverview = (ImageButton) findViewById(R.id.action_overview);

        m_button_actionrecode.setOnClickListener(this);
        m_button_actionoverview.setOnClickListener(this);
    }

    private void active_fragment(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );
        switch (i) {
            case 0:
                if (m_action_fragment_actionrecode == null) {
                    m_action_fragment_actionrecode = new actionfragment_actionrecode();
                    //m_action_fragment_actionrecode.setOnClickListener(this);
                    transaction.add(R.id.fragment, m_action_fragment_actionrecode);
                } else {
                    transaction.show(m_action_fragment_actionrecode);
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
        }
        transaction.commit();
    }
    private void hide_fragment(FragmentTransaction transaction) {
        if (m_action_fragment_actionrecode != null) {
            transaction.hide(m_action_fragment_actionrecode);
        }
        if (m_action_fragment_overview != null) {
            transaction.hide(m_action_fragment_overview);
        }


    }

}
