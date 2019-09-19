package com.petbus.tj.petbus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Intent;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class petbus_profile extends Activity implements OnClickListener {
    private middleware m_middleware = middleware_impl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profile);

        ImageButton mBtnBack = (ImageButton) findViewById(R.id.btn_profileBack);
        ImageButton mBtnAdd = (ImageButton) findViewById(R.id.btn_profileAdd);
        mBtnBack.setOnClickListener(this);
        mBtnAdd.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e( "PetBusApp_Profile", "petbus_profile onResume");
        final List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();

        List<Integer> ids = m_middleware.getPetIds();
        for ( int i = 0; i < ids.size(); i++)
        {
            Map<String, Object> item = m_middleware.getPetInfo(ids.get(i));

            String photoURL = item.get(m_middleware.PETINFO_TYPE_PHOTO).toString();
            if (photoURL.isEmpty()){
                Log.i( "PetBusApp_Profile", "item.get(m_middleware.PETINFO_TYPE_PHOTO) is" +
                        photoURL );
                item.put(m_middleware.PETINFO_TYPE_PHOTO, R.mipmap.imgbtn);
            }
            listItem.add(item);
        }
        MySimpleAdapter adapter = new MySimpleAdapter(this, listItem,
                R.layout.list_pet, new String[]{m_middleware.PETINFO_TYPE_NAME,m_middleware.PETINFO_TYPE_AGE,
                m_middleware.PETINFO_TYPE_WEIGHT, m_middleware.PETINFO_TYPE_PHOTO},
                new int[]{R.id.info_name, R.id.info_age, R.id.info_weight, R.id.info_photo});
        ListView listView = findViewById(R.id.list_pet);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> clickedItem = listItem.get(position);
                Log.i( "PetBusApp_Profile", "item = "+ clickedItem +", position = "+position+", id = "+ id );
                m_middleware.setCurrentPet((int)(clickedItem.get(m_middleware.PETINFO_TYPE_ID)));
                //destroy this activity
                finish();
            }
        });
    }

    public void onClick(View view) {
        Log.i( "PetBusApp_Profile", "onClick" );
        Intent intent = new Intent();
        switch( view.getId() )
        {
            case R.id.btn_profileAdd:
                intent.setClass(petbus_profile.this, petbus_profileadd.class);
                startActivity(intent);
                break;
            case R.id.btn_profileBack:
                //destroy this activity
                finish();
                break;
        }
    }
}
