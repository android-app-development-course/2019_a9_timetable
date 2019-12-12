package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
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
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.explosionfield.ExplosionField;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mTextMessage;
    static  String week[],color[],time[];
    int[]width;
    Button autoAdd;
    static Button b;
    static int id=-1;
    Button temp;
    Button fun;
    int num;

    private ExplosionField mExplosionField;
    boolean Enabled;//通知权限是否打开
    static RelativeLayout relativeLayout;
    static int aveWidth,aveHeight;
    static MyHelper myHelper;
    static Context context;
    static Activity activity;
    FragmentManager  fragmentManager;
    FragmentTransaction transaction;
    //static String project;
//static String start;

    public void cancel(){
        Intent intent = new Intent(MainActivity.this, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }
    public void Notice(){
        Intent intent = new Intent(MainActivity.this, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            Toast.makeText(MainActivity.this,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
// 进行闹铃注册
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1000*60*60, sender);
        }
        //manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
          //      firstTime,60,sender);
        Toast.makeText(this,"开启课程提醒功能",Toast.LENGTH_SHORT).show();
    }

    public  void update(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("information",null,null,null,null,null,null);
        if(cursor.getCount()==0)return;
        int num=1;
        while(cursor.moveToNext()){

            int start=new Integer(cursor.getString(4));
            int ends=new Integer(cursor.getString(5));

            int week1=-1;
            for(int i=0;i<week.length;++i) {
                if(week[i].equals(cursor.getString(3))){
                    // Toast.makeText(this,"as",Toast.LENGTH_SHORT).show();
                    week1=i+1;break;
                }
            }
            RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                    aveWidth, aveHeight*(ends-start+1));
            rp.addRule(RelativeLayout.RIGHT_OF, week1+8*(start-1));
            rp.addRule(RelativeLayout.ALIGN_TOP, week1+8*(start-1));
            Button btn2=new Button(context);
            btn2.setLayoutParams(rp);
            btn2.setId((new Integer(cursor.getString(0)))+1000);
            btn2.setBackground(context.getResources().getDrawable(R.drawable.btn_background));
           btn2.setTextColor(Color.parseColor("#000000"));
            btn2.setText(cursor.getString(1)+"\n"+cursor.getString(6));
            relativeLayout.addView(btn2);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      id=view.getId();
                    Intent intent1=new Intent(context,ClassDetail.class);
                    finish();
                    startActivity(intent1);
                }
            });
         //   ++num;
        }
        cursor.close();
        db.close();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ClassMessage classMessage=new ClassMessage();
                     fragmentManager = getSupportFragmentManager();
                     transaction = fragmentManager.beginTransaction();
                   // 开启一个事务
                    transaction.replace(R.id.message, classMessage);
                    transaction.commit();

//                  mTextMessage.setText(R.string.timetable);
                  //Intent intent=new Intent(MainActivity.this,MainActivity.class);
                  //finish();
                    //startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    HomeMessage homeMessage=new HomeMessage();
                     fragmentManager = getSupportFragmentManager();
                transaction = fragmentManager.beginTransaction();// 开启一个事务
                    transaction.replace(R.id.message, homeMessage);
                     transaction.commit();

//                    Intent intent1=new Intent(MainActivity.this,homework.class);
//                   finish();
//                    startActivity(intent1);
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
        setContentView(R.layout.activity_main);
        activity=MainActivity.activity;
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mExplosionField = ExplosionField.attach2Window(this);
        navView.setSelectedItemId(R.id.navigation_home);

