package com.petbus.tj.petbus.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.util.Calendar;
import java.util.Date;

public class petbus_firstvisit extends FragmentActivity {
    private Button btnNext;
    private TextView name, birth, weight;
    private RadioGroup gender, species;
    private middleware m_middleware = middleware_impl.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_firstvisit);

        btnNext = findViewById(R.id.btn_firstvisitNext);
        name = findViewById(R.id.id_inputName);
        birth = findViewById(R.id.id_inputBirth);
        weight = findViewById(R.id.id_inputWeight);
        gender = findViewById(R.id.id_inputGender);
        species = findViewById(R.id.id_inputSpecies);

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

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int genderVal = 0;
                int speciesVal = 0;
                String strBirth = birth.getText().toString();
                String strName = name.getText().toString();
                int weightVal = Integer.valueOf(weight.getText().toString()).intValue();
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)gender.getChildAt(i);
                    if(r.isChecked()){
                        genderVal = i;
                        break;
                    }
                }
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)species.getChildAt(i);
                    if(r.isChecked()){
                        speciesVal = i;
                        break;
                    }
                }
                addPet(strName,"NULL", strBirth,weightVal,genderVal,speciesVal );
                Intent intent = new Intent();
                intent.setClass(petbus_firstvisit.this,petbus_action.class);
                startActivity(intent);
            }
        });
    }

    private void addPet(String name, String photoPath, String birth, int weight, int gender, int species)
    {
        Toast.makeText(petbus_firstvisit.this, name+','+birth+","
                +weight+","+gender+","+species, Toast.LENGTH_LONG ).show();
        m_middleware.newPet(name, photoPath, birth, weight, gender, species);
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(petbus_firstvisit.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                petbus_firstvisit.this.birth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}

