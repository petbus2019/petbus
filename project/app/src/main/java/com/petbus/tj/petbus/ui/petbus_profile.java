package com.petbus.tj.petbus.ui;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;

public class petbus_profile extends Activity  implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profile);

        ImageButton mBtnBack = (ImageButton) findViewById(R.id.btn_profileBack);
        ImageButton mBtnAdd = (ImageButton) findViewById(R.id.btn_profileAdd);
        mBtnBack.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }


    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" );
        Intent intent = new Intent();
        switch( view.getId() )
        {
            case R.id.btn_profileAdd:
                intent.setClass(petbus_profile.this, petbus_profileadd.class);
                startActivity(intent);
                break;
            case R.id.btn_profileBack:
                intent.setClass(petbus_profile.this,petbus_action.class);
                startActivity(intent);
                break;
        }
    }

}
