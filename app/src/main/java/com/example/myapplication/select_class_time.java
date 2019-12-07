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


public class select_class_time extends DialogFragment implements NumberPicker.OnValueChangeListener, View.OnClickListener {
    //Button classnum;
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
        view = inflater.inflate(R.layout.select_class_time, container);

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
        numberPicker1.setMaxValue(12);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(2);
        numberPicker1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,3));
        numberPicker1.setGravity(Gravity.CENTER);
        numberPicker1.setOnValueChangedListener(this);
        numberPicker3 = (NumberPicker) view.findViewById(R.id.timeselect3);
        week = new String[]{"周一","周二","周三","周四","周五","周六","周日"};
        numberPicker3.setDisplayedValues(week);
        numberPicker3.setMaxValue(week.length-1);
        numberPicker3.setMinValue(0);
        numberPicker3.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker3.setGravity(Gravity.CENTER);
        //设置滑动监听
        numberPicker3.setOnValueChangedListener(this);
        numberPicker2 = (NumberPicker) view.findViewById(R.id.timeselect2);
        String[] end = {"到1","到2","到3","到4","到5","到6","到7","到8","到9","到10","到11","到12"};
        numberPicker2.setDisplayedValues(end);
        numberPicker2.setMaxValue(end.length-1);
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

        int start=((NumberPicker)view.findViewById(R.id.timeselect1)).getValue();
        int end=((NumberPicker)view.findViewById(R.id.timeselect2)).getValue();
        if(start<end)
            time.setText(week[((NumberPicker)view.findViewById(R.id.timeselect3)).getValue()]+" "+start+"-"+end);
        else if(start==end)
            time.setText(week[((NumberPicker)view.findViewById(R.id.timeselect3)).getValue()]+" 第"+start+"节");
        else {
            ((NumberPicker)view.findViewById(R.id.timeselect2)).setValue(start);
            time.setText(week[((NumberPicker)view.findViewById(R.id.timeselect3)).getValue()]+" 第"+start+"节");

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:dismiss();break;
            case R.id.makesure:myDialogFragment_Listener.getDataFrom_DialogFragment(time.getText().toString());dismiss();break;
        }
    }
}

