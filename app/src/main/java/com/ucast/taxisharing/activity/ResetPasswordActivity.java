package com.ucast.taxisharing.activity;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ucast.taxisharing.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

@ContentView(R.layout.activity_reset_password)
public class ResetPasswordActivity extends AppCompatActivity {

    @ViewInject(R.id.tool_bar)
    Toolbar toolbar;

    @ViewInject(R.id.reset_password_input_new_password)
    TextInputLayout new_password;
    @ViewInject(R.id.reset_password_input_new_password_again)
    TextInputLayout new_password_again;
    @ViewInject(R.id.reset_password_submit)
    Button submit;


    EditText et_new_passwpd;
    EditText et_new_passwpd_again;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initToolbar(getString(R.string.reset_password));

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

    @Event(R.id.reset_password_submit)
    private void submit(View v){

    }

}
