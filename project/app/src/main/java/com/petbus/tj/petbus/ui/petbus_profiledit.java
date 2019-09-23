package com.petbus.tj.petbus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

public class petbus_profiledit extends petbus_profile{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PetBus", "In Class:" + Thread.currentThread().getStackTrace()[1].getClassName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());

        setProfileTitle(getString(R.string.editpet));
        setProfileNextBtnName(getString(R.string.save));

        Intent intent = getIntent();
        HashMap petProfile=(HashMap)intent.getSerializableExtra("pet_info");
        setPetId(Integer.parseInt(petProfile.get(ID).toString()));
        showProfile(petProfile);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void doNextBtnClick() {
        super.doNextBtnClick();
        Log.e("PetBus", "In Class:" + Thread.currentThread().getStackTrace()[1].getClassName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());
        editPet();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PetBus", "In Class:" + Thread.currentThread().getStackTrace()[1].getClassName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}