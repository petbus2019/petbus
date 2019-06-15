package com.petbus.tj.petbus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class petbus_profileadd extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profileadd);

        ImageButton mBtnBack = (ImageButton) findViewById(R.id.btn_profileaddBack);
        mBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" );
        switch( view.getId() )
        {
            case R.id.btn_profileaddBack:
                Intent intent = new Intent();
                intent.setClass(petbus_profileadd.this, petbus_profile.class);
                startActivity(intent);
                break;
        }
    }
}

