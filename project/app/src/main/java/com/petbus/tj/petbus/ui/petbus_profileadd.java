package com.petbus.tj.petbus.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.util.Calendar;

public class petbus_profileadd extends FragmentActivity implements View.OnClickListener {
    private Button btnSave;
    private TextView name, birth, weight;
    private RadioGroup gender, species;
    private middleware m_middleware = middleware_impl.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profileadd);

        name = findViewById(R.id.id_inputName);
        birth = findViewById(R.id.id_inputBirth);
        weight = findViewById(R.id.id_inputWeight);
        gender = findViewById(R.id.id_inputGender);
        species = findViewById(R.id.id_inputSpecies);
        ImageButton mBtnBack = (ImageButton) findViewById(R.id.btn_profileaddBack);
        mBtnBack.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_profileaddSave);
        btnSave.setOnClickListener(this);

        birth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        birth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

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
            case R.id.btn_profileaddSave:
                int genderVal = 0;
                int speciesVal = 0;
                //check whether's no input value
                if (birth.getText().length()==0 || name.getText().length()==0 || weight.getText().length()==0)
                {
                    Log.i( "PetBusApp", "profileadd: your  is empty, please try again." );
                    Toast.makeText(petbus_profileadd.this, R.string.inputEmpty, Toast.LENGTH_LONG ).show();
                    break;
                }
                String strBirth = birth.getText().toString();
                String strName = name.getText().toString();
                double weightVal = Double.valueOf(weight.getText().toString());
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)gender.getChildAt(i);
                    if(r.isChecked()){
                        genderVal = i;
                        break;
                    }
                }
                for (int i=0; i<species.getChildCount();i++){
                    RadioButton r=(RadioButton)species.getChildAt(i);
                    if(r.isChecked()){
                        speciesVal = i;
                        break;
                    }
                }
                addPet(strName,"NULL", strBirth,weightVal,genderVal,speciesVal );
                Intent intent2 = new Intent();
                intent2.setClass(petbus_profileadd.this,petbus_profile.class);
                startActivity(intent2);
        }
    }

    private void addPet(String name, String photoPath, String birth, double weight, int gender, int species)
    {
        Toast.makeText(petbus_profileadd.this, name+','+birth+","
                +weight+","+gender+","+species, Toast.LENGTH_LONG ).show();
        m_middleware.newPet(name, photoPath, birth, weight, gender, species);
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(petbus_profileadd.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                petbus_profileadd.this.birth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}

