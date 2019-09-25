package com.petbus.tj.petbus.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class petbus_profileadd extends petbus_profile{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PetBus", "In Class:" + this.getClass().getName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());

        setProfileTitle(getString(R.string.addpet));
        setProfileNextBtnName(getString(R.string.save));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void doNextBtnClick() {
        super.doNextBtnClick();
        Log.e("PetBus", "In Class:" + this.getClass().getName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());
        addPet();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PetBus", "In Class:" + this.getClass().getName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}