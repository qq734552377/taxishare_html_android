package com.ucast.taxisharing.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ucast.taxisharing.R;
import com.ucast.taxisharing.app.ExceptionApplication;
import com.ucast.taxisharing.tools.MyDialog;
import com.ucast.taxisharing.tools.MyTools;

import java.io.File;




public class MySettingActivity extends AppCompatActivity {

    LinearLayout apk_version;
    LinearLayout update;
    LinearLayout clear_cache;
    LinearLayout clear_html_cache;

    Long fileSize = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);

        Toolbar toolbar = (Toolbar) this.findViewById(R.id.tool_bar_work_order);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        apk_version = (LinearLayout) findViewById(R.id.apk_version);
        update = (LinearLayout) findViewById(R.id.update);
        clear_cache = (LinearLayout) findViewById(R.id.clear_cache);
        clear_html_cache = (LinearLayout) findViewById(R.id.clear_html_cache);


        apk_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageInfo packageInfo = MyTools.getPackageInfo(ExceptionApplication.context, getString(R.string.app_packge_name));
                String data = MySettingActivity.this.getResources().getString(R.string.banbengming) + packageInfo
                        .versionName;
                showDialog(data);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        clear_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clera_cache();
            }


        });


        clear_html_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_html_cache();
            }
        });
    }
    private void clera_cache() {
        File file = new File(MainActivity.SAVED_CAMERA_IMAGE_DIR_PATH);
        if (!file.exists() || file.list().length==0) {
            MyDialog.showToast(this, getString(R.string.no_cache));
            return;
        }
        fileSize = 0L;
        showIsueDialog(getString(R.string.is_sure_clear_cache), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = delAllFile(MainActivity.SAVED_CAMERA_IMAGE_DIR_PATH);
                if (result) {
                    showDialog(MySettingActivity.this.getString(R.string.clear_cache_size) + fileSize / 1024 / 1024 +
                            " M");
                } else {
                    showDialog(MySettingActivity.this.getString(R.string.clear_cache_falut));
                }
            }
        });

    }

    private void clear_html_cache(){

        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        //WebView 缓存文件
        File webviewCacheDir = new File(getCacheDir().getAbsolutePath());


        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
            MyDialog.showDialog(this,getString(R.string.clear_html_cache_success));
        }


    }

    public void showDialog(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton(this.getResources().getString(R
                .string.queding), null).create();
        alertDialog.setTitle(this.getResources().getString(R.string.tishi));
        alertDialog.setMessage(s);
        alertDialog.show();
    }

    public void showIsueDialog(String s, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).setPositiveButton(this.getResources().getString(R
                .string.queding), listener).setNegativeButton(this.getResources().getString(R
                .string.cancle), null).create();
        alertDialog.setTitle(this.getResources().getString(R.string.tishi));
        alertDialog.setMessage(s);
        alertDialog.show();
    }


    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                fileSize += temp.length();
                temp.delete();
            }
        }
        flag = true;
        return flag;
    }

    private static final String TAG = "MySettingActivity";
    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
            MyDialog.showToast(this,getString(R.string.no_cache));
        }
    }
}
