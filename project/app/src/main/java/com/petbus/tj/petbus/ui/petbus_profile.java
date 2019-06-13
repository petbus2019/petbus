package com.petbus.tj.petbus.ui;

import android.os.Bundle;
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

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("petbus_petmanage");
        tv.setOnClickListener(this);
    }


    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" );
        switch( view.getId() )
        {
            case R.id.sample_text:
                    Intent intent = new Intent("android.intent.action.petbus_action");
                    intent.setClass(petbus_profile.this, petbus_action.class);
                    startActivity(intent);
                break;
        }
    }

}