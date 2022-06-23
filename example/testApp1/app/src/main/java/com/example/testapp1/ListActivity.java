package com.example.testapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ListActivity extends Activity {
    Button btnClose, btnMoveHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnMoveHome = (Button) findViewById(R.id.btnMoveHome);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMoveHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHome = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intentHome);
            }
        });
    }
}
