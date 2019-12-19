package com.example.myapplication;

import android.annotation.SuppressLint;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.explosionfield.ExplosionField;

import java.util.Calendar;
import java.util.TimeZone;

public class ClassMessage extends Fragment implements View.OnClickListener{
    static  String week[],color[],time[];
    int[]width;
    Button autoAdd;
    static Button b;
    static int id=-1;
    Button temp;
    Button fun;
    int num;
    //   static Button newbtn;
    private ExplosionField mExplosionField;
    boolean Enabled;//通知权限是否打开
    static RelativeLayout relativeLayout;
    static int aveWidth,aveHeight;
    static MyHelper myHelper;
    static Context context;
    public void cancel(){
        Intent intent = new Intent(context, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        manager.cancel(sender);
    }
    public void Notice(){
        Intent intent = new Intent(context, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        long firstTime = SystemClock.elapsedRealtime(); // 开机之后到现在的运行时间(包括睡眠时间)
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.MINUTE,40);
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
        long selectTime = calendar.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {
            //Toast.makeText(context,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
// 进行闹铃注册
        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1000*60*60, sender);
        }
      //  manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,

        //       firstTime,60,sender);
    }

    public  void update(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("information",null,null,null,null,null,null);
      // Toast.makeText(context,cursor.getCount()+"",Toast.LENGTH_LONG).show();
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
                    getActivity().finish();
                    startActivity(intent1);
                    getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                }
            });

        }
        cursor.close();
        db.close();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_timetable, container,false);
        mExplosionField = ExplosionField.attach2Window(getActivity());
autoAdd=(Button)view.findViewById(R.id.btn_add);
autoAdd.setOnClickListener(this);

        context=inflater.getContext();
        temp=null;
       num=0;
        fun=(Button)view.findViewById(R.id.btn_scan);
        fun.setOnClickListener(this);
        myHelper=new MyHelper(context);
       color=new String[]{"#ADADAD","#FF0000","#BE77FF","#80FFFF","#79FF79","#FF5809","#7AFEC6","FFFF6F"};
       week=new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        time=new String[]{"1\n08:30\n09:10","2\n09:20\n10:00","3\n10:20\n11:00","4\n11:10\n11:50"
                ,"5\n14:30\n15:10","6\n15:20\n16:00","7\n16:10\n16:50","8\n17:00\n17:40","9\n19:00\n19:40"
                ,"10\n19:50\n20:30","11\n20:40\n21:20","12"};
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);		//屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
       aveWidth = width / 8;
       int height=dm.heightPixels;
        aveHeight=height/10;
        relativeLayout=(RelativeLayout)view.findViewById(R.id.week1);
        init();
        update();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.query("classclock", null, null, null, null, null, null);
        if (cursor.getCount() == 0) num = 0;
        else {
            cursor.moveToNext();
            num = new Integer(cursor.getString(1));
        }
        cursor.close();
        db.close();
        if(num%2!=0)
            Notice();
        return view;
    }
    public static void init(){
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
                Intent intent=new Intent(context,AutoAddClass.class);
                getActivity().finish();
                startActivity(intent);
                 getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                break;
            case R.id.btn_scan:

                boolean isEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
                if (!isEnabled) {
                    Toast.makeText(context,"打开通知功能失败，请先打开权限",Toast.LENGTH_SHORT).show();
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
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
                                        intent.putExtra("android.provider.extra.APP_PACKAGE", context.getPackageName());
                                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {  //5.0
                                        intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                                        intent.putExtra("app_package",context.getPackageName());
                                        intent.putExtra("app_uid", context.getApplicationInfo().uid);
                                        startActivity(intent);
                                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {  //4.4
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setData(Uri.parse("package:" +context.getPackageName()));
                                    } else if (Build.VERSION.SDK_INT >= 15) {
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
                                    }
                                    getActivity().finish();
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
                                        Toast.makeText(context,"开启课程提醒功能",Toast.LENGTH_SHORT).show();
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
                b = (Button) view.findViewById(view.getId());
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
                                Intent intent = new Intent(context, AddNewClass.class);
                                getActivity().finish();
                             startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                            }
                        }, 800);
//                        Handler handler1 = new Handler();
//                        handler1.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                       MainActivity.guanbi();
//                            }
//                        }, 800);
                    }
                    temp = b;
                }
                break;
        }
    }
}
