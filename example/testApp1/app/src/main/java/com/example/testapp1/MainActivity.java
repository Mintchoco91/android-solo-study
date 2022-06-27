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
    private EditText editUserName, editPhoneNumber;
    private Button btnSave, btnList, btnUpload, btnUploadList;
    ArrayList<Tree> treeArrayList;      // 나무정보들을 저장할 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSave = (Button) findViewById(R.id.BtnSave);
        btnList = (Button) findViewById(R.id.BtnList);
        btnUpload = (Button) findViewById(R.id.BtnUpload);
        btnUploadList = (Button) findViewById(R.id.BtnUploadList);

        editUserName = (EditText) findViewById(R.id.EditUserName);
        editPhoneNumber = (EditText)findViewById(R.id.EditPhoneNumber);

        //spinner
        Spinner spinnerAge = (Spinner)findViewById(R.id.SpinnerAge);
        spinnerAge.setSelection(0);
        ArrayAdapter adapterAge = ArrayAdapter.createFromResource(this,
                R.array.age, android.R.layout.simple_spinner_item);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAge.setPrompt("나이를 선택하세요.");
        spinnerAge.setAdapter(adapterAge);

        Button btnGenderMan = findViewById(R.id.BtnGenderMan);
        Button btnGenderWoman = findViewById(R.id.BtnGenderWoman);

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
                StrUserName = String.valueOf(editUserName.getText()).trim();
                StrAge = spinnerAge.getSelectedItem().toString();
                StrPhoneNumber = String.valueOf(editPhoneNumber.getText()).trim();


                String url = "https://androidserver-kw2.herokuapp.com/accountQuery.php/";
                String mode = "insert";

                if(StrUserName.length() == 0){
                    Toast.makeText(MainActivity.this, "이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                    editUserName.requestFocus();
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
                    editPhoneNumber.requestFocus();
                    return;
                }


                // AsyncTask를 통해 HttpURLConnection 수행.
                // 예를들어 로그인관련 POST 요청을한다.
                InsertLoginTask task = new InsertLoginTask();

                // R.string.url_1은 https://www.naver.com과 같은 특정사이트다.
                task.execute(url, "mode",mode
                        , "userName",StrUserName
                        , "gender",StrGender
                        , "age",StrAge
                        , "phoneNumber",StrPhoneNumber
                );
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

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUpload = new Intent(MainActivity.this, FileUploadActivity.class);
                startActivity(intentUpload);
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


    public class InsertLoginTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "InsertTask";

        public Context mContext;

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null)
                Toast.makeText(MainActivity.this, "등록실패", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(MainActivity.this, "등록성공", Toast.LENGTH_SHORT).show();
            }
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            // TODO : 아래 형식처럼 원하는 key과 value를 계속 추가시킬수있다.
            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            String key = (String) params[1];
            String value = (String) params[2];

            String key2 = (String) params[3];
            String value2 = (String) params[4];

            String key3 = (String) params[5];
            String value3 = (String) params[6];

            String key4 = (String) params[7];
            String value4 = (String) params[8];

            String key5 = (String) params[9];
            String value5 = (String) params[10];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = key + "=" + value +
                    "&" + key2 + "=" + value2
                    + "&" + key3 + "=" + value3
                    + "&" + key4 + "=" + value4
                    + "&" + key5 + "=" + value5;

            //String postParameters = "mode=insert&userName=이름&gender=남&age=29&phoneNumber=01047353592";

            Log.d(TAG, postParameters);

            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL); // 주소가 저장된 변수를 이곳에 입력합니다.

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.

                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.

                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8")); //전송할 데이터가 저장된 변수를 이곳에 입력합니다.

                outputStream.flush();
                outputStream.close();


                // 응답을 읽습니다.

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {

                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {

                    // 에러 발생

                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
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