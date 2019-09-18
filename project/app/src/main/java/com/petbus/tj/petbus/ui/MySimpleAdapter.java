package com.petbus.tj.petbus.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {
    private Context mContext;
    List<Map<String, Object>> mListData;
    private middleware mMiddleware = middleware_impl.getInstance();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        Log.e("PetBus_MySimpleAdapter", "In getView()");
        ImageButton btn=(ImageButton) v.findViewById(R.id.profile_editbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("PetBus_MySimpleAdapter", "In onClick()");
                Map<String, Object> item = mListData.get(position);
                final int petId = Integer.parseInt(item.get(mMiddleware.PETINFO_TYPE_ID).toString());
                Log.e("PetBus_MySimpleAdapter", "pedId ="+ String.valueOf(petId));
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMiddleware.setCurrentPet(petId);
                        Intent intent = new Intent();
                        intent.setClass(mContext, petbus_profiledit.class);
                        mContext.startActivity(intent);
                    }
                });
                dialog.setNegativeButton(R.string.del, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMiddleware.delPet(petId);
                        mListData.remove(position);
                        notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });
        return v;
    }

    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int
            resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        mListData = (List<Map<String, Object>>)data;
        // TODO Auto-generated constructor stub
    }
}
