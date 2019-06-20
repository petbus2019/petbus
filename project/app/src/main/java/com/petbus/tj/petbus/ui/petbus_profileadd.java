package com.petbus.tj.petbus.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class petbus_profileadd extends FragmentActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ImageButton btnImg;
    private Button btnSave;
    private TextView name, birth, weight;
    private RadioGroup gender, species;
    private middleware m_middleware = middleware_impl.getInstance();

    private Uri m_picture_uri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int IMAGE_REQUEST = 1889;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int TIGGER_PHOTO = 0;
    private static final int TIGGER_CAMERA = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profileadd);

        btnImg = findViewById(R.id.id_imgBtn);
        name = findViewById(R.id.id_inputName);
        birth = findViewById(R.id.id_inputBirth);
        weight = findViewById(R.id.id_inputWeight);
        gender = findViewById(R.id.id_inputGender);
        species = findViewById(R.id.id_inputSpecies);
        ImageButton mBtnBack = (ImageButton) findViewById(R.id.btn_profileaddBack);
        mBtnBack.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_profileaddSave);
        btnSave.setOnClickListener(this);

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

    }

    @Override
    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" );
        switch( view.getId() )
        {
            case R.id.id_imgBtn:
                trigger_getpicture();
                break;
            case R.id.btn_profileaddBack:
                Intent intent = new Intent();
                intent.setClass(petbus_profileadd.this, petbus_profile.class);
                startActivity(intent);
                break;
            case R.id.btn_profileaddSave:
                int genderVal = 0;
                int speciesVal = 0;
                //check whether's no input value
                if (birth.getText().length()==0 || name.getText().length()==0 || weight.getText().length()==0)
                {
                    Log.i( "PetBusApp", "profileadd: your  is empty, please try again." );
                    Toast.makeText(petbus_profileadd.this, R.string.inputEmpty, Toast.LENGTH_LONG ).show();
                    break;
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
                addPet(strName,"NULL", strBirth,weightVal,genderVal,speciesVal );
                Intent intent2 = new Intent();
                intent2.setClass(petbus_profileadd.this,petbus_profile.class);
                startActivity(intent2);
        }
    }

    private void addPet(String name, String photoPath, String birth, double weight, int gender, int species)
    {
        Toast.makeText(petbus_profileadd.this, name+','+birth+","
                +weight+","+gender+","+species, Toast.LENGTH_LONG ).show();
        m_middleware.newPet(name, photoPath, birth, weight, gender, species);
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(petbus_profileadd.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                petbus_profileadd.this.birth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
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
            photoURI = FileProvider.getUriForFile( petbus_profileadd.this,
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
}

