package com.example.myapplication;

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
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.TimeZone;

public class HomeMessage extends Fragment implements View.OnClickListener{
    static int id=-1;
    Button btn_add;
   static  Context context;
    Button btn_scan;
    int num=0;
    MyHelper myHelper;
    LinearLayout linearLayout;
      AlarmManager manager;
      long firstTime;
     PendingIntent sender;
    public void cancel(){
        Intent intent = new Intent(context, ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        manager.cancel(sender);
    }
    public void Notice(){
        Intent intent = new Intent(context,HomeworkClock.class);
         sender = PendingIntent.getBroadcast(context, 1, intent, 0);
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
            Toast.makeText(context,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
// 计算现在时间到设定时间的时间差
        long time = selectTime - systemTime;
        firstTime += time;
// 进行闹铃注册
         manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        // pendingIntent 为发送广播
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, sender);
        } else {
            manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1000*60*60*24, sender);
        }
        Toast.makeText(context,"开启作业提醒功能",Toast.LENGTH_SHORT).show();
    }

    public void update(){
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("myhomework",null,null,null,null,null,null);
        //Toast.makeText(this,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
        if(cursor.getCount()==0)return;
        else{
            while(cursor.moveToNext()){
                Button btn=new Button(context);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=inflater.getContext();
        View view = inflater.inflate(R.layout.activity_homework, container,false);
        linearLayout=(LinearLayout)view.findViewById(R.id.HomeworkList);
        btn_add=(Button)view.findViewById(R.id.input_homework);
        btn_add.setOnClickListener(this);
        btn_scan=(Button)view.findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(this);
        //粘贴的代码
        myHelper=new MyHelper(context);
        update();
        return view;
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_scan:
                boolean isEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled();
                if (!isEnabled) {
                    //未打开通知

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

                                        intent.putExtra("app_uid",context.getApplicationInfo().uid);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setIcon(R.drawable.exit)
                                .setTitle("提示！")
                                .setMessage("确认取消作业提醒功能吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "关闭提醒功能", Toast.LENGTH_SHORT).show();
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
                Intent intent=new Intent(context,AddHomework.class);
                getActivity().finish();
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            default:
                id=view.getId();
                Intent intent1=new Intent(context,HomeworkDetail.class);
                getActivity().finish();
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        }
    }
}

