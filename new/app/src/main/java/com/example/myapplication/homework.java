package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;


public class homework extends AppCompatActivity {
    private TextView mTextMessage;

    //粘贴的代码
    //定义一个访问图片的数组

    int [] images = new int []{
            R.drawable.p6,
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p1,
    };
    private ViewPager vp;
    private ArrayList<String> urls = new ArrayList<>();
    private LoopVPAdapter loopVPAdapter;

//粘贴的代码

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.timetable);
                    Intent intent=new Intent(homework.this,MainActivity.class);
                startActivity(intent);
                    return true;

                case R.id.navigation_dashboard:
                Intent intent1=new Intent(homework.this,homework.class);
                startActivity(intent1);
                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);

        //粘贴的代码
        urls.add("http://seopic.699pic.com/photo/00005/5186.jpg_wh1200.jpg");
        urls.add("http://seopic.699pic.com/photo/50010/0719.jpg_wh1200.jpg");
        urls.add("http://seopic.699pic.com/photo/50009/9449.jpg_wh1200.jpg");
        urls.add("http://seopic.699pic.com/photo/50002/5923.jpg_wh1200.jpg");
        urls.add("http://seopic.699pic.com/photo/50001/9330.jpg_wh1200.jpg");
        urls.add("http://seopic.699pic.com/photo/50009/9191.jpg_wh1200.jpg");
        vp = (ViewPager) findViewById(R.id.vp);
        loopVPAdapter = new ImgAdapter(this,urls,vp);

        //粘贴的代码
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    //粘贴的代码



    //粘贴的代码
}