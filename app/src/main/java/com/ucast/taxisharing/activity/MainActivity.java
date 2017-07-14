package com.ucast.taxisharing.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ucast.taxisharing.R;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {
    //DrawerLayout中的左侧菜单控件
    private NavigationView mNavigationView;
    //DrawerLayout控件
    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;


    RingProgressBar mRingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar("主界面");

        mRingProgressBar = (RingProgressBar) findViewById(R.id.progress_bar);


        mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

            @Override
            public void progressToComplete() {
                // 进度达到最大值时回调 默认max进度值为100
                Toast.makeText(MainActivity.this, "完成", Toast.LENGTH_SHORT).show();
                mRingProgressBar.setVisibility(View.GONE);
            }
        });


        new Thread(new Runnable() {
            int pro = 0;

            @Override
            public void run() {

                while (pro <= 100) {
                    handler.sendEmptyMessage(pro++);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            mRingProgressBar.setProgress(msg.what);

            super.handleMessage(msg);
        }
    };


    private void initToolbar(String title) {

        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);

        toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setOnCreateContextMenuListener(this);

        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        mNavigationView.setNavigationItemSelectedListener(naviListener);
        mNavigationView.setBackgroundColor(getResources().getColor(R.color.nav_background_color));

    }

    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView
            .OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            //点击NavigationView中定义的menu item时触发反应
            switch (menuItem.getItemId()) {
                case R.id.querry:
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    break;
                case R.id.add:

                    break;
                case R.id.setting:

                    break;
            }
            //关闭DrawerLayout回到主界面选中的tab的fragment页
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

}
