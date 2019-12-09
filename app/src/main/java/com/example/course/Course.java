package com.example.course;

//todo 继续把各种内容拆分
public class Course {
    //星期几:周一到周日
    private int day;
    //第几节课：总共12节
    private int clsNum;
    //每节课的长度
    private int clsCount;
    //课程名
    private String clsName;

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

    public int getDay() {
        return day;
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

    /*@Override
    public String toString() {
        return "\nStuClass{" +
                "clsCount=" + clsCount +
                ", day=" + day +
                ", clsNum=" + clsNum +
                ", clsName='" + clsName + '\'' +
                "}\n";
    }*/
}