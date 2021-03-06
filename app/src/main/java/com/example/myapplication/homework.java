package com.example.myapplication;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;


public class homework extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextMessage;
    static int id=-1;
    Button btn_scan;
    int num=0;
      AlarmManager manager;
      long firstTime;
       PendingIntent sender;
    //粘贴的代码
    //定义一个访问图片的数组
MyHelper myHelper;
LinearLayout linearLayout;
    public void cancel(){
        Intent intent = new Intent(homework.this, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(homework.this, 1, intent, 0);
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }
    public void Notice(){
        Intent intent = new Intent(homework.this,HomeworkClock.class);
         sender = PendingIntent.getBroadcast(homework.this, 1, intent, 0);
         firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            Toast.makeText(homework.this,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
// 进行闹铃注册
         manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1000*60*60*24, sender);
        }
        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
          //      firstTime,60,sender);
        Toast.makeText(this,"开启作业提醒功能",Toast.LENGTH_SHORT).show();
    }

public void update(){
    SQLiteDatabase db=myHelper.getReadableDatabase();
    Cursor cursor=db.query("myhomework",null,null,null,null,null,null);
    //Toast.makeText(this,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
    if(cursor.getCount()==0)return;
    else{
        while(cursor.moveToNext()){
            Button btn=new Button(this);
            btn.setId(new Integer(cursor.getString(0)));
            btn.setText("课程:"+cursor.getString(1)+"\n作业："+cursor.getString(2)+"\n截止日期："
            +cursor.getString(3)+"-"+cursor.getString(4)+"-"+cursor.getString(5));
LinearLayout.LayoutParams rp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400);
rp.gravity= Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL;
btn.setBackground(getResources().getDrawable(R.drawable.homework_background));
btn.setLayoutParams(rp);
btn.setOnClickListener(this);
linearLayout.addView(btn);
        }
    }
    cursor.close();
    db.close();
}
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
////                    mTextMessage.setText(R.string.timetable);
//                    Intent intent=new Intent(homework.this,MainActivity.class);
//               finish();
//                startActivity(intent);
//                    return true;
//
//                case R.id.navigation_dashboard:
//                Intent intent1=new Intent(homework.this,homework.class);
//              finish();
//                startActivity(intent1);
//                    return true;
////                case R.id.navigation_notifications:
////                    mTextMessage.setText(R.string.title_notifications);
////                    return true;
//            }
//            return false;
//        }
//    };
Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        linearLayout=(LinearLayout)findViewById(R.id.HomeworkList);
btn_add=(Button)findViewById(R.id.input_homework);
btn_add.setOnClickListener(this);
btn_scan=(Button)findViewById(R.id.btn_scan);
btn_scan.setOnClickListener(this);
        //粘贴的代码
myHelper=new MyHelper(this);
        //粘贴的代码
      //  BottomNavigationView navView = findViewById(R.id.nav_view);
       // mTextMessage = findViewById(R.id.message);
     //   navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        update();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_scan:
                boolean isEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled();
                if (!isEnabled) {
                 //未打开通知

                    AlertDialog alertDialog = new AlertDialog.Builder(this)

                            .setTitle("提示")

                            .setMessage("请在“通知”中打开通知权限")

                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();

                                }

                            })

                            .setPositiveButton("去设置", new DialogInterface.OnClickListener() {

                                @Override

                                public void onClick(DialogInterface dialog, int which) {
                                    //      Enabled=true;
                                    dialog.cancel();

                                    Intent intent = new Intent();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                                        intent.putExtra("android.provider.extra.APP_PACKAGE", homework.this.getPackageName());

                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0

                                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                                        intent.putExtra("app_package",homework.this.getPackageName());

                                        intent.putExtra("app_uid", homework.this.getApplicationInfo().uid);

                                        startActivity(intent);

                                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4

                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                                        intent.addCategory(Intent.CATEGORY_DEFAULT);

                                        intent.setData(Uri.parse("package:" +homework.this.getPackageName()));

                                    } else if (Build.VERSION.SDK_INT >= 15) {

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");

                                        intent.setData(Uri.fromParts("package", homework.this.getPackageName(), null));

                                    }

                                    startActivity(intent);



                                }

                            })

                            .create();

                    alertDialog.show();

                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                }

                else {
                    SQLiteDatabase db = myHelper.getReadableDatabase();
                    Cursor cursor = db.query("homeworkclock", null, null, null, null, null, null);
                    if (cursor.getCount() == 0) num = 0;
                    else {
                        cursor.moveToNext();
                        num = new Integer(cursor.getString(1));
                    }
                    cursor.close();
                    db.close();
                    ++num;
                    if (num % 2 != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(homework.this);
                        builder.setIcon(R.drawable.exit)
                                .setTitle("提示！")
                                .setMessage("确认开启作业提醒功能吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Notice();
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("homeworkclock",null,null);
                                        db1.insert("homeworkclock",null,values);
                                        db1.close();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        --num;
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("homeworkclock",null,null);
                                        db1.insert("homeworkclock",null,values);
                                        db1.close();
                                    }
                                })
                                .show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setIcon(R.drawable.exit)
                                .setTitle("提示！")
                                .setMessage("确认取消作业信息提醒功能吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(homework.this, "关闭提醒功能", Toast.LENGTH_SHORT).show();
                                        cancel();
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("homeworkclock",null,null);
                                        db1.insert("homeworkclock",null,values);
                                        db1.close();
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        --num;
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("homeworkclock",null,null);
                                        db1.insert("homeworkclock",null,values);
                                        db1.close();
                                    }
                                })
                                .show();
                    }

                }

                break;
            case R.id.input_homework:
                Intent intent=new Intent(this,AddHomework.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
                default:
                    id=view.getId();
                    Intent intent1=new Intent(this,HomeworkDetail.class);
                    finish();
                    startActivity(intent1);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        }
    }
    private boolean mIsExit;

    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
