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

public class ClassDetail extends AppCompatActivity implements View.OnClickListener {
public static TextView classname;
    public static TextView classroom;
    public static TextView week;
    public static TextView class_time;
    public static TextView teacher;
public static boolean edit=false;
Button btn_scan,edit_class,delete;
MyHelper myHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);
        myHelper=new MyHelper(this);
        edit=true;
        classname=(TextView)findViewById(R.id.classname);
        classroom=(TextView)findViewById(R.id.classroom);
        week=(TextView)findViewById(R.id.week);
        class_time=(TextView)findViewById(R.id.class_time);
        teacher=(TextView)findViewById(R.id.teacher);
       btn_scan=(Button)findViewById(R.id.btn_scan);
       edit_class=(Button)findViewById(R.id.edit_class);
         delete=(Button)findViewById(R.id.delete);
        btn_scan.setOnClickListener(this);
         edit_class.setOnClickListener(this);
        delete.setOnClickListener(this);
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor cursor=db.query("information",null,null,null,null,null,null);
        //Toast.makeText(this,cursor.getCount()+"",Toast.LENGTH_SHORT).show();
//        if(cursor.getCount()==0)return;
        while(cursor.moveToNext()){
            if(new Integer(cursor.getString(0))==(ClassMessage.id-1000)){
                classname.setText(cursor.getString(1));
               classroom.setText(classroom.getText().toString()+cursor.getString(6));
                week.setText(week.getText().toString()+"1-20周");
                if(!cursor.getString(4).equals(cursor.getString(5)))
                class_time.setText(class_time.getText().toString()+cursor.getString(3)+" "+
                        cursor.getString(4)+"-"+cursor.getString(5)+"节");
                else
                    class_time.setText(class_time.getText().toString()+cursor.getString(3)+" 第"+
                            cursor.getString(4)+"节");
                teacher.setText(teacher.getText().toString()+cursor.getString(2));
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
                db.execSQL("DELETE FROM information WHERE _id = '"+(ClassMessage.id-1000)+"'"+";");
                Intent intent=new Intent(this,MainActivity.class);
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
            case R.id.edit_class:
                Intent intent2=new Intent(this,AddNewClass.class);
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
