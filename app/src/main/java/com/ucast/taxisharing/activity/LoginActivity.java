package com.ucast.taxisharing.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ucast.taxisharing.R;
import com.ucast.taxisharing.tools.MyDialog;
import com.ucast.taxisharing.tools.MyHttpSucessCallback;
import com.ucast.taxisharing.tools.MyXHttpRequest;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import static com.ucast.taxisharing.R.string.email;


@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements MyHttpSucessCallback{
    @ViewInject(R.id.login_email)
    TextInputLayout email;
    @ViewInject(R.id.login_password)
    TextInputLayout password;
    @ViewInject(R.id.login_password_see)
    ImageButton isseeBt;
    @ViewInject(R.id.login_email_sign_in_button)
    ImageButton login_bt;

    EditText et_email;
    EditText et_password;

    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;
    private static final String TAG = "LoginActivity";
    String email_format="[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        et_email = email.getEditText();
        et_password = password.getEditText();

        initToolbar(getString(R.string.login));


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

    @Event(R.id.login_password_see)
    private void canSeePassword(View v) {
      if( et_password.getInputType()== 129){
            isseeBt.setImageResource(R.mipmap.see_open);
            et_password.setInputType(InputType.TYPE_CLASS_TEXT);
      }else{
          isseeBt.setImageResource(R.mipmap.see_close);
          et_password.setInputType(129);
      }
    }


    @Event(R.id.login_email_sign_in_button)
    private void sign_in(View v) {


        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();


        String url = "http://192.168.0.56:12907/api/Login";

        Map<String,String> params=new HashMap<>();
        params.put("Email",email);
        params.put("Password",password);

        MyXHttpRequest.postParamsRequest(this,url,params,this);


    }


    @Event(R.id.login_forget_password)
    private void goto_forget_activity(View v) {
        startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
    }

    @Event(R.id.login_sign_in)
    private void goto_regist_activity(View v) {
        startActivity(new Intent(LoginActivity.this, RegistFirstActivity.class));
    }
    @Override
    public void sucess(String data) {

    }
}
