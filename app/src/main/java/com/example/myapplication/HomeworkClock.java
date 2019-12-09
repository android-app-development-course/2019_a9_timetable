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

public class HomeworkClock extends BroadcastReceiver {
    int year,month,day,hour,minute;
    String week;
    int Surplus;
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
        //重复定时任务
        //HomeMessage.firstTime+=1000*60*60;
        Intent intent1 = new Intent(context,HomeworkClock.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent1, 0);
        AlarmManager manager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*60*60*24, sender);
                }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000*60*60*24, sender);
                }
        String message=null;
        Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        getDate();
int dayt[];
        int day1[]={31,28,31,30,31,30,31,31,30,31,30,31};
        int day2[]={31,29,31,30,31,30,31,31,30,31,30,31};
      SQLiteDatabase db=new MyHelper(context).getReadableDatabase();
      if(year%4==0&&year%100!=0||year%400==0)dayt=day2;
      else dayt=day1;
        Cursor cursor=db.query("myhomework",null,null,null,null,null,null);
        if(cursor.getCount()==0)return;
while(cursor.moveToNext()){
    if(new Integer(cursor.getString(3))==year&&(new Integer(cursor.getString(4))>=month))
    {
if(new Integer(cursor.getString(4))>month)
    Surplus=dayt[new Integer(cursor.getString(4))-2]+new Integer(cursor.getString(5))-day+1;

else   Surplus=new Integer(cursor.getString(5))-day+1;
if(Surplus<=3)
{
    message="课程:"+cursor.getString(1)+" \n作业:"+cursor.getString(2)+"\n剩余天数:"+Surplus;
    break;
}
    }
   else if(((new Integer(cursor.getString(3)))-year==1)&&month==12&&(new Integer(cursor.getString(4))==1))
    {
Surplus=31-day+new Integer(cursor.getString(5))+1;
        if(Surplus<=3)
        {
            message="课程:"+cursor.getString(1)+" \n作业:"+cursor.getString(2)+"\n剩余天数:"+Surplus;
            break;
        }
    }
   else continue;
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
                        .setContentTitle(message)
                        .setContentText(message)
                        .setSmallIcon(R.mipmap.ic_launcher).build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, id)
                        .setContentTitle("作业到期提醒")
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
