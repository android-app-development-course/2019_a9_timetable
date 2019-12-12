package com.example.course;

public class Course {
    //星期几:周一到周日
    private int day;
    //第几节课：总共12节
    private int clsNum;
    //每节课的长度
    private int clsCount;
    //课程名
    private String clsName;
    private String[] clss;

    public int getClsNum() {
        return clsNum;
    }

    public void setClsNum(int clsNum) {
        this.clsNum = clsNum;
    }

    public String getClsName() {
        return clsName;
    }

    public void setClsName(String clsName) {
        this.clsName = clsName;
    }

    public String getDay() {
        switch(day){
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
            default:
                return null;
        }
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getClsCount() {
        return clsCount;
    }

    public void setClsCount(int clsCount) {
        this.clsCount = clsCount;
    }

    public void setClss(){
        clss = clsName.split(" ");
    }

    public String getTeacherName() {
        return clss.length < 5 ? clss[clss.length - 1] : clss[clss.length - 2];
    }

    public String getLocation(){
        return clss.length < 5 ? "" : clss[clss.length - 1];
    }

    public String getName(){
        return clss[0];
    }

    @Override
    public String toString() {
        return getName() + "-"
                + getTeacherName() + "-"
                + getDay() + "-"
                + getClsNum() + "-"
                + (getClsNum() + getClsCount() - 1) + "-"
                + getLocation() + "\n";
        //课程名，老师，星期，开始、结束，课室
    }
}