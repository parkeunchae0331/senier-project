package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);//activity_loading 불러옴

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), GoogleLoginActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);//Handler를 사용하여 GoogleLoginActivity가 실행되기 전에 2초간 activity_loading 띄움
    }
}
