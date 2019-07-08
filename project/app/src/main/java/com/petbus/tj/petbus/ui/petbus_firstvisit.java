package com.petbus.tj.petbus.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;
import com.petbus.tj.petbus.ui.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class petbus_firstvisit extends FragmentActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Uri m_picture_uri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int IMAGE_REQUEST = 1889;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int TIGGER_PHOTO = 0;
    private static final int TIGGER_CAMERA = 1;

    private ImageButton btnImg;
    private Button btnNext;
    private TextView name, birth, weight;
    private RadioGroup gender, species;
    private Bitmap mCirclebitmap;
    private middleware m_middleware = middleware_impl.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_firstvisit);

        btnImg = findViewById(R.id.id_imgBtn);
        btnNext = findViewById(R.id.btn_firstvisitNext);
        name = findViewById(R.id.id_inputName);
        birth = findViewById(R.id.id_inputBirth);
        weight = findViewById(R.id.id_inputWeight);
        gender = findViewById(R.id.id_inputGender);
        species = findViewById(R.id.id_inputSpecies);

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trigger_getpicture();
            }
        });

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
                //check whether's no input value
                if (birth.getText().length()==0 || name.getText().length()==0 || weight.getText().length()==0)
                {
                    Log.i( "PetBusApp", "profileadd: your  is empty, please try again." );
                    Toast.makeText(petbus_firstvisit.this, R.string.inputEmpty, Toast.LENGTH_LONG ).show();
                    return;
                }
                String strBirth = birth.getText().toString();
                String strName = name.getText().toString();
                double weightVal = Double.valueOf(weight.getText().toString());
                for (int i=0; i<gender.getChildCount();i++){
                    RadioButton r=(RadioButton)gender.getChildAt(i);
                    if(r.isChecked()){
                        genderVal = i;
                        break;
                    }
                }
                for (int i=0; i<species.getChildCount();i++){
                    RadioButton r=(RadioButton)species.getChildAt(i);
                    if(r.isChecked()){
                        speciesVal = i;
                        break;
                    }
                }
                addPet(strName,mCirclebitmap,strBirth,weightVal,genderVal,speciesVal );
                Intent intent = new Intent();
                intent.setClass(petbus_firstvisit.this,petbus_action.class);
                startActivity(intent);
            }
        });
    }

    private void addPet(String name, Bitmap circlebitmap, String birth, double weight, int gender, int species)
    {
        Toast.makeText(petbus_firstvisit.this, name+','+birth+","
                +weight+","+gender+","+species, Toast.LENGTH_LONG ).show();
        //save photo now
        String photoPath = saveBitmapAsFile( "PHOTO_",circlebitmap );
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i( "PetBusApp", "onRequestPermissionsResult" );
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
        },1);
    }

    public int show_list(){
        CommomDialog.Builder builder = new CommomDialog.Builder(this);
        String photo_dialog_title = getResources().getString(R.string.photo_dialog_title);
        String photo_dialog_message = getResources().getString(R.string.photo_dialog_message);
        builder.setMessage( photo_dialog_message );
        builder.setTitle( photo_dialog_title );

        builder.setCameraButton(R.string.actionphoto, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i( "PetBusApp", "photo" );
                dialog.dismiss();
                trigger_photo();
            }
        });
        builder.setPhotoButton(R.string.actioncamera,new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i( "PetBusApp", "camera" );
                dialog.dismiss();
                trigger_camera();
            }
        });
        builder.create().show();
        return 0;
    }

    public void trigger_getpicture(){
        int id = show_list();
    }

    public void trigger_photo(){
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    public void trigger_camera(){
        int checkCallPhonePermission = ContextCompat.checkSelfPermission( this, Manifest.permission.CAMERA);
        if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST);
            return;
        }
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        m_picture_uri = getOutputMediaFileUri( MEDIA_TYPE_IMAGE );
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, m_picture_uri);
        Log.i( "PetBusApp", "picture path is " + m_picture_uri );
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void trigger_change( int fragment ){
        Log.i( "PetBusApp", "trigger_change  " + fragment );
        return ;
    }
    public void trigger_datachange(){
        return ;
    }

    private Uri getOutputMediaFileUri(int type)
    {
        Uri photoURI;
        try{
            photoURI = FileProvider.getUriForFile( petbus_firstvisit.this,
                    BuildConfig.APPLICATION_ID + ".provider",createImageFile());
        }
        catch (Exception e) {
            return null;
        }
        return photoURI;
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image;
        try{
            image = File.createTempFile( imageFileName, ".jpg", storageDir );
        }
        catch ( Exception e ){
            return null;
        }

        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String imgPath = "";
        mCirclebitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.imgbtn);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try
            {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), m_picture_uri);
                Bitmap bitmap = Bitmap.createScaledBitmap(bm, 200, 200, true);
                CircleImageView circleImg = new CircleImageView(getApplicationContext());
                mCirclebitmap = circleImg.getCircleBitmap(bitmap, 0);
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        else
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imgPath = ImageFilePath.getPath(this, uri);
            try {
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                Bitmap bitmap = Bitmap.createScaledBitmap(bm, 200, 200, true);
                CircleImageView circleImg = new CircleImageView(getApplicationContext());
                mCirclebitmap = circleImg.getCircleBitmap(bitmap, 0);
			} catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        btnImg.setAdjustViewBounds(true);
        //btnImg.setMaxHeight(200);
        //btnImg.setMaxWidth(200);
        btnImg.setImageBitmap(mCirclebitmap);
    }

	/*
    private Bitmap scaleCanvas(Bitmap bitmap, Rect rect)
    {
        Bitmap newBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        Bitmap temp = bitmap;

        //针对绘制bitmap添加抗锯齿
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setFilterBitmap(true); //对Bitmap进行滤波处理
        paint.setAntiAlias(true);//设置抗锯齿
        canvas.setDrawFilter(pfd);
        canvas.drawBitmap(temp, null, rect, paint);

        return newBitmap;
    }*/

    private String saveBitmapAsFile(String name, Bitmap bitmap) {
        long sysTime = System.currentTimeMillis();
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm");
        String time = sDateFormat.format(new Date(sysTime));
        String file_name = getCacheDir() + "/" + name + time + ".png";
        File saveFile = new File( file_name );

        FileOutputStream os = null;
        try {
            Log.d("FileCache", "Saving File To Cache " + saveFile.getPath());
            os = new FileOutputStream(saveFile);
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, os );
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file_name;
    }

}

