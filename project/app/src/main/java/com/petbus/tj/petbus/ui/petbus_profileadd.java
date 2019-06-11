package com.petbus.tj.petbus.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class petbus_profileadd extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profileadd);

        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("petbus_petmanage");
        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

