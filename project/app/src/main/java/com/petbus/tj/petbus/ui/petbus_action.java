package com.petbus.tj.petbus.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;

import android.Manifest;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;


public class petbus_action extends FragmentActivity implements OnClickListener,ui_interface,
                                                    ActivityCompat.OnRequestPermissionsResultCallback {
    private Fragment m_action_fragment_recode;
    private Fragment m_action_fragment_overview;
    private Fragment m_action_fragment_diary;

    private ImageButton m_button_actionbutton;
    private ImageButton m_button_overviewbutton;
    private ImageButton m_button_settingbutton;
    private ImageButton m_button_addbutton;
    private TextView m_title_view;

    private middleware m_middleware;

    private static final int CAMERA_REQUEST = 1888;

    private int m_fragment_id = ui_interface.MAINFRAMGENT_ID;
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_action);

        m_middleware = middleware_impl.getInstance();

        init_view();
        active_fragment( 0 );
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "PetBus:onClick" + view.getId() );
        switch( view.getId() )
        {
            case R.id.leftreturn_button:
                Log.i( "PetBusApp", "PetBus:action_recode" );
                active_fragment(ui_interface.MAINFRAMGENT_ID);
                break;
            case R.id.overview_button:
                active_fragment(ui_interface.OVERVIEWFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:overview_button" );
                break;
            case R.id.setting_button:
                active_fragment(ui_interface.SETTINGFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:setting_button" );
                break;
        }
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
            case ui_interface.MAINFRAMGENT_ID:
                m_title_view.setText( R.string.petdiary );
                m_button_overviewbutton.setVisibility(View.VISIBLE);
                m_button_settingbutton.setVisibility(View.VISIBLE);
                break;
            case ui_interface.OVERVIEWFRAMGENT_ID:
                m_title_view.setText( R.string.overview );
                m_button_actionbutton.setVisibility(View.VISIBLE);
                break;
            case ui_interface.SETTINGFRAMGENT_ID:
                m_button_actionbutton.setVisibility(View.VISIBLE);
                m_title_view.setText( R.string.addrecode );
                break;
        }
    }

    private void active_fragment( int i ) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );
        switch (i) {
            case ui_interface.MAINFRAMGENT_ID:
                if (m_action_fragment_recode == null) {
                    m_action_fragment_recode = new actionfragment_actionrecode();
                    transaction.add(R.id.fragment, m_action_fragment_recode);
                } else {
                    transaction.show(m_action_fragment_recode);
                }
                break;
            case ui_interface.OVERVIEWFRAMGENT_ID:
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
            case ui_interface.SETTINGFRAMGENT_ID:
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // imageIV.setImageBitmap(photo);
            Log.i( "PetBusApp", "PetBus:onActivityResult" );
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i( "PetBusApp", "onRequestPermissionsResult" );
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
        },1);
    }
    public void trigger_camera(){
        int checkCallPhonePermission = ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA);
        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);
            return;
        }
        // https://www.cnblogs.com/dubo-/p/7927821.html
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
        // File outputImage = new File(Environment.getExternalStorageDirectory(),"tempImage" + ".jpg");
        // Uri imageUri = Uri.fromFile(outputImage);
        // cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void trigger_change( int fragment ){
        active_fragment( fragment );
        Log.i( "PetBusApp", "trigger_change  " + fragment );
        return ;
    }
}
