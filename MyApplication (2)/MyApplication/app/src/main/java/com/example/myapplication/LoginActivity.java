package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();//파이어스토어에 저장된 db사용

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//activity_login 레이아웃 사용
        Button go = (Button) findViewById(R.id.go) ; //확인버튼
        Button update=(Button) findViewById(R.id.update);
        final EditText child=(EditText)findViewById(R.id.editText);//어린이 입력 텍스트박스

        Date date = new Date();
        String stringDate = DateFormat.getDateTimeInstance().format(date);

        //버튼 눌렀을 때 새로운시간 뜨도록
        final Map<String, Object> ArriveCollection = new HashMap<>();
        //ArriveCollection.put("ArriveTime", new Timestamp(new Date()));
        ArriveCollection.put("ArriveTime", stringDate);

        go.setOnClickListener(new View.OnClickListener(){public void onClick(View v){
            db.collection("Users")
                    .orderBy("age", Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("qwe", document.getId() + " => " + document.getData());//log에서 저장된 데이터 확인 가능
                                    if(child.getText().toString().equals(document.getId()))//만약 어린이 id가 파이어베이스에 저장된 id와 같다면
                                    {
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);//MainActivity로 넘어감
                                        finish();
                                    }

                                }
                            } else {
                                Log.w("rty", "Error getting documents.", task.getException());//log에 오류 띄워줌
                            }
                        }
                    });

            }
        });
        update.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //클릭했을때 TodayTime 하나 더 생기게
                db.collection("Users").document("Child1").collection("ArriveCollection").document()
                        .set(ArriveCollection)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("asd", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("aer", "Error adding document", e);
                            }
                        });

                db.collection("Users").document("Child1").collection("ArriveCollection")
                        .orderBy("ArriveTime", Query.Direction.DESCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d("qwe", document.getId() + " => " + document.getData());
                                    }
                                } else {
                                    Log.w("rty", "Error getting documents.", task.getException());
                                }
                            }
                        });
                //db.collection("ArriveCollection")
                //.orderBy("ArriveTime", Query.Direction.DESCENDING);
            }

        });

        //update랑 delete는 그때그때 상황보고 합치기
    }

}