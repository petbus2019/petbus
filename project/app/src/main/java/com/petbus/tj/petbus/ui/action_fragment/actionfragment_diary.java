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
import android.view.View.OnClickListener;
import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Color;
import android.text.InputType;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.util.Log;
import android.graphics.Canvas;
import android.graphics.Rect;

import android.app.Dialog;
import android.content.Context;

import android.os.Environment;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

//https://blog.csdn.net/shaoyezhangliwei/article/details/79441799
// https://blog.csdn.net/yanzi1225627/article/details/21294553

// https://www.jianshu.com/p/281fd392870b
class HorizontalListViewAdapter extends BaseAdapter{
    private middleware m_middleware;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;
    private List<Integer> m_selected_index;
 
    public HorizontalListViewAdapter( Context context ){
        this.mContext = context;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_middleware = middleware_impl.getInstance();
    }
    public void set_petlist( List<Integer> pet_list ) {
        m_selected_index = pet_list;
    }
    @Override
    public int getCount() {
        return m_middleware.get_petnumber();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int position_to_id = position;
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.petview_select_item, null);
        if( convertView != null )
        {
            ImageView pet_image = (ImageView)convertView.findViewById(R.id.pet_photo);
            TextView pet_name = (TextView)convertView.findViewById(R.id.pet_nickname_text);
            Map<String,Object> pet_map = m_middleware.getPetIndex( position_to_id ); 
            Log.d("PetBusApp", "Select pet item:" + pet_map + " position_to_id:" + position_to_id );

            Bitmap bitmap = null;
            if( String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != "null" 
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != null 
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ).length() != 0
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != "" )
            {
                bitmap = BitmapFactory.decodeFile( String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) );
                pet_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                pet_image.setImageBitmap(bitmap);
            }
            else
            {
                pet_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                pet_image.setImageDrawable(mContext.getResources().getDrawable((R.mipmap.default_photo)));
            }
            
            Log.d("PetBusApp", "pet:" + String.valueOf( pet_map.get( middleware.PETINFO_TYPE_NAME ) ) );
            pet_name.setText( String.valueOf( pet_map.get( middleware.PETINFO_TYPE_NAME ) ) );
            // Log.d("PetBusApp", "pet:" + pet_map + " width:" + bitmap.getWidth() + "Height:" + bitmap.getHeight() );
        }
        String text_null = "";
        if( m_selected_index.contains(position) ) {
            text_null = "取消选择";
            convertView.setBackgroundColor(Color.GRAY);
        }
        else {
            text_null = "选择";
        }
        // TextView button_text = (TextView)convertView.findViewById(R.id.confirm_button);
        // button_text.setText( text_null );

        return convertView;
    }
 
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}

class diary_petselect_dialog extends Dialog implements OnItemClickListener,OnClickListener{
    private List<Integer> m_petid_list;
    private Context m_context;
    HorizontalListViewAdapter hListViewAdapter;

    public diary_petselect_dialog(Context context, List<Integer> pet_list) {
        super(context);
        m_petid_list = pet_list;
        m_context = context;
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "PetBus:onClick" + view.getId() );
        switch( view.getId() )
        {
            case R.id.confirm_button:
                Log.i( "PetBusApp", "PetBus:confirm_button" );
                dismiss();
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("PetBusApp", "diary_actionadapter onItemClick:" + position + "List:" + m_petid_list );
        if( m_petid_list.contains( (Integer)position ) )
        {
            m_petid_list.remove( (Integer)position );
            Log.d("PetBusApp", "diary_actionadapter contains" + position );
        }
        else
        {
            m_petid_list.add( (Integer)position );
            Log.d("PetBusApp", "diary_actionadapter not contains" + position );
        }
        hListViewAdapter.notifyDataSetChanged();
    }

    public diary_petselect_dialog(Context context, int theme, List<Integer> pet_list) {
        super(context, theme);
        m_context = context;
        m_petid_list = pet_list;
        m_context = context;
    }

    public void set_petlist( List<Integer> list ) {
        m_petid_list = list;
    }

    public List<Integer> get_petid_list(){
        return m_petid_list;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.petview_select );

