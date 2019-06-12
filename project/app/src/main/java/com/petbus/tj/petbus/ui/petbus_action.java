package com.petbus.tj.petbus.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix; 
import android.view.LayoutInflater;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.Manifest;
import android.provider.MediaStore;
import android.provider.DocumentsContract;
import android.content.ContentUris;
import android.content.ContentResolver;

import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Date;

import com.petbus.tj.petbus.middleware.middleware;
import com.petbus.tj.petbus.middleware.middleware_impl;

class FileUtils {
    public static String getFilePathByUri(Context context, Uri uri) {
        String path = null;
        Log.i( "PetBusApp", "the uri is " + uri );
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }

        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}

class ImageFilePath {

    /**
     * Method for return file path of Gallery image
     * 
     * @param context
     * @param uri
     * @return path of the selected image file from gallery
     */
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     * 
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
            String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }
}

public class petbus_action extends FragmentActivity implements OnClickListener,ui_interface,
                                                    ActivityCompat.OnRequestPermissionsResultCallback {
    private Fragment m_action_fragment_record;
    private Fragment m_action_fragment_overview;
    private actionfragment_diary m_action_fragment_diary;

    private ImageButton m_button_actionbutton;
    private ImageButton m_button_overviewbutton;
    private ImageButton m_button_settingbutton;
    private ImageButton m_button_addbutton;
    private TextView m_title_view;

    private middleware m_middleware;

    private Uri m_picture_uri;

    private static final int CAMERA_REQUEST = 1888;
    private static final int IMAGE_REQUEST = 1889;
    
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static final int TIGGER_PHOTO = 0;
    private static final int TIGGER_CAMERA = 1;

    private int m_fragment_id = ui_interface.MAINFRAMGENT_ID;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petbus_action);

        m_middleware = middleware_impl.getInstance();

        init_view();
        active_fragment( 0 );
    }

    public void onClick( View view ) {
        Log.i( "PetBusApp", "PetBus:onClick" + view.getId() );
        switch( view.getId() )
        {
            case R.id.leftreturn_button:
                Log.i( "PetBusApp", "PetBus:action_record" );
                active_fragment(ui_interface.MAINFRAMGENT_ID);
                break;
            case R.id.overview_button:
                active_fragment(ui_interface.OVERVIEWFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:overview_button" );
                break;
            case R.id.setting_button:
                active_fragment(ui_interface.SETTINGFRAMGENT_ID);
                Log.i( "PetBusApp", "PetBus:setting_button" );
                break;
        }
    }

    private void init_view()
    {
        m_title_view = (TextView) findViewById( R.id.title_text );
        m_button_actionbutton = (ImageButton) findViewById(R.id.leftreturn_button);
        m_button_overviewbutton = (ImageButton) findViewById(R.id.overview_button);
        m_button_settingbutton = (ImageButton) findViewById(R.id.setting_button);
        m_button_addbutton = (ImageButton) findViewById(R.id.add_button);

        m_button_actionbutton.setOnClickListener(this);
        m_button_overviewbutton.setOnClickListener(this);
        m_button_settingbutton.setOnClickListener(this);
        m_button_addbutton.setOnClickListener(this);

        m_button_actionbutton.setVisibility(View.INVISIBLE);
        m_button_overviewbutton.setVisibility(View.INVISIBLE);
        m_button_settingbutton.setVisibility(View.INVISIBLE);
        m_button_addbutton.setVisibility(View.INVISIBLE);
    }

    private void reset_actionbutton( int id ){
        m_button_actionbutton.setVisibility(View.INVISIBLE);
        m_button_overviewbutton.setVisibility(View.INVISIBLE);
        m_button_settingbutton.setVisibility(View.INVISIBLE);
        m_button_addbutton.setVisibility(View.INVISIBLE);

        switch( id ) {
            case ui_interface.MAINFRAMGENT_ID:
                m_title_view.setText( R.string.petdiary );
                m_button_overviewbutton.setVisibility(View.VISIBLE);
                m_button_settingbutton.setVisibility(View.VISIBLE);
                break;
            case ui_interface.OVERVIEWFRAMGENT_ID:
                m_title_view.setText( R.string.overview );
                m_button_actionbutton.setVisibility(View.VISIBLE);
                break;
            case ui_interface.SETTINGFRAMGENT_ID:
                m_button_actionbutton.setVisibility(View.VISIBLE);
                m_title_view.setText( R.string.addrecord );
                break;
        }
    }

    private void active_fragment( int i ) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hide_fragment( transaction );
        switch (i) {
            case ui_interface.MAINFRAMGENT_ID:
                if (m_action_fragment_record == null) {
                    m_action_fragment_record = new actionfragment_actionrecord();
                    transaction.add(R.id.fragment, m_action_fragment_record);
                } else {
                    transaction.show(m_action_fragment_record);
                }
                break;
            case ui_interface.OVERVIEWFRAMGENT_ID:
                if ( m_action_fragment_overview == null )
                {
                    m_action_fragment_overview = new actionfragment_overview();
                    transaction.add(R.id.fragment, m_action_fragment_overview);
                }
                else
                {
                    transaction.show( m_action_fragment_overview );
                }
                break;
            case ui_interface.SETTINGFRAMGENT_ID:
                if ( m_action_fragment_diary == null )
                {
                    m_action_fragment_diary = new actionfragment_diary();
                    transaction.add(R.id.fragment, m_action_fragment_diary);
                }
                else
                {
                    transaction.show( m_action_fragment_diary );
                }
                break;

        }
        transaction.commit();
        reset_actionbutton( i );
        m_fragment_id = i;
    }


    private void hide_fragment(FragmentTransaction transaction) {
        if (m_action_fragment_record != null) {
            transaction.hide(m_action_fragment_record);
        }
        if (m_action_fragment_overview != null) {
            transaction.hide(m_action_fragment_overview);
        }
        if (m_action_fragment_diary != null) {
            transaction.hide(m_action_fragment_diary);
        }
    }

    private Bitmap getBitmapFromUrl(String url, double width, double height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置了此属性一定要记得将值设置为false
        Log.i( "PetBusApp", "the file name is null !!!!!!!!!" + url );
        Bitmap bitmap = BitmapFactory.decodeFile(url);
        // 防止OOM发生
        options.inJustDecodeBounds = false;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = 1;
        float scaleHeight = 1;
//        try {
//            ExifInterface exif = new ExifInterface(url);
//            String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        // 按照固定宽高进行缩放
        // 这里希望知道照片是横屏拍摄还是竖屏拍摄
        // 因为两种方式宽高不同，缩放效果就会不同
        // 这里用了比较笨的方式
        if(mWidth <= mHeight) {
            scaleWidth = (float) (width/mWidth);
            scaleHeight = (float) (height/mHeight);
        } else {
            scaleWidth = (float) (height/mWidth);
            scaleHeight = (float) (width/mHeight);
        }
        // 按照固定大小对图片进行缩放
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
        // 用完了记得回收
        bitmap.recycle();
        return newBitmap;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String imgPath = "";
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), m_picture_uri);
                m_action_fragment_diary.picture_result( bitmap );
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }

            //this work fine.
            // Bitmap photo = (Bitmap) data.getExtras().get("data");
            // if( null != photo ){
            //     m_action_fragment_diary.picture_result( photo );
            // }
            // else
            // {
            //     Log.i( "PetBusApp", "the file name is null !!!!!!!!!" );
            // }
        }
        else if( requestCode == IMAGE_REQUEST && resultCode == RESULT_OK ){
            Uri uri = data.getData();
            imgPath = ImageFilePath.getPath( this, uri );
            Log.i( "PetBusApp", "the file name is (" + imgPath + ")" );
            m_action_fragment_diary.picture_result( imgPath );
        }

    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i( "PetBusApp", "onRequestPermissionsResult" );
        ActivityCompat.requestPermissions(this, new String[]{
            Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE
        },1);
    }

    public int show_list(){
        CommomDialog.Builder builder = new CommomDialog.Builder(this);
        builder.setMessage("请选择图片来源");
        builder.setTitle("请选择");

        builder.setCameraButton(R.string.actionphoto, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i( "PetBusApp", "确定" );
                dialog.dismiss();
                trigger_photo();
            }
        });
        builder.setPhotoButton(R.string.actioncamera,new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i( "PetBusApp", "取消" );
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

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image;
        try{
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        }
        catch ( Exception e ){
            return null;
        }

        return image;
    }

    private Uri getOutputMediaFileUri(int type)
    {
        Uri photoURI;
        try{
            photoURI = FileProvider.getUriForFile( petbus_action.this,
                BuildConfig.APPLICATION_ID + ".provider",createImageFile());
        }
        catch (Exception e) {
            return null;
        }
        return photoURI;
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type)
    {
        File mediaStorageDir = null;
        try
        {
            mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                                              Environment.DIRECTORY_PICTURES),
                                                              "MyCameraApp");

            Log.d("PetBusApp", "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d("PetBusApp", "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("PetBusApp",
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }
        else if (type == MEDIA_TYPE_VIDEO)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        }
        else
        {
            return null;
        }

        return mediaFile;
    }

    public void trigger_change( int fragment ){
        active_fragment( fragment );
        Log.i( "PetBusApp", "trigger_change  " + fragment );
        return ;
    }
}
