package com.example.testapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

//이걸로 실제어플 만들거임.
public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private String jsonString, StrUserName, StrGender, StrAge, StrPhoneNumber;
    private EditText etUserName, etPhoneNumber;
    private Button btnSave, btnList, btnUploadList;
    ArrayList<Tree> treeArrayList;      // 나무정보들을 저장할 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSave = (Button) findViewById(R.id.activity_main_btn_save);
        btnList = (Button) findViewById(R.id.activity_main_btn_list);
        btnUploadList = (Button) findViewById(R.id.activity_main_btn_upload_list);

        etUserName = (EditText) findViewById(R.id.activity_main_et_user_name);
        etPhoneNumber = (EditText)findViewById(R.id.activity_main_et_phone_number);

        //spinner
        Spinner spinnerAge = (Spinner)findViewById(R.id.activity_main_spn_age);
        spinnerAge.setSelection(0);
        ArrayAdapter adapterAge = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setPrompt("나이를 선택하세요.");
        spinnerAge.setAdapter(adapterAge);

        Button btnGenderMan = findViewById(R.id.activity_main_btn_gender_man);
        Button btnGenderWoman = findViewById(R.id.activity_main_btn_gender_woman);

        btnGenderMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrGender = "남";
                btnGenderMan.setBackgroundColor(Color.LTGRAY);
                btnGenderWoman.setBackgroundColor(Color.WHITE);
            }
        });

        btnGenderWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrGender = "여";
                btnGenderMan.setBackgroundColor(Color.WHITE);
                btnGenderWoman.setBackgroundColor(Color.LTGRAY);
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrUserName = String.valueOf(etUserName.getText()).trim();
                StrAge = spinnerAge.getSelectedItem().toString();
                StrPhoneNumber = String.valueOf(etPhoneNumber.getText()).trim();

                if(StrUserName.length() == 0){
                    Toast.makeText(MainActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    etUserName.requestFocus();
                    return;
                }
                if(StrGender == null){
                    Toast.makeText(MainActivity.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                    btnGenderMan.requestFocus();
                    return;
                }
                if(StrAge.equals("선택")){
                    Toast.makeText(MainActivity.this, "나이를 선택하세요.", Toast.LENGTH_SHORT).show();
                    spinnerAge.requestFocus();
                    return;
                }
                if(StrPhoneNumber.length() == 0){
                    Toast.makeText(MainActivity.this, "핸드폰 번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                    etPhoneNumber.requestFocus();
                    return;
                }

                //다음 단계로 (이미지 업로드)
                Intent intentUpload = new Intent(MainActivity.this, FileUploadActivity.class);
                intentUpload.putExtra("StrUserName",StrUserName);
                intentUpload.putExtra("StrGender",StrGender);
                intentUpload.putExtra("StrAge",StrAge);
                intentUpload.putExtra("StrPhoneNumber",StrPhoneNumber);

                startActivity(intentUpload);
            }
        });


        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent -> 외부 activity 선언
                Intent intentList = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intentList);
            }
        });

        btnUploadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUploadList = new Intent(MainActivity.this, FileUploadListActivity.class);
                startActivity(intentUploadList);
            }
        });

        /* select시 참고할것. */
        //textView = (TextView) findViewById(R.id.textView);
        //JsonParse jsonParse = new JsonParse();      // AsyncTask 생성
        //jsonParse.execute("https://androidserver-kw2.herokuapp.com/convertJson.php");     // AsyncTask 실행
    }


    public String accountQueryExcute(final String mode, final String userName, final String gender, final String age, final String phoneNumber) {
        try {
            String url = "https://androidserver-kw2.herokuapp.com/accountQuery.php/";
            URL serverURL = new URL(url);
            //String postData = "mode=" + mode + "&" + "userName=" + userName + "&" + "gender=" + gender + "&" + "age=" + age + "&" + "phoneNumber=" + phoneNumber;
            String postData = "mode=insert&userName=이름&gender=남&age=29&phoneNumber=01047353592";
            HttpURLConnection conn = (HttpURLConnection)serverURL.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            //String result = readStream(conn.getInputStream());
            String result = "success!";
            conn.disconnect();
            Toast.makeText(MainActivity.this, "성공", Toast.LENGTH_SHORT).show();
            return result;
        }
        catch (Exception e) {
            Toast.makeText(MainActivity.this, "실패", Toast.LENGTH_SHORT).show();
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }

        public class JsonParse extends AsyncTask<String, Void, String> {
        String TAG = "JsonParseTest";
        @Override
        protected String doInBackground(String... strings) {    // execute의 매개변수를 받아와서 사용
            String url = strings[0];
            try {
                URL serverURL = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) serverURL.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                Log.d(TAG, sb.toString().trim());

                return sb.toString().trim();        // 받아온 JSON의 공백을 제거
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                String errorString = e.toString();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String fromdoInBackgroundString) { // doInBackgroundString에서 return한 값을 받음
            super.onPostExecute(fromdoInBackgroundString);

            if(fromdoInBackgroundString == null)
                textView.setText("error");
            else {
                jsonString = fromdoInBackgroundString;
                treeArrayList = doParse();
                Log.d(TAG,treeArrayList.get(0).getUserName());
                textView.setText(treeArrayList.get(0).getUserName());
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }


        private ArrayList<Tree> doParse() {
            ArrayList<Tree> tmpTreeArray = new ArrayList<Tree>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("Tree");

                for(int i=0;i<jsonArray.length();i++) {
                    Tree tmpTree = new Tree();
                    JSONObject item = jsonArray.getJSONObject(i);
                    tmpTree.setId(item.getString("id"));
                    tmpTree.setUserName(item.getString("userName"));

                    tmpTreeArray.add(tmpTree);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return tmpTreeArray;
        } // JSON을 가공하여 ArrayList에 넣음
    }
}