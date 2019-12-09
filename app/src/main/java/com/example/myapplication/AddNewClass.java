package com.example.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewClass extends AppCompatActivity implements View.OnClickListener, select_class_time.MyDialogFragment_Listener {
    private Button save,btnreturn;
    Button time;
    private EditText classname;
    private EditText classroom;
    private Button class_time;
    private EditText teacher;
    double width;
    double height;
    static Button button;
    MyHelper myHelper;
    int s;
    public void add(){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("project",classname.getText().toString());
        values.put("teacher",teacher.getText().toString());
        values.put("week",class_time.getText().toString().substring(class_time.getText().toString().indexOf("周"),class_time.getText().toString().indexOf("周")+2));
        int start1=class_time.getText().toString().indexOf("第");
        int start2=class_time.getText().toString().indexOf("-");
        int ends=class_time.getText().toString().indexOf("节");
        int space=class_time.getText().toString().indexOf(" ");
        if(start1!=-1) {
            values.put("start", class_time.getText().toString().substring(start1+1,ends));
            values.put("ends", class_time.getText().toString().substring(start1+1,ends));
        }
        else{
            values.put("start", class_time.getText().toString().substring(space+1,start2));
            values.put("ends", class_time.getText().toString().substring(start2+1));

        }
        values.put("location",classroom.getText().toString());
        if(!ClassDetail.edit)
        db.insert("information",null,values);
        else
        {
            db.execSQL("DELETE FROM information WHERE _id = '"+(ClassMessage.id-1000)+"'"+";");
            db.insert("information",null,values);
            ClassDetail.edit=false;
        }
        db.close();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_class);
        button=null;
        classname=(EditText)findViewById(R.id.classname);
        classroom=(EditText)findViewById(R.id.classroom);
        class_time=(Button)findViewById(R.id.class_time);
        teacher=(EditText)findViewById(R.id.teacher);
        if(ClassDetail.edit){
            String s1=ClassDetail.classname.getText().toString();
            String s2=ClassDetail.classroom.getText().toString();
            String s3=ClassDetail.week.getText().toString();
            String s4=ClassDetail.class_time.getText().toString();
            String s5=ClassDetail.teacher.getText().toString();
            classname.setText(s1);
        //    classroom.setText("asa");
           classroom.setText(s2.substring(s2.indexOf(":")+1));
            class_time.setText(s4.substring(s4.indexOf(":")+1));
            teacher.setText(s5.substring(s5.indexOf(":")+1));
        }
        myHelper=new MyHelper(this);
        btnreturn=(Button)findViewById(R.id.btn_return);
        btnreturn.setOnClickListener(this);
        // EditText editText=(EditText)findViewById(R.id.classnumber);

      //  time=(Button)findViewById(R.id.time);
       // time.setOnClickListener(this);
        String week[]={"一","二","三","四","五","六","日"};
        if(!ClassDetail.edit) {
            s=MainActivity.b.getId();
            class_time.setText("周" + week[(s - 1) % 8 - 1] + " 第" + ((s - 1) / 8 + 1) + "节");
        }
//Toast.makeText(this,classnumber.getText().toString().indexOf("周")+"   "+classnumber.getText().toString().indexOf("一"),Toast.LENGTH_SHORT).show();
        save=(Button)findViewById(R.id.btn_save);
        save.setOnClickListener(this);
        class_time.setOnClickListener(this);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width=metrics.widthPixels;
        height=metrics.heightPixels;

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_return:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.exit)
                        .setTitle("提示！")
                        .setMessage("确认取消添加课程信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              ClassDetail.edit=false;
                                Intent intent=new Intent(AddNewClass.this,MainActivity.class);
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
            case R.id.btn_save:
                add();
                Intent intent=new Intent(AddNewClass.this,MainActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                break;
            case R.id.class_time:
                select_class_time editNameDialogFragment=new select_class_time();
                editNameDialogFragment.show(getFragmentManager(),"1");
                break;

        }
    }
    @Override
    public void getDataFrom_DialogFragment(String selectnum) {
        class_time.setText(selectnum);
        // Log.d("Tag", "DialogFragment回传的数据为：" + data01 + " " + data02 + " " + data03);
    }
    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.exit)
                        .setTitle("提示！")
                        .setMessage("确认取消添加课程信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClassDetail.edit=false;
                                Intent intent=new Intent(AddNewClass.this,MainActivity.class);
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
}
