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

public class petbus_firstvisit extends FragmentActivity {
    Button btnNext;
    TextView name, birth, weight;
    RadioGroup gender, species;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_firstvisit);

        btnNext = findViewById(R.id.id_nextBtn);
        name = findViewById(R.id.id_inputName);
        birth = findViewById(R.id.id_inputBirth);
        weight = findViewById(R.id.id_inputWeight);
        gender = findViewById(R.id.id_inputGender);
        species = findViewById(R.id.id_inputSpecies);
        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String strBirth, strName, strWeight;
                String strGender="NULL";
                String strSpecies="NULL";
                strBirth = birth.getText().toString();
                strName = name.getText().toString();
                strWeight = weight.getText().toString();
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)gender.getChildAt(i);
                    if(r.isChecked()){
                        strGender = (String) r.getText();
                        break;
                    }
                }
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)species.getChildAt(i);
                    if(r.isChecked()){
                        strSpecies = (String) r.getText();
                        break;
                    }
                }
                Toast.makeText(petbus_firstvisit.this, strName+','+strBirth+","
                        +strWeight+","+strGender+","+strSpecies, Toast.LENGTH_LONG ).show();
            }
        });
    }
}

