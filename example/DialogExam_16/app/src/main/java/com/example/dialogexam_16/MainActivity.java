package com.example.dialogexam_16;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn_dialog;
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_dialog = (Button) findViewById(R.id.btn_dialog);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                //Dialog에서 띄울 아이콘
                ad.setIcon(R.mipmap.ic_launcher);
                ad.setTitle("제목");
                //Dialog에 중간 메세지
                ad.setMessage("다이얼로그 질문을 하시겠습니까?");

                //Dialog안에 EditText객체를 Add해주기
                final EditText et = new EditText(MainActivity.this);
                ad.setView(et);

                //Yes Btn
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = et.getText().toString();
                        tv_result.setText(result);
                        dialog.dismiss(); //Dialog 닫기

                    }
                });

                //No Btn
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //Dialog 닫기
                    }
                });
                //다이얼로그 구현
                ad.show() ;

            }
        });
    }


}