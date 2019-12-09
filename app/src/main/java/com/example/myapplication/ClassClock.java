package com.example.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

import static android.content.Context.NOTIFICATION_SERVICE;

//import android.support.v4.app.NotificationCompat;

//import androidx.core.app.NotificationCompat;

public class ClassClock extends BroadcastReceiver {
    int year,month,day,hour,minute;
    String week;
    int starthour[],startminute[];
    public void getDate(){
        Calendar calendar = Calendar.getInstance();

//获取系统的日期
//年
         year = calendar.get(Calendar.YEAR);
//月
         month = calendar.get(Calendar.MONTH)+1;
//日
         day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
         hour = calendar.get(Calendar.HOUR_OF_DAY);
//分钟
         minute = calendar.get(Calendar.MINUTE);

    int i=calendar.get(Calendar.DAY_OF_WEEK);
    switch(i){
        case Calendar.SUNDAY:week="周日";break;
        case Calendar.MONDAY:
            week="周一";break;
        case Calendar.TUESDAY:
            week="周二";break;
        case Calendar.WEDNESDAY:
            week="周三";break;
        case Calendar.THURSDAY:
            week= "周四";break;
        case Calendar.FRIDAY:
            week= "周五";break;
        case Calendar.SATURDAY:
            week= "周六";break;
    }
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent intent1 = new Intent(context,ClassClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent1, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*60*60, sender);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*60*60, sender);
        }
        String message=null;
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);

        starthour=new int[]{8,9,10,11,14,15,16,17,19,19,20,21};
        startminute=new int[]{30,20,20,10,30,20,10,0,0,50,40,30};
getDate();

        SQLiteDatabase db=new MyHelper(context).getReadableDatabase();

        Cursor cursor=db.query("information",null,null,null,null,null,null);
        if(cursor.getCount()==0)return;
        int num=1;
        while(cursor.moveToNext()){
  //          Toast.makeText(context,cursor.getString(3),Toast.LENGTH_SHORT).show();
if(!week.equals(cursor.getString(3)))continue;
else{
    //Toast.makeText(context,(starthour[new Integer(cursor.getString(4))-1]*60+startminute[new Integer(cursor.getString(4))-1])-(hour*60+minute)+"",Toast.LENGTH_SHORT).show();
if(((starthour[new Integer(cursor.getString(4))-1]*60+startminute[new Integer(cursor.getString(4))-1])-(hour*60+minute)>=0)&&(starthour[new Integer(cursor.getString(4))-1]*60+startminute[new Integer(cursor.getString(4))-1])-(hour*60+minute)<=51) {
    message = cursor.getString(1) + "\n" + cursor.getString(6);break;
}
}
        }
if(message!=null) {
    String id = "my_channel_01";
    String name = "我是渠道名字";
    NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    Notification notification = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);

        notificationManager.createNotificationChannel(mChannel);
        notification = new Notification.Builder(context, id)
                .setChannelId(id)
                .setContentTitle("课程信息提醒")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher).build();
    } else {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, id)
                .setContentTitle("课程信息提醒")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setChannelId(id);//无效
        notification = notificationBuilder.build();
    }
    vibrator.vibrate( 2500);//比较常用

    notificationManager.notify(111123, notification);
}

   }
}
