package com.example.myapplication;



import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;


public class select_deadlinetime extends DialogFragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    //Button classnum;
    int year,month,day;
    View view,view1;
    String[]week;
    NumberPicker numberPicker1;
    NumberPicker numberPicker2;
    NumberPicker numberPicker3;
    TextView time;
    Button makesure;
    Button cancel;
    private MyDialogFragment_Listener myDialogFragment_Listener;
    // 回调接口，用于传递数据给Activity -------
    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment(String selectnum);
    }
    public void getDate() {
        Calendar calendar = Calendar.getInstance();

//获取系统的日期
//年
        year = calendar.get(Calendar.YEAR);
//月
        month = calendar.get(Calendar.MONTH) + 1;
//日
        day = calendar.get(Calendar.DAY_OF_MONTH);
//获取系统时间
//小时
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            myDialogFragment_Listener = (MyDialogFragment_Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implementon MyDialogFragment_Listener");
        }
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.select_deadline_time, container);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        makesure=(Button)view.findViewById(R.id.makesure);
        cancel=(Button)view.findViewById(R.id.cancel);
        makesure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        time= (TextView) view.findViewById(R.id.time);
        numberPicker1 = (NumberPicker) view.findViewById(R.id.timeselect1);
        numberPicker1.setMaxValue(2050);
        numberPicker1.setMinValue(1998);
        numberPicker1.setValue(2019);
        numberPicker1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker1.setGravity(Gravity.CENTER);
        numberPicker1.setOnValueChangedListener(this);
        numberPicker3 = (NumberPicker) view.findViewById(R.id.timeselect3);
        numberPicker3.setMaxValue(31);
        numberPicker3.setMinValue(1);
        numberPicker3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker3.setGravity(Gravity.CENTER);
        //设置滑动监听
        numberPicker3.setOnValueChangedListener(this);
        numberPicker2 = (NumberPicker) view.findViewById(R.id.timeselect2);
       numberPicker2.setMaxValue(12);
        numberPicker2.setMinValue(1);
        numberPicker2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker2.setGravity(Gravity.CENTER);
        //设置滑动监听
        numberPicker2.setOnValueChangedListener(this);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels,  getDialog().getWindow().getAttributes().height );
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        int year=((NumberPicker)view.findViewById(R.id.timeselect1)).getValue();
        int month=((NumberPicker)view.findViewById(R.id.timeselect2)).getValue();
        int day=((NumberPicker)view.findViewById(R.id.timeselect3)).getValue();
       time.setText(year+"-"+month+"-"+day);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:dismiss();break;
            case R.id.makesure:
                if(!time.equals("请选择截止时间"))
                {
                    myDialogFragment_Listener.getDataFrom_DialogFragment(time.getText().toString());
                    dismiss();
                    break;
                }
                    else
                {
                    myDialogFragment_Listener.getDataFrom_DialogFragment(year+"-"+month+"-"+day);
                    dismiss();
                    break;
                }

        }
    }
}

