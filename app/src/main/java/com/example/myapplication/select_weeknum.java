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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;


public class select_weeknum extends DialogFragment implements NumberPicker.OnValueChangeListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    //Button classnum;
  //  int year,month,day;
    RadioGroup radioGroup;
    View view;
  //  String[]week;
    NumberPicker numberPicker1;
    NumberPicker numberPicker2;
    TextView time;
    Button makesure;
    Button cancel;
    String week="全部";
    private MyDialogFragment_Listener1 myDialogFragment_Listener;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        week=((RadioButton)view.findViewById(i)).getText().toString();
    }

    // 回调接口，用于传递数据给Activity -------
    public interface MyDialogFragment_Listener1 {
        void getDataFrom_DialogFragment1(String selectnum);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            myDialogFragment_Listener = (MyDialogFragment_Listener1) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implementon MyDialogFragment_Listener");
        }
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.select_weeknum, container);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        radioGroup=(RadioGroup)view.findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        makesure=(Button)view.findViewById(R.id.makesure);
        cancel=(Button)view.findViewById(R.id.cancel);
        makesure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        time= (TextView) view.findViewById(R.id.time);
        numberPicker1 = (NumberPicker) view.findViewById(R.id.timeselect1);
        numberPicker1.setMaxValue(20);
        numberPicker1.setMinValue(1);
        numberPicker1.setValue(1);
        numberPicker1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker1.setGravity(Gravity.CENTER);
        numberPicker1.setOnValueChangedListener(this);

        numberPicker2 = (NumberPicker) view.findViewById(R.id.timeselect2);
        numberPicker2.setMaxValue(20);
        numberPicker2.setMinValue(1);
        numberPicker2.setValue(20);
        numberPicker2.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1));
        numberPicker2.setGravity(Gravity.CENTER);
        //设置滑动监听
        numberPicker2.setOnValueChangedListener(this);
       // time.setText(numberPicker1.getValue()+"-"+numberPicker2.getValue());
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
        int ends=((NumberPicker)view.findViewById(R.id.timeselect2)).getValue();
       if(ends<=start){
           ((NumberPicker)view.findViewById(R.id.timeselect2)).setValue(start);
           time.setText("第"+start+"周");
       }
       else
           time.setText(start+"-"+ends);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:dismiss();break;
            case R.id.makesure:
                if(!week.equals("全部"))
                    time.setText(time.getText().toString()+"("+week+")");
                myDialogFragment_Listener.getDataFrom_DialogFragment1(time.getText().toString());
                    dismiss();
                    break;
        }
    }
}

