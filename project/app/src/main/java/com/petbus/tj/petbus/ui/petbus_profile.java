package com.petbus.tj.petbus.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

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
        final List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();

        List<Integer> ids = m_middleware.getPetIds();
        for ( int i = 0; i < ids.size(); i++)
        {
            Map<String, Object> item = m_middleware.getPetInfo(ids.get(i));
            String photoStr = item.get(m_middleware.PETINFO_TYPE_PHOTO).toString();
            if (photoStr.isEmpty())
            {
                Uri tmpUri = resourceIdToUri(this,R.mipmap.imgbtn);
                item.put(m_middleware.PETINFO_TYPE_PHOTO, tmpUri);
            }
            listItem.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, listItem,
                R.layout.list_pet, new String[]{m_middleware.PETINFO_TYPE_NAME,m_middleware.PETINFO_TYPE_AGE,
                m_middleware.PETINFO_TYPE_WEIGHT, m_middleware.PETINFO_TYPE_PHOTO},
                new int[]{R.id.info_name, R.id.info_age, R.id.info_weight, R.id.info_photo});
        ListView listView = findViewById(R.id.list_pet);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> clickedItem = listItem.get(position);
                Log.i( "PetBusApp", "item = "+ clickedItem +", position = "+position+", id = "+ id );
                m_middleware.setCurrentPet((int)(clickedItem.get(m_middleware.PETINFO_TYPE_ID)));
                //destroy this activity
                finish();
            }
        });
    }

    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" );
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

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse("/" + context.getPackageName() + "android.resource://" + resourceId);
    }
}
