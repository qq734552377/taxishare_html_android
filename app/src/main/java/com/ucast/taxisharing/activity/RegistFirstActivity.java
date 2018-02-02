package com.ucast.taxisharing.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ucast.taxisharing.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_regist_first)
public class RegistFirstActivity extends AppCompatActivity {
    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;


    EditText et_regist_email;
    EditText et_regist_firstname;
    EditText et_regist_lastname;
    EditText et_regist_nickname;
    EditText et_regist_password;
    EditText et_regist_reiuput_password;
    EditText et_regist_idcard;
    EditText et_regist_idcard_again;


    @ViewInject(R.id.regist_email)
    TextInputLayout til_regist_email;
    @ViewInject(R.id.regist_firstname)
    TextInputLayout til_regist_firstname;
    @ViewInject(R.id.regist_lastname)
    TextInputLayout til_regist_lastname;
    @ViewInject(R.id.regist_nickname)
    TextInputLayout til_regist_nickname;
    @ViewInject(R.id.regist_password)
    TextInputLayout til_regist_password;
    @ViewInject(R.id.regist_reiuput_password)
    TextInputLayout til_regist_reiuput_password;
    @ViewInject(R.id.regist_idcard)
    TextInputLayout til_regist_idcard;
    @ViewInject(R.id.regist_idcard_again)
    TextInputLayout til_regist_idcard_again;


    @ViewInject(R.id.regist_sex)
    RadioGroup rg_regist_sex;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initToolbar(getString(R.string.sign_in));

        init();
    }

    private void init() {
        et_regist_email= til_regist_email.getEditText();
        et_regist_firstname= til_regist_email.getEditText();
        et_regist_lastname= til_regist_email.getEditText();
        et_regist_nickname= til_regist_email.getEditText();
        et_regist_password= til_regist_email.getEditText();
        et_regist_reiuput_password= til_regist_email.getEditText();
        et_regist_idcard= til_regist_email.getEditText();
        et_regist_idcard_again= til_regist_idcard_again.getEditText();

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


    @Event(R.id.regist_next)
    private void goto_secondRegist(View v){
        String email=et_regist_email.getText().toString().trim();
        String firstname=et_regist_firstname.getText().toString().trim();
        String lastname=et_regist_lastname.getText().toString().trim();
        String nickname=et_regist_nickname.getText().toString().trim();
        String password=et_regist_password.getText().toString().trim();
        String reiuput_password=et_regist_reiuput_password.getText().toString().trim();
        String idcard=et_regist_idcard.getText().toString().trim();
        String idcard_again=et_regist_idcard_again.getText().toString().trim();
        String sex="";

        for (int i = 0; i <rg_regist_sex.getChildCount() ; i++) {
            RadioButton child= (RadioButton) rg_regist_sex.getChildAt(i);
            if (child.isChecked()){
                sex=child.getText().toString().trim();
            }
        }










        startActivity(new Intent(RegistFirstActivity.this, RegistSecondActivity.class));

    }
}
