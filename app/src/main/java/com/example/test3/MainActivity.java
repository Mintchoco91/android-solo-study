package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//https://www.youtube.com/watch?v=fRDy13p8L78&list=PLC51MBz7PMyyyR2l4gGBMFMMUfYmBkZxm&index=5
//안드로이드 앱 만들기 #4 (Intent 화면전환) - 쉽게 앱 만드는 방법 (현직 개발자 설명)
public class MainActivity extends AppCompatActivity {

    ImageView img_test;

    @Override
    //처음 실행할때 실행되는 부분
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //객체 가져오기
        img_test = findViewById(R.id.img_test);

        //버튼 클릭시 액션 주는부분.
        img_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. toast  (getApplicationContext,[내용],[띄울시간])
                Toast.makeText(getApplicationContext(), "ToastTest",Toast.LENGTH_SHORT).show();
            }
        });
    }
}