package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddHomework extends AppCompatActivity implements View.OnClickListener, select_deadlinetime.MyDialogFragment_Listener {
Button btn_return;
EditText classname;
EditText jobname;
Button deadline;
Button btn_save;
EditText content;
MyHelper myHelper;
String deadlinetime[];
public void add(){
    if(deadline.getText().toString().equals("")) {
        Toast.makeText(this,"添加失败，请选择截止时间",Toast.LENGTH_SHORT).show();
        return;
    }
    SQLiteDatabase db=myHelper.getWritableDatabase();
    ContentValues values=new ContentValues();
    values.put("project",classname.getText().toString());
    values.put("jobname",jobname.getText().toString());
deadlinetime=deadline.getText().toString().split("-");
    values.put("deadlineyear",deadlinetime[0]);
    values.put("deadlinemonth",deadlinetime[1]);
    values.put("deadlineday",deadlinetime[2]);
    values.put("content",content.getText().toString());
    if(!HomeworkDetail.edit)
    db.insert("myhomework",null,values);
    else{
        db.execSQL("DELETE FROM myhomework WHERE _id = '"+HomeMessage.id+"'"+";");
        db.insert("myhomework",null,values);
HomeworkDetail.edit=false;
    }
    db.close();
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_homework);
        HomeMessage.homework=true;
        myHelper=new MyHelper(this);
        content=(EditText)findViewById(R.id.content);
        deadline=(Button)findViewById(R.id.deadline);
        jobname=(EditText)findViewById(R.id.jobname);
        btn_save=(Button)findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        classname=(EditText)findViewById(R.id.classname);
        btn_return=(Button)findViewById(R.id.btn_return);
        btn_return.setOnClickListener(this);
        deadline.setOnClickListener(this);
        if(HomeworkDetail.edit){
      //      HomeworkDetail.edit=false;
            String s1=HomeworkDetail.classname.getText().toString();
            String s2=HomeworkDetail.jobname.getText().toString();
            String s3=HomeworkDetail.deadline.getText().toString();
            String s4=HomeworkDetail.content.getText().toString();
            classname.setText(s1);
            jobname.setText(s2.substring(s2.indexOf(":")+1));
            deadline.setText(s3.substring(s3.indexOf(":")+1));
            content.setText(s4);
        }
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_save:
                add();
                Intent intent=new Intent(AddHomework.this,MainActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.deadline:
                select_deadlinetime editNameDialogFragment=new select_deadlinetime();
                editNameDialogFragment.show(getFragmentManager(),"1");
                break;
            case R.id.btn_return:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.exit)
                        .setTitle("提示！")
                        .setMessage("确认取消添加作业信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HomeworkDetail.edit=false;

                                Intent intent=new Intent(AddHomework.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
        }
    }
    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.exit)
                        .setTitle("提示！")
                        .setMessage("确认取消添加作业信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HomeworkDetail.edit=false;

                                Intent intent=new Intent(AddHomework.this,MainActivity.class);
                                finish();
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
        }
        return false;

    }
    @Override
    public void getDataFrom_DialogFragment(String selectnum) {
if(!selectnum.equals("请选择截止时间"))
        deadline.setText(selectnum);
        // Log.d("Tag", "DialogFragment回传的数据为：" + data01 + " " + data02 + " " + data03);
    }
}
