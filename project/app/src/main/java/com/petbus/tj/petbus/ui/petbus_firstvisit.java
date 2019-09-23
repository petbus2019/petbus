package com.petbus.tj.petbus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

public class petbus_firstvisit extends petbus_profile{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("PetBus_Firstvisit", "In " +
                Thread.currentThread().getStackTrace()[2].getMethodName());

        setProfileBtnBackVisble(View.GONE);
        setProfileTitle(getString(R.string.addfirstpet));
        setProfileNextBtnName(getString(R.string.nextstep));
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
        addPet();
        Intent intent = new Intent(petbus_firstvisit.this, petbus_action.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("PetBus", "In Class:" + Thread.currentThread().getStackTrace()[1].getClassName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }
}