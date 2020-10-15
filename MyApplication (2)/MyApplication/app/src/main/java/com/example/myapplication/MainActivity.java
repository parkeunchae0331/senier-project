package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;//환경설정 부분이 될 drawer 선언
    ActionBarDrawerToggle toggle;
    boolean isDrawerOpened;

    private RecyclerView mFireStoreList;
    private FirebaseFirestore firebaseFirestore;

    private FirestoreRecyclerAdapter adapter;
    //FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireStoreList = findViewById(R.id.firestore_list);
        firebaseFirestore=FirebaseFirestore.getInstance();
        //Query
        Query query1 = firebaseFirestore.collection("Users").document("Child1").collection("ArriveCollection").orderBy("ArriveTime", Query.Direction.DESCENDING);
        //쿼리를 사용함 -> User에 저장되어있는 특정 어린이의 도착시간이 내림차순으로 정렬

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>()
                .setQuery(query1,ProductModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ProductModel, ProductsViewHolder>(options) {
            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new ProductsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull ProductModel model) {
                holder.list_time.setText(model.getArriveTime());
            }
        };
        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList.setAdapter(adapter);


        //푸시알림
        Intent intent = getIntent();
        if(intent != null) {//푸시알림을 선택해서 실행한것이 아닌경우 예외처리
            String notificationData = intent.getStringExtra("test");
            if(notificationData != null)
                Log.d("FCM_TEST", notificationData);
        }


        //네비게이션 드로어
        drawer = findViewById(R.id.main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        getSupportActionBar().setTitle("우리 아이 등원 시간");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#B183E6")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_drawer_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id=menuItem.getItemId();
                if(id==R.id.menu_drawer_logout){
                    signOut();
                }else if(id==R.id.menu_drawer_bye){
                    revokeAccess();
                }//else if(id==R.id.app_bar_switch){

                //}

                return false;
            }
        });

    }
    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        private TextView list_time;

        public ProductsViewHolder(@NonNull View itemView){
            super(itemView);

            list_time=itemView.findViewById(R.id.list_time);

        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();

    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(MainActivity.this,GoogleLoginActivity.class);
        startActivity(intent);
        finish();//로그아웃
    }
    private void revokeAccess(){
        FirebaseAuth.getInstance().getCurrentUser().delete();
        Intent intent=new Intent(MainActivity.this,GoogleLoginActivity.class);
        startActivity(intent);
        finish();//탈퇴
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(toggle.onOptionsItemSelected(item)){
            return false;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showToast(String message){
        Toast toast=Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}

