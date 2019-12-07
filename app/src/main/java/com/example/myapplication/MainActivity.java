package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.explosionfield.ExplosionField;

import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextMessage;
    static  String week[],color[],time[];
    int[]width;
    Button autoAdd;
    static Button b;
    Button temp;
    Button fun;
    int num;
    //   static Button newbtn;
    private ExplosionField mExplosionField;

    static RelativeLayout relativeLayout;
    static int aveWidth,aveHeight;
    static MyHelper myHelper;
    static Context context;
    //static String project;
//static String start;
    public void cancel(){
        Intent intent = new Intent(MainActivity.this, MyReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        manager.cancel(sender);
    }
    public void Notice(){
        Intent intent = new Intent(MainActivity.this, MyReceiver.class);
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
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                firstTime,1000*60*60,sender);
        Toast.makeText(this,"开启课程提醒功能",Toast.LENGTH_SHORT).show();
    }

    public static void update(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("information",null,null,null,null,null,null);
        if(cursor.getCount()==0)return;
        int num=1;
        while(cursor.moveToNext()){
            // project.concat(" "+cursor.getString(1));
            // start+=" "+cursor.getString(4);
            if(num==8)num=1;
            //Toast.makeText(this,""+cursor.getCount(),Toast.LENGTH_SHORT).show();
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
            //  Toast.makeText(this,start+" "+ends+" "+(start-ends+1)+" ",Toast.LENGTH_SHORT).show();
            rp.addRule(RelativeLayout.RIGHT_OF, week1+8*(start-1));
            rp.addRule(RelativeLayout.ALIGN_TOP, week1+8*(start-1));
            Button btn2=new Button(context);
            btn2.setLayoutParams(rp);
            btn2.setBackground(context.getResources().getDrawable(R.drawable.btn_background));

            btn2.setTextColor(Color.parseColor("#000000"));
            btn2.setText(cursor.getString(1).substring(cursor.getString(1).indexOf(":")+1)+"\n"+cursor.getString(6).substring(cursor.getString(6).indexOf(":")+1));
            relativeLayout.addView(btn2);
            ++num;
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
//                    mTextMessage.setText(R.string.timetable);
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_dashboard:
                    Intent intent1=new Intent(MainActivity.this,homework.class);
                    startActivity(intent1);
                    break;
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
        setContentView(R.layout.activity_timetable);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mExplosionField = ExplosionField.attach2Window(this);
autoAdd=(Button)findViewById(R.id.btn_add);
autoAdd.setOnClickListener(this);
        //addListener(findViewById(R.id.week1));
//        loading l=new loading();
//        l.show(getFragmentManager(),"3");
        context=MainActivity.this;
        temp=null;
        num=0;
        fun=(Button)findViewById(R.id.btn_scan);
        fun.setOnClickListener(this);
        myHelper=new MyHelper(this);
        color=new String[]{"#ADADAD","#FF0000","#BE77FF","#80FFFF","#79FF79","#FF5809","#7AFEC6","FFFF6F"};
        week=new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        time=new String[]{"1\n08:30\n09:10","2\n09:20\n10:00","3\n10:20\n11:00","4\n11:10\n11:50"
                ,"5\n14:30\n15:10","6\n15:20\n16:00","7\n16:10\n16:50","8\n17:00\n17:40","9\n19:00\n19:40"
                ,"10\n19:50\n20:30","11\n20:40\n21:20","12"};
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);		//屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        aveWidth = width / 8;
        int height=dm.heightPixels;
        aveHeight=height/10;
        relativeLayout=(RelativeLayout)findViewById(R.id.week1);
        init();
        update();
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
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                break;
            case R.id.btn_scan:
                // num= readFile();
                //     Toast.makeText(this,"read  "+num,Toast.LENGTH_SHORT).show();
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
                ++num;
                //writeFile();
                if(num%2!=0){
                    //   Toast.makeText(this,"开启提醒功能",Toast.LENGTH_SHORT).show();
                    Notice();
                }
                else{
                    Toast.makeText(this,"关闭提醒功能",Toast.LENGTH_SHORT).show();
                    cancel();
                }

                // Toast.makeText(this,"dfff",Toast.LENGTH_SHORT).show();
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
}
