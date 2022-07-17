package com.example.testapp1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.bumptech.glide.load.resource.bitmap.BitmapImageDecoderResourceDecoder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileUploadActivity extends Activity {

    Button btnBack, btnRegist;
    private String strUserName, strGender, strAge, strPhoneNumber;
    private static final String TAG = "MainActivity";
    private String[] strFileNames = new String[6];
    private Uri[] uriImgPaths = new Uri[6];
    private StorageReference storageRef;

    private ImageView ivUserPicture0, ivUserPicture1, ivUserPicture2, ivUserPicture3, ivUserPicture4, ivUserPicture5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_activity);

        //main에서 전달 받은 데이터
        Intent intentMain = getIntent();
        strUserName = intentMain.getStringExtra("strUserName");
        strGender = intentMain.getStringExtra("strGender");
        strAge = intentMain.getStringExtra("strAge");
        strPhoneNumber = intentMain.getStringExtra("strPhoneNumber");

        btnBack = (Button) findViewById(R.id.file_upload_activity_btn_back);
        btnRegist = (Button) findViewById(R.id.file_upload_activity_btn_regist);

        //이미지 추가
        ivUserPicture0 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture0);
        ivUserPicture1 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture1);
        ivUserPicture2 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture2);
        ivUserPicture3 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture3);
        ivUserPicture4 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture4);
        ivUserPicture5 = (ImageView) findViewById(R.id.file_upload_activity_iv_user_picture5);

        //이미지 업로드 클릭
        ivUserPicture0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });

        //이미지 업로드 클릭
        ivUserPicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 1);
            }
        });

        //이미지 업로드 클릭
        ivUserPicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 2);
            }
        });

        //이미지 업로드 클릭
        ivUserPicture3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 3);
            }
        });

        //이미지 업로드 클릭
        ivUserPicture4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 4);
            }
        });

        //이미지 업로드 클릭
        ivUserPicture5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이미지를 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 5);
            }
        });

        //뒤로 가기
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //회원 등록(Firebase 이미지 업로드 -> DB회원 저장)
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB Update
                userRegistDB(strFileNames);
            }
        });
    }

    //파일 선택 후 결과 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // requestCode 몇번째 이미지인지 확인용
        int imgNo = requestCode;

        if(resultCode == RESULT_OK){
            uriImgPaths[imgNo] = data.getData();
            Log.d(TAG, "uri:" + String.valueOf(uriImgPaths[imgNo]));
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImgPaths[imgNo]);
                switch (imgNo) {
                    case 0:
                        ivUserPicture0.setImageBitmap(bitmap);
                        break;
                    case 1:
                        ivUserPicture1.setImageBitmap(bitmap);
                        break;
                    case 2:
                        ivUserPicture2.setImageBitmap(bitmap);
                        break;
                    case 3:
                        ivUserPicture3.setImageBitmap(bitmap);
                        break;
                    case 4:
                        ivUserPicture4.setImageBitmap(bitmap);
                        break;
                    case 5:
                        ivUserPicture5.setImageBitmap(bitmap);
                        break;
                }

                //fireBase Upload
                uploadFileAndRegistDB(uriImgPaths, imgNo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFileAndRegistDB(Uri[] uriImgPaths, Integer imgNo) {
        //업로드할 파일이 있으면 수행
        if (uriImgPaths[imgNo] != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseFirestore storage = FirebaseFirestore.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
            Date now = new Date();
            strFileNames[imgNo] = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            storageRef = FirebaseStorage.getInstance().getReference();
            storageRef = storageRef.child("photos").child(strFileNames[imgNo]);

            //업로드 시작
            storageRef.putFile(uriImgPaths[imgNo])
                    //성공시
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
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

    private void userRegistDB(String[] strFileNames){
        String url = "https://androidserver-kw2.herokuapp.com/accountQuery.php/";
        String mode = "insert";

        //빈 Array를 제거하기 위해 list로 변경
        List<String> lstFileName = new ArrayList<>(Arrays.asList(strFileNames));
        lstFileName.removeAll(Collections.singletonList(null));

        //빈배열 제거한 array
        String[] insertFileNames = new String[6];

        for(int i=0; i<6; i++){
            if(i < lstFileName.size()) {
                insertFileNames[i] = lstFileName.get(i);
            }else{
                insertFileNames[i] = null;
            }
        }

        // AsyncTask를 통해 HttpURLConnection 수행.
        // 예를들어 로그인관련 POST 요청을한다.
        Context context = getApplicationContext();
        InsertLoginTaskRxjava task = new InsertLoginTaskRxjava(context);

        task.sampleMethod(url, "mode",mode
                , "userName",strUserName
                , "gender",strGender
                , "age",strAge
                , "phoneNumber",strPhoneNumber
                , "fileName0",insertFileNames[0]
                , "fileName1",insertFileNames[1]
                , "fileName2",insertFileNames[2]
                , "fileName3",insertFileNames[3]
                , "fileName4",insertFileNames[4]
                , "fileName5",insertFileNames[5]
        );

        /*
        InsertLoginTask task = new InsertLoginTask(context);

        task.execute(url, "mode",mode
                , "userName",strUserName
                , "gender",strGender
                , "age",strAge
                , "phoneNumber",strPhoneNumber
                , "fileName0",insertFileNames[0]
                , "fileName1",insertFileNames[1]
                , "fileName2",insertFileNames[2]
                , "fileName3",insertFileNames[3]
                , "fileName4",insertFileNames[4]
                , "fileName5",insertFileNames[5]
        );

         */
    }
}
