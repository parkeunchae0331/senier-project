package com.example.myapplication;

public class ProductModel {
    //도착시간을 mainlayout에 불러 오기 위해 선언한 모델
    private String ArriveTime;


    private ProductModel() {}
    private ProductModel(String ArriveTime){
        this.ArriveTime=ArriveTime;
    }

    public String getArriveTime(){
        return ArriveTime;
    }

    public void setArriveTime(String ArriveTime){
        this.ArriveTime = ArriveTime;
    }
}
