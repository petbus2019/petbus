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
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class petbus_profiledit extends FragmentActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private ImageButton mBtnImg;
    private ImageButton mBtnSave;
    private ImageButton mBtnBack;
    private TextView mName, mBirth, mWeight, mTitle;
    private RadioGroup mGender, mSpecies;
	private Bitmap mCirclebitmap;
    private middleware mMiddleware = middleware_impl.getInstance();

    private Uri m_picture_uri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int IMAGE_REQUEST = 1889;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private int editPetId = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profileadd);

        mTitle = findViewById(R.id.txt_profileaddTitle);
        mBtnImg = findViewById(R.id.id_imgBtn);
        mBtnImg.setOnClickListener(this);
        mBtnBack = findViewById(R.id.btn_profileaddBack);
        mBtnBack.setOnClickListener(this);
        mBtnSave = findViewById(R.id.btn_profileaddSave);
        mBtnSave.setOnClickListener(this);
        mName = findViewById(R.id.id_inputName);
        mBirth = findViewById(R.id.id_inputBirth);
        mWeight = findViewById(R.id.id_inputWeight);
        mGender = findViewById(R.id.id_inputGender);
        mSpecies = findViewById(R.id.id_inputSpecies);

        mBirth.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        mBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPet();
    }

    @Override
    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" + view.getId());
        switch( view.getId() )
        {
            case R.id.id_imgBtn:
                trigger_getpicture();
                break;
            case R.id.btn_profileaddBack:
                //destroy this activity
                finish();
                break;
            case R.id.btn_profileaddSave:
                int genderVal = 0;
                int speciesVal = 0;
                //check whether's no input value
                if (mBirth.getText().length()==0 || mName.getText().length()==0 || mWeight.getText().length()==0)
                {
                    Log.i( "PetBusApp", "profileadd: your  is empty, please try again." );
                    break;
                }
                String strBirth = mBirth.getText().toString();
                String strName = mName.getText().toString();
                //eg. 5.0 kg, remove kg, keep 5.0
                double weightVal = Double.valueOf((mWeight.getText().toString()).split(" ")[0]);
                for (int i=0; i < mGender.getChildCount();i++){
                    RadioButton r=(RadioButton)mGender.getChildAt(i);
                    if(r.isChecked()){
                        genderVal = i;
                        break;
                    }
                }
                for (int i=0; i<mSpecies.getChildCount();i++){
                    RadioButton r=(RadioButton)mSpecies.getChildAt(i);
                    if(r.isChecked()){
                        speciesVal = i;
                        break;
                    }
                }
                editPet(strName, mCirclebitmap, strBirth, weightVal, genderVal, speciesVal );
                //destroy this activity
                finish();
        }
    }

    private void showPet()
    {
        Intent intent = getIntent();
        HashMap petItem=(HashMap)intent.getSerializableExtra("pet_info");

        System.out.println(petItem);
        editPetId = Integer.parseInt(petItem.get(mMiddleware.PETINFO_TYPE_ID).toString());
        String name = petItem.get(mMiddleware.PETINFO_TYPE_NAME).toString();
        String photo = petItem.get(mMiddleware.PETINFO_TYPE_PHOTO).toString();
        String weight = petItem.get(mMiddleware.PETINFO_TYPE_WEIGHT).toString() + " kg";
        String birth = petItem.get(mMiddleware.PETINFO_TYPE_BIRTH).toString();
        int gender = Integer.parseInt(petItem.get(mMiddleware.PETINFO_TYPE_GENDER).toString());
        int species = Integer.parseInt(petItem.get(mMiddleware.PETINFO_TYPE_SPECIES).toString());

        mTitle.setText(R.string.editpet);
        mName.setText(name);
        mName.setGravity(Gravity.CENTER);
        mWeight.setText(weight);
        mWeight.setGravity(Gravity.CENTER);
        mBirth.setText(birth);
        mBirth.setGravity(Gravity.CENTER);

        if (!photo.isEmpty()){
            Log.i( "PetBusApp_profiledit", "photo url is not empty, url is " + photo);
            File photofile = new File(photo);
            Uri uri = Uri.fromFile(photofile);
            mBtnImg.setImageURI(uri);
        }

        for (int i=0; i<mGender.getChildCount(); i++){
            RadioButton r=(RadioButton)mGender.getChildAt(i);
            if(i == gender){
                r.setChecked(true);
            }
            else{
                r.setChecked(false);
            }
        }

        for (int i=0; i<mSpecies.getChildCount();i++){
            RadioButton r=(RadioButton)mSpecies.getChildAt(i);
            if(i == species){
                r.setChecked(true);
            }
            else{
                r.setChecked(false);
            }
        }
    }

    private void editPet(String name, Bitmap circlebitmap, String birth, double weight,
                         int gender, int species)
    {
        //save photo now
        String photoPath = "";
        if (circlebitmap != null)
        {
            photoPath = saveBitmapAsFile( "PHOTO_",circlebitmap );
        }
        mMiddleware.editPet(editPetId, name, photoPath, birth, weight, gender, species);
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(petbus_profiledit.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                petbus_profiledit.this.mBirth.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
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
        com.petbus.tj.petbus.ui.CommomDialog.Builder builder = new com.petbus.tj.petbus.ui.CommomDialog.Builder(this);
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
            photoURI = FileProvider.getUriForFile( petbus_profiledit.this,
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
            //imgPath = ImageFilePath.getPath(this, uri);
            try {
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                Bitmap bitmap = Bitmap.createScaledBitmap(bm, 200, 200, true);
                CircleImageView circleImg = new CircleImageView(getApplicationContext());
                mCirclebitmap = circleImg.getCircleBitmap(bitmap, 0);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i( "PetBusApp", "onActivityResult：Do nothing, return");
            return ;
        }
        Log.i( "PetBusApp", "onActivityResult：setImageBitmap");
        mBtnImg.setAdjustViewBounds(true);
        mBtnImg.setImageBitmap(mCirclebitmap);
    }

    /*
    private Bitmap scaleCanvas(Bitmap bitmap, Rect rect)
    {
        Bitmap newBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        Bitmap temp = bitmap;

        //��Ի���bitmap��ӿ����
        PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        paint.setFilterBitmap(true); //��Bitmap�����˲�����
        paint.setAntiAlias(true);//���ÿ����
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
