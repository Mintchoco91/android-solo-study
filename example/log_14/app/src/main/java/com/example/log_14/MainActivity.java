package com.example.log_14;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String kiwon = "기원";

        Log.e("MainActivity : ", kiwon);

        int a = 10;

        Log.e("MainActivity : ", String.valueOf(a));

        // 한줄 주석

        /*
            여러줄 주석
            여러줄 주석
            여러줄 주석
         */


    }
}