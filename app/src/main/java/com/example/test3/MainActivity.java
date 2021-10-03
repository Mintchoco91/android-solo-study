package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//https://www.youtube.com/watch?v=EKCQ6sxMWNo&list=PLC51MBz7PMyyyR2l4gGBMFMMUfYmBkZxm&index=4
//안드로이드 앱 만들기 #3 (Intent 화면전환) - 쉽게 앱 만드는 방법 (현직 개발자 설명)
public class MainActivity extends AppCompatActivity {

    EditText et_id;
    Button btn_move;
    private String str;

    @Override
    //처음 실행할때 실행되는 부분
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //객체 가져오기
        et_id = findViewById(R.id.et_id);
        btn_move = findViewById(R.id.btn_move);

        //버튼 클릭시 액션 주는부분.
        btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.텍스트 변환
                //et_id.setText("버튼 클릭했음");

                //2.화면 이동 & 데이터 전송
                str = et_id.getText().toString();
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                intent.putExtra("prevPageStr",str); //"str은 별명이다."
                startActivity(intent);  //  액티비티 이동.
            }
        });
    }
}