package com.ucast.taxisharing.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.haha.perflib.Main;
import com.ucast.taxisharing.R;
import com.ucast.taxisharing.tools.MyDialog;
import com.ucast.taxisharing.tools.SavePasswd;
import com.ucast.taxisharing.user_permision.GetUsePermision;

import java.io.File;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {
    //DrawerLayout中的左侧菜单控件
    private NavigationView mNavigationView;
    //DrawerLayout控件
    private DrawerLayout mDrawerLayout;


    public static String SAVED_CAMERA_IMAGE_DIR_PATH =
            Environment.getExternalStorageDirectory().getPath()
                    + "/ucast/taxishare/camera/";// 拍照路径
    public static final String APP_CACAHE_DIRNAME = "/taxishare_web_cache";

    ProgressBar mRingProgressBar;
    private Dialog cameraOrPhoto;
    private TextView username;
    private WebView webView;
    private LinearLayout myLayout;
    private WebSettings settings;
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上

    public static String URL_UCAST_HOST = "http://58.246.122.118:8800/Taxisharing";
//    public static String URL_UCAST_HOST = "http://192.168.0.132/taxi";
    public static String URL_UCAST_MAIN = URL_UCAST_HOST + "/#/main2";
    public static String URL_UCAST_SEARCH = URL_UCAST_HOST + "/#/search";
    public static String URL_UCAST_ACCOUNT= URL_UCAST_HOST + "/#/sidemenu/account";
    public static String URL_UCAST_LOGIN = URL_UCAST_HOST + "/#/login";
    public static String MAIL = "mailto:";
    public static String TEL = "tel:";
    public static final int FILE_SELECT_CODE = 908;
    public static final int Camera_SELECT_CODE = 909;
    public static final int TOKEN_FRESH = 2389;
    public static final int USERNAME_FRESH = 2489;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        GetUsePermision.requestMultiPermissions(this,mPermissionGrant);

        mRingProgressBar =(ProgressBar) findViewById(R.id.progress_bar);
        mRingProgressBar.setVisibility(View.INVISIBLE);

        initDrawLayout();
        initWebview();
        username = (TextView) findViewById(R.id.mydrawlayout_username);
        String url = SavePasswd.getInstace().get("url");
        if(url == "" || !url.contains(URL_UCAST_HOST)){
            webView.loadUrl(URL_UCAST_LOGIN);
        }else{
            webView.loadUrl(url);
        }
    }

    private void initDrawLayout() {
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(naviListener);
        mNavigationView.setBackgroundColor(getResources().getColor(R.color.nav_background_color));
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                MyDialog.showToast(MainActivity.this,"打开了");
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void initWebview() {
//        myLayout = (LinearLayout)this.findViewById(R.id.mylayout);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        webView = new WebView(getApplicationContext());
//        webView.setLayoutParams(params);
//        myLayout.addView(webView);
        webView = (WebView) this.findViewById(R.id.webview);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(
                    WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());


        // 设置浏览器属性
        settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8"); // 默认编码
        settings.setJavaScriptEnabled(true); // JS 交互
        settings.setLoadsImagesAutomatically(true);//设置自动加载图片
        settings.setUseWideViewPort(true);//可任意比例缩放

        //缩放操作
        settings.setSupportZoom(false); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(true); //隐藏原生的缩放控件

        settings.setLoadWithOverviewMode(true);
        settings.setAllowFileAccessFromFileURLs(true);

        // 缓存设置
        String cacheDir = this.getCacheDir().getAbsolutePath();
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        // 数据库设置
        settings.setDatabaseEnabled(true);
        if (android.os.Build.VERSION.SDK_INT < 18) {
            settings.setDatabasePath(cacheDir);
        }
        // 定位设置
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(cacheDir);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setLongClickable(false);
        webView.requestFocusFromTouch();
        // 隐藏滚动条
        webView.setHorizontalScrollBarEnabled(false);

        webView.addJavascriptInterface(new HybridInterface(getApplicationContext()), "getSomethingByJs");

        webView.setOnTouchListener(new View.OnTouchListener() {

            private float startx;
            private float starty;
            private float offsetx;
            private float offsety;
            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.getParent().requestDisallowInterceptTouchEvent(true);
//                        startx = event.getX();
//                        starty = event.getY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        offsetx = Math.abs(event.getX() - startx);
//                        offsety = Math.abs(event.getY() - starty);
//                        if (offsetx > offsety) {
//                            return true;
//                        } else {
//                            v.getParent().requestDisallowInterceptTouchEvent(false);
//                        }
//                        break;
//                    default:
//                        break;
//                }
                return false;
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TOKEN_FRESH:


                    break;
                case USERNAME_FRESH:

//                    MyDialog.showToast(MainActivity.this,SavePasswd.getInstace().get("UserName"));
                    if (username == null)
                        username = (TextView) findViewById(R.id.mydrawlayout_username);
                    if (username != null){
                        String name = SavePasswd.getInstace().get("UserName");
                        if (name != "")
                            username.setText("Hellow,"+name);
                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };


    public void showProgress(int progress){
        if (mRingProgressBar.getVisibility() == View.INVISIBLE)
            mRingProgressBar.setVisibility(View.VISIBLE);
        mRingProgressBar.setProgress(progress);
    }
    public  void hiddenProgress(){
        mRingProgressBar.setVisibility(View.INVISIBLE);
        mRingProgressBar.setProgress(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (cameraOrPhoto != null){
                cameraOrPhoto.cancel();
            }
            return;
        }

        switch (requestCode) {

            case FILE_SELECT_CODE:
                if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                    Uri uri = data.getData();
                    Uri[] uris = new Uri[]{uri};
                   /* ClipData clipData = data.getClipData();  //选择多张
                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            Uri uri = item.getUri();
                            uris[i]=uri;
                        }
                    }*/
                   if (mUploadCallbackAboveL != null)
                        mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
                    mUploadCallbackAboveL = null;
                } else {//4.4以下处理
                    Uri uri = data.getData();
                    if (mUploadMessage != null)
                        mUploadMessage.onReceiveValue(uri);
                    mUploadMessage = null;
                }
                if (cameraOrPhoto != null){
//                    cameraOrPhoto.cancel();
                }
            break;
            case Camera_SELECT_CODE:
                if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                    Uri uri = Uri.fromFile(new File(cameraPath));
                    Uri[] uris = new Uri[]{uri};
                    if (mUploadCallbackAboveL != null)
                        mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
                    mUploadCallbackAboveL = null;
                } else {//4.4以下处理
                    Uri uri = Uri.fromFile(new File(cameraPath));
                    if (mUploadMessage != null)
                        mUploadMessage.onReceiveValue(uri);
                    mUploadMessage = null;
                }
                cameraPath = null;
                if (cameraOrPhoto != null){
//                    cameraOrPhoto.cancel();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (webView != null){
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
        }
        super.onDestroy();
    }

    public void fileCallbackRelease(){
        if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(null);
            mUploadCallbackAboveL = null;
        }
        if (mUploadMessage !=null) {
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
    }


    private class MyWebChromeClient extends WebChromeClient {
        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            if (mUploadMessage != null) {
//                mUploadMessage.onReceiveValue(null);
//            }
            mUploadMessage = uploadMsg;
            showDialog();
        }
        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//            if (mUploadCallbackAboveL != null) {
//                mUploadCallbackAboveL.onReceiveValue(null);
//            }
                mUploadCallbackAboveL = filePathCallback;
            showDialog();
            return true;
        }
        // 指定源的网页内容在没有设置权限状态下尝试使用地理位置API。
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            boolean allow = true;   // 是否允许origin使用定位API
            boolean retain = false; // 内核是否记住这次制授权
            callback.invoke(origin, allow, retain);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress < 100){
                showProgress(newProgress);
            }else{
                hiddenProgress();
            }


            super.onProgressChanged(view, newProgress);
        }
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains(URL_UCAST_HOST))
                SavePasswd.getInstace().save("url",url);

            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.contains(MAIL)){
                sendMail(url.substring(MAIL.length()).trim());
                return  true;
            }else if(url.contains(TEL)){
                telPhoneNumber(url);
                return true;
            }
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            MyDialog.showToast(MainActivity.this,errorCode+ "--->>>><<");
            switch(errorCode){
                case -6:
                    break;
            }
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();    //表示等待证书响应
        }
    }

    public void showDialog(){
        if (cameraOrPhoto == null)
            cameraOrPhoto = MyDialog.createCameraDialog(this);
        cameraOrPhoto.show();
    }

    String cameraPath = null;
    public void goToCamera(){
        cameraPath = MainActivity.SAVED_CAMERA_IMAGE_DIR_PATH + System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir = new File(MainActivity.SAVED_CAMERA_IMAGE_DIR_PATH );
        if (!dir.exists()) {
            dir.mkdirs();
        } // 把文件地址转换成Uri格式
        Uri uri = Uri.fromFile(new File(cameraPath));
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, Camera_SELECT_CODE);
    }


    public void sendMail(String mail){
        String[] email = {mail}; // 需要注意，email必须以数组形式传入
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822"); // 设置邮件格式
        intent.putExtra(Intent.EXTRA_EMAIL, email); // 接收人
        intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme)); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.eamil_content)); // 正文
        startActivity(Intent.createChooser(intent, getString(R.string.email_app_select)));
    }

    public void telPhoneNumber(String uri){
        Intent intent1 = new Intent(Intent.ACTION_CALL,Uri.parse(uri));
        startActivity(intent1);
    }


    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView
            .OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            //点击NavigationView中定义的menu item时触发反应
            switch (menuItem.getItemId()) {
                case R.id.querry:
                    if (webView != null){
                        webView.loadUrl(URL_UCAST_SEARCH);
                    }
                    break;
                case R.id.account:
                    if (webView != null){
                        webView.loadUrl(URL_UCAST_ACCOUNT);
                    }
                    break;
                case R.id.setting:
                    startActivity(new Intent(MainActivity.this, MySettingActivity.class));
                    break;
            }
            //关闭DrawerLayout回到主界面选中的tab的fragment页
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

    public class HybridInterface {
        Context context;

        HybridInterface(Context context) {
            this.context = context;
        }

        //Js 回调方法，
        @JavascriptInterface
        public void getUserToken(String token){
            SavePasswd.getInstace().save("Token",token);
//            MyDialog.showToast(MainActivity.this,token);
            handler.sendEmptyMessage(TOKEN_FRESH);
        }
        @JavascriptInterface
        public void getUsername(String username){
            SavePasswd.getInstace().save("UserName",username);
//            MyDialog.showToast(MainActivity.this,username);
            handler.sendEmptyMessage(USERNAME_FRESH);
        }

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        GetUsePermision.requestPermissionsResult(this, requestCode, permissions, grantResults, mPermissionGrant);
    }

    private GetUsePermision.PermissionGrant mPermissionGrant = new GetUsePermision.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode) {
            switch (requestCode) {
                case GetUsePermision.CODE_RECORD_AUDIO:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_GET_ACCOUNTS:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_GET_ACCOUNTS", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_READ_PHONE_STATE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_CALL_PHONE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CALL_PHONE", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_CAMERA:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_CAMERA", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_ACCESS_FINE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_ACCESS_COARSE_LOCATION:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_READ_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                case GetUsePermision.CODE_WRITE_EXTERNAL_STORAGE:
                    Toast.makeText(MainActivity.this, "Result Permission Grant CODE_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
}