        GridView hListView = (GridView )findViewById(R.id.horizon_listview);
        hListViewAdapter = new HorizontalListViewAdapter( m_context );
        hListView.setAdapter(hListViewAdapter);
        hListView.setOnItemClickListener( this );
        hListViewAdapter.set_petlist( m_petid_list );

        Button confirm_button = ( Button )findViewById( R.id.confirm_button );
        confirm_button.setOnClickListener( this );
    }

}

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
    LayoutInflater m_inflater;
    ViewGroup m_viewgroup;
    private ui_interface m_tigger;
    private TextView m_time_text;
    private EditText m_remark_edit;
    private ImageView m_imageview_picture;
    private Spinner m_action_item;
    private Button m_entry_button;
    private middleware m_middleware;
    private String m_picture_filename;
    private List<Integer> m_petid_list;
    private ImageView m_image_1;
    private ImageView m_image_2;
    private ImageView m_image_3;
    private ImageView m_image_4;
    private List<ImageView> m_image_list;

    private String saveBitmapAsFile(String name, Bitmap bitmap) {
        long sysTime = System.currentTimeMillis();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        String time = sDateFormat.format(new Date(sysTime));
        String file_name = getActivity().getCacheDir() + "/" + name + time + ".jpg";
        File saveFile = new File( file_name );
        FileOutputStream os = null;
        if( null == bitmap ){
            Log.d("PetBusApp", "bitmap is NULLLLLLLLL" + bitmap);
            return null;
        }
        try {
            Log.d("PetBusApp", "Saving File To Cache " + saveFile.getPath());
            os = new FileOutputStream(saveFile);
            Log.d("PetBusApp", "Saving File To file " + os);
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

    private Bitmap compressPixel( Bitmap src_map, String file_name ){
        Matrix matrix = new Matrix();
        matrix.setScale(0.1f, 0.1f);
        Bitmap bm_tmp = Bitmap.createBitmap(src_map, 0, 0, 
                                        src_map.getWidth(),//101dp
                                        src_map.getHeight(),//68dp
                                        matrix, true);
        Bitmap bm = Bitmap.createScaledBitmap(bm_tmp, dpToPx(101), dpToPx(68), true);
        Bitmap result = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        int dot = file_name.lastIndexOf('.');
        String prefix = file_name.substring( 0,dot );
        Log.i( "PetBusApp", "actionfragment_diary:compressPixel" + prefix );
        String thumbnail_file_name = prefix + "_thumbnail.jpg";
        File saveFile = new File( thumbnail_file_name );
        FileOutputStream os = null;
        if( null == bm ){
            Log.d("PetBusApp", "bm is NULLLLLLLLL" + bm);
            return null;
        }
        try {
            Log.d("PetBusApp", "Saving File To Cache " + saveFile.getPath());
            os = new FileOutputStream(saveFile);
            Log.d("PetBusApp", "Saving File To file " + os);
            Canvas canvas = new Canvas(result);
            Rect rect = new Rect(0, 0, bm.getWidth(), bm.getHeight());
            canvas.drawBitmap(result, null, rect, null);
            result.compress( Bitmap.CompressFormat.JPEG, 10, os );
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }

    public int picture_result( String picture_path ){
        Log.i( "PetBusApp", "actionfragment_diary:picture_result" + picture_path );

        Bitmap bitmap = BitmapFactory.decodeFile(picture_path);
        m_imageview_picture.setScaleType(ImageView.ScaleType.FIT_CENTER);
        m_imageview_picture.setImageBitmap(bitmap);
        m_picture_filename = saveBitmapAsFile( "picture",bitmap );
        if( null == m_picture_filename )
        {
            return 0;
        }
        compressPixel( bitmap, m_picture_filename );
        return 0;
    }

    public int picture_result( Bitmap pic ){
        m_imageview_picture.setImageBitmap(pic);
        m_picture_filename = saveBitmapAsFile( "picture",pic );
        if( null == m_picture_filename )
        {
            return 0;
        }
        compressPixel( pic, m_picture_filename );
        return 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
        m_inflater = inflater;
        m_viewgroup = container;
        m_image_list = new ArrayList<ImageView>();
        m_petid_list = new ArrayList<Integer>();

        m_middleware = middleware_impl.getInstance();
        View view = inflater.inflate(R.layout.actionfragment_diary, container, false);
        m_time_text = ( TextView )view.findViewById( R.id.record_time_text );
        m_imageview_picture = ( ImageView )view.findViewById( R.id.picture_button );
        m_action_item = ( Spinner ) view.findViewById( R.id.action_spinner );
        LinearLayout ll = (LinearLayout)view.findViewById(R.id.pet_select);
        ll.setOnClickListener(this);
        m_image_1 = ( ImageView )view.findViewById( R.id.pet_photo_1 );
        m_image_2 = ( ImageView )view.findViewById( R.id.pet_photo_2 );
        m_image_3 = ( ImageView )view.findViewById( R.id.pet_photo_3 );
        m_image_4 = ( ImageView )view.findViewById( R.id.pet_photo_4 );

        m_image_list.add( m_image_1 );
        m_image_list.add( m_image_2 );
        m_image_list.add( m_image_3 );
        m_image_list.add( m_image_4 );
        m_image_1.setVisibility( View.INVISIBLE );
        m_image_2.setVisibility( View.INVISIBLE );
        m_image_3.setVisibility( View.INVISIBLE );
        m_image_4.setVisibility( View.INVISIBLE );

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
        // m_imageview_picture.setMaxHeight(400);
        // m_imageview_picture.setMaxWidth(400);

        m_imageview_picture.setOnClickListener(this);
        m_entry_button.setOnClickListener(this);

        // diary_actionadapter arr_adapter = new diary_actionadapter( this.getActivity(), m_middleware.get_action_list() );
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.services_array, R.layout.spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        m_action_item.setAdapter(adapter);


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
            case R.id.pet_select:
                diary_petselect_dialog myDialog = new diary_petselect_dialog( getActivity(),R.style.dialog,m_petid_list );
                myDialog.set_petlist( m_petid_list );
                myDialog.show();
                myDialog.setOnDismissListener( new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        diary_petselect_dialog myDialog = (diary_petselect_dialog) dialog;
                        update_pet_list();
                        Log.i("PetBusApp", "11111 dismiss" + m_petid_list );
                    }
                });
                break;
        }
    }
    private void update_pet_list()
    {
        int position_to_id = 1;
        // int position_to_id = m_petid_list.get(0) + 1;
        Log.i( "PetBusApp", "pet_select:update_pet_list" + m_petid_list );
        m_image_1.setVisibility( View.INVISIBLE );
        m_image_2.setVisibility( View.INVISIBLE );
        m_image_3.setVisibility( View.INVISIBLE );
        m_image_4.setVisibility( View.INVISIBLE );

        for(int i = 0;i < m_petid_list.size(); i ++){
            position_to_id = m_petid_list.get(i);
            ImageView image = m_image_list.get( i );

            Map<String,Object> pet_map = m_middleware.getPetIndex( position_to_id );
            Bitmap bitmap = null;

            if( String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != "null" 
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != null 
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ).length() != 0
            && String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) != "" )
            {
                bitmap = BitmapFactory.decodeFile( String.valueOf( pet_map.get( middleware.PETINFO_TYPE_PHOTO ) ) );
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setImageBitmap(bitmap);
            }
            else
            {
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setImageDrawable(getActivity().getResources().getDrawable((R.mipmap.default_photo)));
            }
            image.setAdjustViewBounds(true);
            image.setMaxHeight(200);
            image.setMaxWidth(200);
            image.setVisibility( View.VISIBLE );
        }
        
    }

    private void do_record(){
        String text = m_time_text.getText().toString();
        String action_text = m_action_item.getSelectedItem().toString();
        String remark_text = m_remark_edit.getText().toString();
        ArrayList<String> picture_list = new ArrayList<String>();
        picture_list.add( m_picture_filename );
        if( 0 == m_petid_list.size() ){
            Toast.makeText(getActivity(), "未选择爱宠，请选择爱宠", Toast.LENGTH_LONG ).show();
            Log.i( "PetBusApp", "PetBus:add empty pet" );
            return ;
        }

        int re = m_middleware.new_record( text, m_petid_list, action_text, remark_text, picture_list );
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
        else {
            m_imageview_picture.setImageDrawable(getResources().getDrawable((R.mipmap.camera)));
            m_picture_filename = "";
            m_remark_edit.setText("");
        }
    }

    private int dpToPx(int dps) {
       return Math.round(getResources().getDisplayMetrics().density * dps);
    }
}
