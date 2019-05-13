package com.petbus.tj.petbus.ui;

//https://blog.csdn.net/chaoshenzhaoxichao/article/details/79871145

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;

public class petbus_welcome extends Activity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_welcome);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("petbus_welcome");
        tv.setOnClickListener(this);
    }

    public void onClick(View view) {
        Log.i( "PetBusApp", "PetBus:onClick" );
        switch( view.getId() )
        {
            case R.id.sample_text:
                // try{
                    //https://www.jianshu.com/p/19147a69e970
                    Intent intent = new Intent();
                    intent.setClass(petbus_welcome.this, petbus_petmanage.class);
                    startActivity(intent);
                // }
                // catch(ActivityNotFoundException e){
                //     Log.d("PetBusApp","找不到该活动");
                // }
                break;
        }
    }
}
