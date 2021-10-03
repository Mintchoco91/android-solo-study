package com.example.test3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//https://www.youtube.com/watch?v=fRDy13p8L78&list=PLC51MBz7PMyyyR2l4gGBMFMMUfYmBkZxm&index=5
//안드로이드 앱 만들기 #4 (Intent 화면전환) - 쉽게 앱 만드는 방법 (현직 개발자 설명)
public class MainActivity extends AppCompatActivity {

    private ListView list;

    @Override
    //처음 실행할때 실행되는 부분
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //객체 가져오기
        list = (ListView) findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        // this, [유형], [어댑터명]
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        data.add("data1");
        data.add("data2");
        adapter.notifyDataSetChanged(); //이 데이터를 저장해라
    }
}