package com.ucast.taxisharing.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ucast.taxisharing.R;
import com.ucast.taxisharing.tools.BitmapUtils;
import com.ucast.taxisharing.tools.CameraTools;
import com.ucast.taxisharing.tools.MyDialog;
import com.ucast.taxisharing.tools.SavePasswd;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;

import static com.ucast.taxisharing.app.ExceptionApplication.context;
import static com.ucast.taxisharing.tools.SavePasswd.getInstace;
import static junit.runner.Version.id;

@ContentView(R.layout.activity_regist_second)
public class RegistSecondActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;

    public static final int FORGROUND_CAMERA = 100;
    public static final int FORGROUND_ABLUM = 200;
    public static final int BACKGROUND_CAMERA = 101;
    public static final int BACKGROUND_ABLUM = 201;
    public static final String FORGROUND_PHOTO_ADRESS = "forground_idcard.jpg";
    public static final String BACKGROUND_PHOTO_ADRESS = "background_idcard.jpg";

    @ViewInject(R.id.regist_photo_forground)
    ImageView iv_forground_photo;
    @ViewInject(R.id.regist_photo_bacgroundground)
    ImageView iv_background_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar(getString(R.string.upload_photo));
    }

    private void initToolbar(String s) {
        toolbar.setTitle(s);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public static String SAVED_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/ucast/camera/";// 拍照路径

    @Event(R.id.regist_camera)
    private void getCameraForForgroundPhoto(View v) {

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }

        String cameraPath = SAVED_IMAGE_DIR_PATH + FORGROUND_PHOTO_ADRESS;
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        String out_file_path = SAVED_IMAGE_DIR_PATH;
        File dir = new File(out_file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        } // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(new File(cameraPath));
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, FORGROUND_CAMERA);
    }

    @Event(R.id.regist_camera_background)
    private void getCameraForBackgroundPhoto(View v) {
        String cameraPath = SAVED_IMAGE_DIR_PATH + BACKGROUND_PHOTO_ADRESS;
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        String out_file_path = SAVED_IMAGE_DIR_PATH;
        File dir = new File(out_file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        } // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(new File(cameraPath));
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, BACKGROUND_CAMERA);


    }

    @Event(R.id.regist_album)
    private void getForgroundPhotoFromAblum(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, FORGROUND_ABLUM);

    }

    @Event(R.id.regist_album_background)
    private void getBackgroundPhotoFromAblum(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, BACKGROUND_ABLUM);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK){
            if(requestCode==FORGROUND_CAMERA){
                String path=  BitmapUtils.compressImageUpload("forground_photo_press.jpg",SAVED_IMAGE_DIR_PATH+FORGROUND_PHOTO_ADRESS);
                SavePasswd.getInstace().save("forground_photo",path);
                this.iv_forground_photo.setImageDrawable(Drawable.createFromPath(path));
                return;
            }
            if(requestCode==BACKGROUND_CAMERA){
                String path=  BitmapUtils.compressImageUpload("background_photo_press.jpg",SAVED_IMAGE_DIR_PATH+BACKGROUND_PHOTO_ADRESS);
                SavePasswd.getInstace().save("background_photo",path);
                this.iv_background_photo.setImageDrawable(Drawable.createFromPath(path));
                return;
            }

            String absolutePath="";
            try {
                Uri uri = data.getData();
                absolutePath = CameraTools.getAbsolutePath(this, uri);
                MyDialog.showDialog(this, "path=" + absolutePath);


                if (requestCode == FORGROUND_ABLUM) {
                    String path = BitmapUtils.compressImageUpload("forground_photo_press.jpg", absolutePath);
                    SavePasswd.getInstace().save("forground_photo", path);
                    this.iv_forground_photo.setImageDrawable(Drawable.createFromPath(path));
                }else if (requestCode == BACKGROUND_ABLUM) {
                    String path = BitmapUtils.compressImageUpload("background_photo_press.jpg", absolutePath);
                    this.iv_background_photo.setImageDrawable(Drawable.createFromPath(path));
                    SavePasswd.getInstace().save("background_photo", path);
                }
            } catch (Exception e) {
                e.printStackTrace();
                MyDialog.showDialog(this,getString(R.string.nodata));
            }

        }



    }
}
