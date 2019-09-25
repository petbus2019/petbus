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
import android.widget.Toast;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class petbus_profile extends FragmentActivity implements View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * The params of profile for each pet
     */
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BIRTH = "birth";
    public static final String WEIGHT = "weight";
    public static final String GENDER = "gender";
    public static final String SPECIES = "species";
    public static final String PHOTO = "photo";

    /**
     * The size of Photo (dp)
     */
    public static final int weight_n_height = 103;

    private ImageButton mBtnImg;
    private Button mBtnNext;
    private ImageButton mBtnBack;
    private TextView mName, mBirth, mWeight, mTitle;
    private RadioGroup mGender, mSpecies;
    private middleware mMiddleware = middleware_impl.getInstance();
    private HashMap<String,Object> mProfile = new HashMap<String, Object>();

    private Uri m_picture_uri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int IMAGE_REQUEST = 1889;

    public static final int MEDIA_TYPE_IMAGE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_profile);
        Log.e("PetBus", "In Class:" + this.getClass().getName()
                + ", Method:" + Thread.currentThread().getStackTrace()[2].getMethodName());

        mTitle = findViewById(R.id.txt_profileTitle);
        mBtnImg = findViewById(R.id.btn_profileImg);
        mBtnImg.setOnClickListener(this);
        mBtnBack = findViewById(R.id.btn_profileBack);
        mBtnBack.setOnClickListener(this);
        mBtnNext = findViewById(R.id.btn_profileNext);
        mBtnNext.setOnClickListener(this);
        mName = findViewById(R.id.input_profileName);
        mBirth = findViewById(R.id.input_profileBirth);
        mWeight = findViewById(R.id.input_profileWeight);
        mGender = findViewById(R.id.radio_profileGender);
        mSpecies = findViewById(R.id.radio_profileSpecies);

        //default photo url is empty
        mProfile.put(PHOTO, "");

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
        //showPet();
    }

    @Override
    public void onClick(View view) {
        Log.i( "PetBusApp", "onClick" + view.getId());
        switch( view.getId() )
        {
            case R.id.btn_profileImg:
                trigger_getpicture();
                break;
            case R.id.btn_profileBack:
                //destroy this activity
                finish();
                break;
            case R.id.btn_profileNext:
                int genderVal = 0;
                int speciesVal = 0;
                //check whether's no input value
                if (mBirth.getText().length()==0 || mName.getText().length()==0 || mWeight.getText().length()==0)
                {
                    Log.i( "PetBusApp", "profile: your  is empty, please try again." );
                    Toast.makeText(petbus_profile.this, R.string.inputEmpty, Toast.LENGTH_LONG ).show();
                    break;
                }
                mProfile.put(NAME, mName.getText().toString());
                mProfile.put(BIRTH, mBirth.getText().toString());
                //eg. 5.0 kg, remove kg, keep 5.0
                mProfile.put(WEIGHT, Double.valueOf((mWeight.getText().toString()).split(" ")[0]));

                for (int i=0; i < mGender.getChildCount();i++){
                    RadioButton r=(RadioButton)mGender.getChildAt(i);
                    if(r.isChecked()){
                        mProfile.put(GENDER, i);
                        break;
                    }
                }
                for (int i=0; i<mSpecies.getChildCount();i++){
                    RadioButton r=(RadioButton)mSpecies.getChildAt(i);
                    if(r.isChecked()){
                        mProfile.put(SPECIES, i);
                        break;
                    }
                }
                doNextBtnClick();
        }
    }

    protected void setProfileBtnBackVisble(final int visible)
    {
        mBtnBack.setVisibility(visible);
    }

    protected void setProfileTitle(final String title)
    {
        mTitle.setText(title);
    }

    protected void setProfileNextBtnName(final String nextStr)
    {
        mBtnNext.setText(nextStr);
    }

    protected void doNextBtnClick()
    {
        Log.e("PetBus_Profile", "doNextBtnClick()");
    }

    public void showProfile(HashMap petItem)
    {
        String photo = petItem.get(PHOTO).toString();
        int gender = Integer.parseInt(petItem.get(GENDER).toString());
        int species = Integer.parseInt(petItem.get(SPECIES).toString());

        //Set profile id
        mProfile.put(ID, Integer.parseInt(petItem.get(ID).toString()));

        mName.setText(petItem.get(NAME).toString());
        mWeight.setText(petItem.get(WEIGHT).toString() + " kg");
        mBirth.setText(petItem.get(BIRTH).toString());

        //photo url does NOT equal default_photo
        if (!photo.equals(String.valueOf(R.mipmap.default_photo))){
            Log.i( "PetBusApp", "Profile: photo url is not empty, url is " + photo);
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

    protected void setPetId(final int id)
    {
        mProfile.put(ID, id);
    }

    public void addPet()
    {
        mMiddleware.newPet(mProfile.get(NAME).toString(),
                mProfile.get(PHOTO).toString(),
                mProfile.get(BIRTH).toString(),
                Double.parseDouble(mProfile.get(WEIGHT).toString()),
                Integer.parseInt(mProfile.get(GENDER).toString()),
                Integer.parseInt(mProfile.get(SPECIES).toString()));
    }

    public void editPet()
    {
        mMiddleware.editPet(Integer.parseInt(mProfile.get(ID).toString()),
                mProfile.get(NAME).toString(),
                mProfile.get(PHOTO).toString(),
                mProfile.get(BIRTH).toString(),
                Double.parseDouble(mProfile.get(WEIGHT).toString()),
                Integer.parseInt(mProfile.get(GENDER).toString()),
                Integer.parseInt(mProfile.get(SPECIES).toString()));
    }

    protected void showDatePickDlg() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(petbus_profile.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                int realMon = monthOfYear + 1;
                petbus_profile.this.mBirth.setText(year + "-" + realMon + "-" + dayOfMonth);
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
            photoURI = FileProvider.getUriForFile( petbus_profile.this,
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
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try
            {
                Bitmap photpBitmap;
                int weightNheight = dp2px((float)weight_n_height);
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), m_picture_uri);
                Bitmap bitmap = Bitmap.createScaledBitmap(bm, weightNheight, weightNheight, true);
                CircleImageView circleImg = new CircleImageView(getApplicationContext());
                photpBitmap = circleImg.getCircleBitmap(bitmap, 0);
                Log.i( "PetBusApp", "onActivityResult：setImageBitmap");
                mBtnImg.setAdjustViewBounds(true);
                mBtnImg.setImageBitmap(photpBitmap);
                String photoPath = saveBitmapAsFile( "PHOTO_",photpBitmap );
                mProfile.put(PHOTO, photoPath);
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
                Bitmap photpBitmap;
                int weightNheight = dp2px((float)weight_n_height);
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                Bitmap bitmap = Bitmap.createScaledBitmap(bm, weightNheight, weightNheight, true);
                CircleImageView circleImg = new CircleImageView(getApplicationContext());
                photpBitmap = circleImg.getCircleBitmap(bitmap, 0);
                Log.i( "PetBusApp", "onActivityResult：setImageBitmap");
                mBtnImg.setAdjustViewBounds(true);
                mBtnImg.setImageBitmap(photpBitmap);
                String photoPath = saveBitmapAsFile( "PHOTO_",photpBitmap );
                mProfile.put(PHOTO, photoPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            Log.i( "PetBusApp", "onActivityResult：Do nothing, return");
        }
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

    /**
     * according the display resolution, transform dp to px
     */
    public int dp2px(float dpValue) {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

