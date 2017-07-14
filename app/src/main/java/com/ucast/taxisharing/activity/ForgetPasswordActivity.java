package com.ucast.taxisharing.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.ucast.taxisharing.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;



@ContentView(R.layout.activity_forget_password)
public class ForgetPasswordActivity extends AppCompatActivity {
    @ViewInject(R.id.forget_password_phone_number)
    TextInputLayout phonenumber;
    @ViewInject(R.id.forget_password_get_verify)
    Button get_verify;
    @ViewInject(R.id.forget_password_verify)
    TextInputLayout verify;
    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar(getString(R.string.get_back_password));
    }


    @Event(R.id.forget_password_next)
    private void next(View v){
        startActivity(new Intent(ForgetPasswordActivity.this,ResetPasswordActivity.class));
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

}
