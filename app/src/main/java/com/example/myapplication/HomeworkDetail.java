package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeworkDetail extends AppCompatActivity implements View.OnClickListener {
MyHelper myHelper;
Button delete,btn_scan,edit_homework;
static TextView classname,jobname,deadline,content;
static boolean edit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        edit=true;
        edit_homework=(Button)findViewById(R.id.edit_homework);
        btn_scan=(Button)findViewById(R.id.btn_scan);
        edit_homework.setOnClickListener(this);
        btn_scan.setOnClickListener(this);
        myHelper=new MyHelper(this);
        classname=(TextView)findViewById(R.id.classname);
        jobname=(TextView)findViewById(R.id.jobname);
        deadline=(TextView)findViewById(R.id.deadline);
        content=(TextView)findViewById(R.id.content);
        delete =(Button)findViewById(R.id.delete);
        delete.setOnClickListener(this);
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("myhomework",null,null,null,null,null,null);
        //Toast.makeText(this,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
//        if(cursor.getCount()==0)return;
            while(cursor.moveToNext()){
                if(new Integer(cursor.getString(0))==HomeMessage.id){
                    classname.setText(cursor.getString(1));
                    jobname.setText(jobname.getText().toString()+cursor.getString(2));
                    deadline.setText(deadline.getText().toString()+
                            cursor.getString(3)+"-"+
                            cursor.getString(4)+"-"+cursor.getString(5));
                    content.setText(cursor.getString(6));
                    break;
                }
            }
        cursor.close();
        db.close();

    }
    @Override
    public void onClick(View view) {
     switch(view.getId()){
         case R.id.delete:
             edit=false;
             SQLiteDatabase db=myHelper.getReadableDatabase();
             db.execSQL("DELETE FROM myhomework WHERE _id = '"+HomeMessage.id+"'"+";");
             Intent intent=new Intent(HomeworkDetail.this,MainActivity.class);
             finish();
             startActivity(intent);
             overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
             break;
         case R.id.btn_scan:
             edit=false;
             Intent intent1=new Intent(this,MainActivity.class);
             finish();
             startActivity(intent1);
             overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
             break;
         case R.id.edit_homework:
             Intent intent2=new Intent(this,AddHomework.class);
             finish();
             startActivity(intent2);
             overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
     }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        edit=false;
        Intent intent1=new Intent(this,MainActivity.class);
        finish();
        startActivity(intent1);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

        return true;
    }
    }