//        ClassMessage classMessage=new ClassMessage();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();	// 开启一个事务
//        transaction.replace(R.id.message, classMessage);
//        transaction.commit();
    }
    public static void init(){
        //    RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.week1);
        for(int i = 1; i <= 12; i ++){
            for(int j = 1; j <= 8; j ++){
                Button tx = new Button(context);
                tx.setId((i - 1) * 8  + j);
                if(j==1) {
                    tx.setBackground(context.getResources().getDrawable(R.drawable.setbar_bg_opacity));
                    tx.setText(time[i - 1]);
                }else
                tx.setBackgroundColor(Color.parseColor("#00000000"));
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth ,aveHeight);
                tx.setGravity(Gravity.CENTER);				//字体样式

                //如果是第一列，需要设置课的序号（1 到 12）
                if(j == 1)				{
                    //设置他们的相对位置
                    if(i == 1)
                        rp.addRule(RelativeLayout.BELOW, R.id.title);
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);				}
                else				{
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
                    tx.setText("");				}
                tx.setLayoutParams(rp);
                tx.setOnClickListener((View.OnClickListener) context);
                relativeLayout.addView(tx);			}		}
    }
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add:
                Intent intent=new Intent(MainActivity.this,AutoAddClass.class);
                //finish();   //主界面不用结束
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                break;
            case R.id.btn_scan:
                 Enabled=true;
                // num= readFile();
                //     Toast.makeText(this,"read  "+num,Toast.LENGTH_SHORT).show();
                boolean isEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled();
                if (!isEnabled) {
                   Enabled=false;
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

                                        intent.putExtra("android.provider.extra.APP_PACKAGE", MainActivity.this.getPackageName());

                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0

                                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");

                                        intent.putExtra("app_package",MainActivity.this.getPackageName());

                                        intent.putExtra("app_uid", MainActivity.this.getApplicationInfo().uid);

                                        startActivity(intent);

                                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4

                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                                        intent.addCategory(Intent.CATEGORY_DEFAULT);

                                        intent.setData(Uri.parse("package:" +MainActivity.this.getPackageName()));

                                    } else if (Build.VERSION.SDK_INT >= 15) {

                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");

                                        intent.setData(Uri.fromParts("package", MainActivity.this.getPackageName(), null));

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
                    Cursor cursor = db.query("classclock", null, null, null, null, null, null);
                    if (cursor.getCount() == 0) num = 0;
                    else {
                        cursor.moveToNext();
                        num = new Integer(cursor.getString(1));
                    }
                    cursor.close();
                    db.close();
                    ++num;
                    if (num % 2 != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.exit)
                                .setTitle("提示！")
                                .setMessage("确认开启上课信息提醒功能吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Notice();
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("classclock",null,null);
                                        db1.insert("classclock",null,values);
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
                                        db1.delete("classclock",null,null);
                                        db1.insert("classclock",null,values);
                                        db1.close();
                                    }
                                })
                                .show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.exit)
                                .setTitle("提示！")
                                .setMessage("确认取消上课信息提醒功能吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "关闭提醒功能", Toast.LENGTH_SHORT).show();
                                        cancel();
                                        SQLiteDatabase db1=myHelper.getWritableDatabase();
                                        ContentValues values=new ContentValues();
                                        values.put("num",num+"");
                                        db1.delete("classclock",null,null);
                                        db1.insert("classclock",null,values);
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
                                        db1.delete("classclock",null,null);
                                        db1.insert("classclock",null,values);
                                        db1.close();
                                    }
                                })
                                .show();
                    }

                }
                break;
            default:
                b = (Button) findViewById(view.getId());
                if((b.getId()-1)%8!=0) {
                    //   Toast.makeText(this,b.getId()+"",Toast.LENGTH_SHORT).show();
                    if (temp != null && temp != b) {
                        temp.setBackgroundColor(Color.parseColor("#00000000"));
                        b.setBackground(getResources().getDrawable(R.drawable.c));
                        // b.setBackgroundColor(Color.BLUE);
                    }
                    if (temp == null)
                        // b.setBackgroundColor(Color.BLUE);
                        b.setBackground(getResources().getDrawable(R.drawable.c));
                    if (temp == b) {
                        mExplosionField.explode(b);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(MainActivity.this, AddNewClass.class);
                              finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            }
                        }, 800);
                    }
                    temp = b;
                }
                break;
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

                MainActivity.this.finish();
//System.exit(0);
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
