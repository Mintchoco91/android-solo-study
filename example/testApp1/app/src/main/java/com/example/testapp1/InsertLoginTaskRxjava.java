package com.example.testapp1;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class InsertLoginTaskRxjava {

    private final Context context;
    Disposable backgroundTask;

    public InsertLoginTaskRxjava (Context context) {
        this.context = context;
    }

    // backgroundTask를 실행하는 메소드
    public void sampleMethod(String... params) {
        // task에서 반환할 Hashmap
        HashMap<String, String> map = new HashMap<>();

        //onPreExecute(task 시작 전 실행될 코드 여기에 작성)
        backgroundTask = Observable.fromCallable(() -> {
            //doInBackground(task에서 실행할 코드 여기에 작성)
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

            String key6 = (String) params[11];
            String value6 = (String) params[12];

            String key7 = (String) params[13];
            String value7 = (String) params[14];

            String key8 = (String) params[15];
            String value8 = (String) params[16];

            String key9 = (String) params[17];
            String value9 = (String) params[18];

            String key10 = (String) params[19];
            String value10 = (String) params[20];

            String key11 = (String) params[21];
            String value11 = (String) params[22];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.

            // TODO : 위에 추가한 형식처럼 아래 postParameters에 key과 value를 계속 추가시키면 끝이다.
            // ex : String postParameters = "name=" + name + "&country=" + country;
            String postParameters = key + "=" + value +
                    "&" + key2 + "=" + value2
                    + "&" + key3 + "=" + value3
                    + "&" + key4 + "=" + value4
                    + "&" + key5 + "=" + value5
                    + "&" + key6 + "=" + value6
                    + "&" + key7 + "=" + value7
                    + "&" + key8 + "=" + value8
                    + "&" + key9 + "=" + value9
                    + "&" + key10 + "=" + value10
                    + "&" + key11 + "=" + value11;

            //String postParameters = "mode=insert&userName=이름&gender=남&age=29&phoneNumber=01047353592";

            Log.d("InsertTask", postParameters);

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
                Log.d("InsertTask", "POST response code - " + responseStatusCode);

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



                Toast.makeText(context, "등록 성공", Toast.LENGTH_LONG).show();
                return sb.toString();


            } catch (Exception e) {

                Log.d("InsertTask", "InsertData: Error ", e);
                Toast.makeText(context, "등록 실패", Toast.LENGTH_LONG).show();

                return new String("Error: " + e.getMessage());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }
}
