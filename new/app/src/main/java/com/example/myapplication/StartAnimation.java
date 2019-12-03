package com.example.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

//import androidx.appcompat.app.AppCompatActivity;

public class StartAnimation extends AppCompatActivity implements View.OnClickListener {
    int width;
    int height;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                btn.setText(String.format("跳过:%ds",millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(StartAnimation.this,MainActivity.class);
                startActivity(intent);
            }
        }.start();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);		//屏幕宽度
        width=dm.widthPixels;
        height=dm.heightPixels;
        findViewById(R.id.loading3).setX(width/2-250);
        findViewById(R.id.loading3).setY(height/2-650);
        findViewById(R.id.loading2).setVisibility(View.INVISIBLE);
        findViewById(R.id.loading3).setVisibility(View.INVISIBLE);
        findViewById(R.id.loadingtitle).setVisibility(View.INVISIBLE);
        findViewById(R.id.loading1).setX(width/2-250);
        findViewById(R.id.loading1).setY(height/2-200);
        findViewById(R.id.loadingtitle).setY(height/2-200);
        ObjectAnimator translation = ObjectAnimator.ofFloat(findViewById(R.id.loading1),"TranslationY",height/2-450,height/2-650);
        translation.setDuration(2000);
        translation.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loading2).setVisibility(View.VISIBLE);
                ObjectAnimator translationY = ObjectAnimator.ofFloat(findViewById(R.id.loading2),"TranslationY",height/2-1090,height/2-850);
                translationY.setDuration(2000);
                // translationY.start();
                ObjectAnimator translationX = ObjectAnimator.ofFloat(findViewById(R.id.loading2),"TranslationX",width/2-370,width/2-230,width/2-340);
                translationX.setDuration(700);
                ObjectAnimator translationX1 = ObjectAnimator.ofFloat(findViewById(R.id.loading2),"TranslationX",width/2-340,width/2-280,width/2-330);
                translationX1.setDuration(500);
                ObjectAnimator translationX2 = ObjectAnimator.ofFloat(findViewById(R.id.loading2),"TranslationX",width/2-330,width/2-290,width/2-330);
                translationX2.setDuration(500);
                AnimatorSet animatorSet1=new AnimatorSet();
                animatorSet1.playSequentially(translationX,translationX1,translationX2);
                AnimatorSet animatorSet=new AnimatorSet();
                animatorSet.playTogether(translationY,animatorSet1);
                animatorSet.start();
                //  translationX.start();
            }
        },1700);
        Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.loading3).setVisibility(View.VISIBLE);
                findViewById(R.id.loading2).setVisibility(View.VISIBLE);
                findViewById(R.id.loadingtitle).setVisibility(View.VISIBLE);
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                    Intent intent=new Intent(StartAnimation.this,MainActivity.class);
//                    startActivity(intent);
//                    }
//                },1000);
            }
        },3600);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
