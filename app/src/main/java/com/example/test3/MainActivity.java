package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText et_id;
    Button btn_test;

    @Override
    //처음 실행할때 실행되는 부분
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //객체 가져오기
        et_id = findViewById(R.id.et_id);
        btn_test = findViewById(R.id.btn_test);

        //버튼 클릭시 액션 주는부분.
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_id.setText("버튼 클릭했음");
            }
        });

    }
}