package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Button;

public class AutoAddClass extends AppCompatActivity {
    Button btn2;
    protected   int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,
                getResources().getDisplayMetrics());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_add_class);
btn2=(Button)findViewById(R.id.two);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);		//屏幕宽度
        int width = dm.widthPixels;
        int x=dp2px(75);
        btn2.setX(width/2-x);
    }
}
