package com.example.myapplication;

import com.example.course.Course;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoAddClass extends AppCompatActivity implements View.OnClickListener{
    private Button btn_exit, btn_add, btn_go;
    private ConstraintLayout step1, step2;
    private EditText website;
    private WebView web;
    public String addr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_add_class);

        btn_add = (Button) findViewById(R.id.btn_add);
        btn_exit = (Button) findViewById(R.id.btn_scan);
        btn_go = (Button) findViewById(R.id.btn_open);
        btn_add.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        step1 = (ConstraintLayout) findViewById(R.id.lay_step1);
        step2 = (ConstraintLayout) findViewById(R.id.lay_step2);
        website = (EditText) findViewById(R.id.et_website);
        web = (WebView) findViewById(R.id.web);

        web.getSettings().setJavaScriptEnabled(true);
        web.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        web.setWebViewClient(new WebViewClient());
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
            //ToDo 处理获取的课表数据    摸了
        }
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_add:
                //todo 获取课表数据，处理，将整理后的课表返回上一个activity
                break;
            case R.id.btn_open:
                if(!website.getText().toString().isEmpty()) {
                    addr = website.getText().toString();
                    step1.setVisibility(View.INVISIBLE);
                    step2.setVisibility(View.VISIBLE);
                    permission();
                }
                else Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_scan:
                //todo 直接返回上一个activity
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

    public static String process(String data){
        //todo 总之先将要用到的东西摆在这里
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
                    //str =parsePersonalCourse(str);
                    Course course = new Course();
                    course.setClsName(str);
                    course.setDay(j+1);
                    if(!td.attr("rowspan").isEmpty())
                        course.setClsCount(Integer.valueOf(td.attr("rowspan")));
                    else course.setClsCount(1);
                    course.setClsNum(i+1);
                    courses.add(course);
                }
            }
        }
        return courses.toString();
    }
}
