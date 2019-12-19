package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.course.Course;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AutoAddClass extends AppCompatActivity implements View.OnClickListener{
    private Button btn_return, btn_save, btn_go, btn_two;
    private ConstraintLayout step1, step2;
    private EditText website;
    private WebView web;
    private TextView tv_instruction;
    public String addr = "";
    public List<Course> html;
    private MyHelper myHelper;
    protected   int dp2px(int dpVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,
                getResources().getDisplayMetrics());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_add_class);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);		//屏幕宽度
int width=dm.widthPixels;
int t=dp2px(75);
        btn_two = (Button) findViewById(R.id.two);
        btn_two.setX(width/2-t);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_return = (Button) findViewById(R.id.btn_return);
        btn_go = (Button) findViewById(R.id.btn_open);
        btn_save.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_return.setOnClickListener(this);
        tv_instruction = (TextView) findViewById(R.id.tv_instruction);
        step1 = (ConstraintLayout) findViewById(R.id.lay_step1);
        step2 = (ConstraintLayout) findViewById(R.id.lay_step2);
        website = (EditText) findViewById(R.id.et_website);
        web = (WebView) findViewById(R.id.web);
        myHelper=new MyHelper(this);

        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        web.setWebViewClient(new WebViewClient(){
            @Override
            public void onLoadResource(WebView view, String url){
                web.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                        "document.getElementsByTagName('iframe')[0]" +
                        ".contentWindow.document.body.innerHTML+'</head>');");
                super.onLoadResource(view, url);
            }
        });
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result)
            {
                return super.onJsAlert(view, url, message, result);
            }
        });   //登陆后的自动跳转
        //WebView各种设置
        WebSettings webSettings = web.getSettings();
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
    }

    final class InJavaScriptLocalObj{
        @JavascriptInterface
        public void showSource(String content){
            html = process(content);
            //addClass(process(content));
        }
    }   //JS脚本class
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_save:
                web.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                        "document.getElementsByTagName('iframe')[0]" +
                        ".contentWindow.document.body.innerHTML+'</head>');");
                addClass(html);
                finish();
                startActivity(new Intent(AutoAddClass.this, MainActivity.class));
                break;
            case R.id.btn_open:
                if(!website.getText().toString().isEmpty()) {
                    addr = website.getText().toString();
                    step1.setVisibility(View.INVISIBLE);
                    step2.setVisibility(View.VISIBLE);
                    permission();
                    btn_two.setBackgroundResource(R.drawable.btn_circle);
                    tv_instruction.setText(R.string.instruction2);
                }
                else Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_return:
                startActivity(new Intent(AutoAddClass.this, MainActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 404: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openWeb();
                    Toast.makeText(this,"success",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this,"failed",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void openWeb(){
        web.loadUrl(addr);
    }   //这好像也没有缩减了什么

    private void permission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.INTERNET)) {
                Toast.makeText(this,"explore the Internet",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET},
                        404);
            }
        } else {
            openWeb();
        }
    }   //获取权限，访问网页

    private static List<Course> process(String data){
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.parse(data);
        //首先获取Table
        Element table = doc.getElementById("Table1");
        //然后获取table中的td节点
        Elements trs = table.select("tr");
        //移除不需要的参数，这里表示移除前两个数值。
        trs.remove(0);
        trs.remove(0);
        for (int i=0; i<trs.size(); ++i){
            Element tr = trs.get(i);
            //获取tr下的td节点，要求
            Elements tds = tr.select("td[align]");
            //遍历td节点
            for(int j=0; j<tds.size(); ++j){
                Element td = tds.get(j);
                String str = td.text();
                //如果数值为空则不计算。
                if (!str.isEmpty()){
                    //解析文本数据
                    Course course = new Course();
                    course.setDay(j+1);
                    course.setClsNum(i+1);
                    course.setClsName(str);
                    if(!td.attr("rowspan").isEmpty()) {
                        course.setClsCount(Integer.valueOf(td.attr("rowspan")));
                    } else{
                        Iterator iter = courses.iterator();
                        while(iter.hasNext()){
                            Course temp = (Course) iter.next();
                            if(temp.getName().equals(course.getClsName().split(" ")[0])
                                    && temp.getDay().equals(course.getDay())
                                    && temp.getClsNum() == course.getClsNum()-2) {
                                course.setClsNum(temp.getClsNum());
                                course.setClsName(temp.getClsName());
                                iter.remove();
                            }
                        }
                        course.setClsCount(3);
                    }
                    course.setClss();
                    courses.add(course);
                }
            }
        }
        return courses;
    }   //处理获得的html数据

    private void addClass(List<Course> classes){
        Course cla;
        Iterator iterator = classes.iterator();
        SQLiteDatabase db = myHelper.getWritableDatabase();
        while(iterator.hasNext()){
            ContentValues values = new ContentValues();
            cla = (Course) iterator.next();
            values.put("project", cla.getName());
            values.put("teacher", cla.getTeacherName());
            values.put("week", "周" + cla.getDay());
            values.put("start", cla.getClsNum());
            values.put("ends", (cla.getClsNum()+cla.getClsCount()-1));
            values.put("location", cla.getLocation());
            db.insert("information", null, values);
        }   //课程名，老师，星期，开始、结束，课室
        db.close();
        /*String str = "";
        while(iterator.hasNext()){
            cla = (Course) iterator.next();
            str += cla.getClsName()+",";
            str += cla.getTeacherName()+",";
            str += cla.getDay()+",";
            str += cla.getClsNum()+",";
            str += cla.getLocation()+"\n";
        }
        Toast.makeText(this, classes.size()+"\n"+str, Toast.LENGTH_LONG).show();*/
    }   //添加课程到数据库中
    @Override

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.exit)
                        .setTitle("提示！")
                        .setMessage("确认放弃自动导入课程信息吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              //  ClassDetail.edit=false;
                                Intent intent=new Intent(AutoAddClass.this,MainActivity.class);
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

