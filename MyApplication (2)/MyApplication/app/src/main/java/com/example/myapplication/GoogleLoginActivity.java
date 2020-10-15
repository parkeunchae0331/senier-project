package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class GoogleLoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlelogin);//layout은 activity_googlelogin


        // 어떤 방법으로 인증할지 선택
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // 로그인 인텐트 생성 및 실행
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .setLogo(R.drawable.iconbus_big)
                        .setTheme(R.style.googleloginT)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // 성공적으로 인증이 되었을 때
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);////어린이 사용자를 입력하는 LoginActivity로 넘어감
                finish();


                // ...
            } else {
                // 로그인 실패시
                Toast.makeText(this,"로그인 실패, 로그인을 다시 시도해주세요" , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



}
