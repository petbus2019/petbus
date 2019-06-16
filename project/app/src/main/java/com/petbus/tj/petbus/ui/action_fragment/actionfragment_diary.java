package com.petbus.tj.petbus.ui;

import com.petbus.tj.petbus.ui.R;
import android.os.Bundle;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageButton;
import android.util.Log;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.InputType;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

class diary_actionadapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
 
    public diary_actionadapter(Context pContext, List<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }
 
    @Override
    public int getCount() {
        return mList.size();
    }
 
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.actionfragment_overview_selectitem, null);
        if( convertView != null )
        {
            TextView _TextView1=(TextView)convertView.findViewById(R.id.action_text);
            _TextView1.setText(mList.get(position));
        }
        return convertView;
    }
}

public class actionfragment_diary extends Fragment implements OnClickListener
{
    private ui_interface m_tigger;
    private TextView m_time_text;
    private EditText m_remark_edit;
    private ImageView m_imageview_picture;
    private Spinner m_action_item;
    private Spinner m_pet_item;
    private Button m_entry_button;
    private middleware m_middleware;
    private String m_picture_filename;

    private String saveBitmapAsFile(String name, Bitmap bitmap) {
        long sysTime = System.currentTimeMillis();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        String time = sDateFormat.format(new Date(sysTime));
        String file_name = getActivity().getCacheDir() + "/" + name + time + ".jpg";
        File saveFile = new File( file_name );
        FileOutputStream os = null;
        try {
            Log.d("FileCache", "Saving File To Cache " + saveFile.getPath());
            os = new FileOutputStream(saveFile);
            bitmap.compress( Bitmap.CompressFormat.JPEG, 100, os );
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file_name;
    }

    public int picture_result( String picture_path ){
        Log.i( "PetBusApp", "actionfragment_diary:picture_result" + picture_path );

        Bitmap bitmap = BitmapFactory.decodeFile(picture_path);
        m_imageview_picture.setScaleType(ImageView.ScaleType.FIT_CENTER);
        m_imageview_picture.setImageBitmap(bitmap);
        m_picture_filename = saveBitmapAsFile( "picture",bitmap );
        return 0;
    }

    public int picture_result( Bitmap pic ){
        m_imageview_picture.setImageBitmap(pic);
        m_picture_filename = saveBitmapAsFile( "picture",pic );
        return 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){

        m_middleware = middleware_impl.getInstance();
        View view = inflater.inflate(R.layout.actionfragment_diary, container, false);
        m_time_text = ( TextView )view.findViewById( R.id.record_time_text );
        m_imageview_picture = ( ImageView )view.findViewById( R.id.picture_button );
        m_action_item = ( Spinner ) view.findViewById( R.id.action_spinner );
        m_pet_item = ( Spinner ) view.findViewById( R.id.pet_spinner );
        m_entry_button = ( Button ) view.findViewById( R.id.entry_button );
        m_remark_edit = ( EditText ) view.findViewById( R.id.remark_text_input );
        m_remark_edit.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.i( "PetBusApp", "actionfragment_diary:onTouch" + event );
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    m_remark_edit.setFocusable(false);
                    m_remark_edit.setInputType(InputType.TYPE_NULL);
                }
                return false;
            }
        });
        m_remark_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean focused) {
                Log.i( "PetBusApp", "m_remark_edit:onFocusChange:" + focused );
                // m_remark_edit.setInputType(InputType.TYPE_NULL);
                return ;
            }
        });
        m_imageview_picture.setAdjustViewBounds(true);
        m_imageview_picture.setMaxHeight(200);
        m_imageview_picture.setMaxWidth(200);

        m_imageview_picture.setOnClickListener(this);
        m_entry_button.setOnClickListener(this);

        diary_actionadapter arr_adapter = new diary_actionadapter( this.getActivity(), m_middleware.get_action_list() );
        m_action_item.setAdapter(arr_adapter);

        diary_actionadapter pet_adapter = new diary_actionadapter( this.getActivity(), m_middleware.get_petname_list() );
        m_pet_item.setAdapter(pet_adapter);
        return view;
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "actionfragment_diary:onClick" );
        switch( view.getId() )
        {
            case R.id.picture_button:
                m_tigger.trigger_getpicture();
                break;
            case R.id.entry_button:
                do_record();
                break;
        }
    }

    private void do_record(){
        String text = m_time_text.getText().toString();
        String action_text = m_action_item.getSelectedItem().toString();
        String remark_text = m_remark_edit.getText().toString();
        String petname_text = m_pet_item.getSelectedItem().toString();
        ArrayList<String> picture_list = new ArrayList<String>();
        picture_list.add( m_picture_filename );

        int re = m_middleware.new_record( text, petname_text, action_text, remark_text, picture_list );
        if( middleware.MIDDLEWARE_RETURN_OK == re )
        {
            Log.i( "PetBusApp", "PetBus:add succeeded" );
            m_tigger.trigger_datachange();
            m_tigger.trigger_change( ui_interface.MAINFRAMGENT_ID );
        }
    }
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        m_tigger = (ui_interface)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i( "PetBusApp", "PetBus:onResume " );
        long sysTime = System.currentTimeMillis();
        SimpleDateFormat sDateFormat = new SimpleDateFormat( middleware.DATE_FORMAT_FULL );
        String time = sDateFormat.format(new Date(sysTime));

        Log.i( "PetBusApp", "PetBus:onResume time:" + time );
        m_time_text.setText( time );
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i( "PetBusApp", "PetBus:onHiddenChanged :" + hidden );
        if( false == hidden ){
            long sysTime = System.currentTimeMillis();
            SimpleDateFormat sDateFormat = new SimpleDateFormat( middleware.DATE_FORMAT_FULL );
            String time = sDateFormat.format(new Date(sysTime));

            Log.i( "PetBusApp", "PetBus:onHiddenChanged time:" + time );
            m_time_text.setText( time );
        }
    }
}
