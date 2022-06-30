package com.example.testapp1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUploadActivity extends Activity {

    Button btnBack, btnChoose, btnRegist;
    private ImageView ivPreview;
    private String StrUserName, StrGender, StrAge, StrPhoneNumber;
    private static final String TAG = "MainActivity";
    private Uri filePath;
    private StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);

        //main에서 전달 받은 데이터
        Intent intentMain = getIntent();
        StrUserName = intentMain.getStringExtra("StrUserName");
        StrGender = intentMain.getStringExtra("StrGender");
        StrAge = intentMain.getStringExtra("StrAge");
        StrPhoneNumber = intentMain.getStringExtra("StrPhoneNumber");

        btnBack = (Button) findViewById(R.id.file_upload_activity_btn_back);
        btnChoose = (Button) findViewById(R.id.file_upload_activity_btn_choose);
        btnRegist = (Button) findViewById(R.id.file_upload_activity_btn_regist);

        ivPreview = (ImageView) findViewById(R.id.file_upload_activity_iv_preview);
        storageRef = FirebaseStorage.getInstance().getReference();

        //뒤로 가기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //이미지 선택
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //회원 등록(Firebase 이미지 업로드 -> DB회원 저장)
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //업로드
                uploadFileAndRegistDB();
            }
        });
    }

    //결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if(requestCode == 0 && resultCode == RESULT_OK){
            filePath = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(filePath));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivPreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFileAndRegistDB() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseFirestore storage = FirebaseFirestore.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            String StrFileName = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            storageRef = storageRef.child("photos").child(StrFileName);

            //업로드 시작
            storageRef.putFile(filePath)
                    //성공시
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기

                            //DB Update
                            userRegistDB(StrFileName);
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                            double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void userRegistDB(String StrFileName){
        String url = "https://androidserver-kw2.herokuapp.com/accountQuery.php/";
        String mode = "insert";

        // AsyncTask를 통해 HttpURLConnection 수행.
        // 예를들어 로그인관련 POST 요청을한다.
        Context context = getApplicationContext();
        InsertLoginTask task = new InsertLoginTask(context);

        task.execute(url, "mode",mode
                , "userName",StrUserName
                , "gender",StrGender
                , "age",StrAge
                , "phoneNumber",StrPhoneNumber
                , "fileName",StrFileName
        );
    }
}
