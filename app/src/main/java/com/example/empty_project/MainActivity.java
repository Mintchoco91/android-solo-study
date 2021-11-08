package com.example.empty_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

//안드로이드 앱 만들기 #9 (WebView)
public class MainActivity extends AppCompatActivity {

    EditText et_save;
    String shared = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_save = (EditText)findViewById(R.id.et_save);

        //데이터 불러오는 부분
        SharedPreferences sharedPreferences = getSharedPreferences(shared, 0);
        String value = sharedPreferences.getString("kiwon", "");
        et_save.setText(value);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //데이터 저장부분
        SharedPreferences sharedPreferences= getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String value = et_save.getText().toString();
        editor.putString("kiwon", value); //kiwon 이라는 값으로 데이터를 불러온다.
        editor.commit();    //해당 데이터를 save해라.

    }
}