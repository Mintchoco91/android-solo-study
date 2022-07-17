package com.example.testapp1;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class FileUploadListActivity extends Activity {
    private ImageView ivArea;
    private Button btImageLoad, btDirectoryImageLoad;
    private TextView tvName, tvGender, tvAge, tvPhoneNumber;

    // 이미지 폴더 경로 참조
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_upload_list_activity);

        ivArea = (ImageView) findViewById(R.id.file_upload_list_activity_iv_area);
        btImageLoad = (Button) findViewById(R.id.bt_image_load);
        btDirectoryImageLoad = (Button) findViewById(R.id.bt_directory_image_load);

        tvName = (TextView) findViewById(R.id.file_upload_list_activity_tv_name);
        tvGender = (TextView) findViewById(R.id.file_upload_list_activity_tv_gender);
        tvAge = (TextView) findViewById(R.id.file_upload_list_activity_tv_age);
        tvPhoneNumber = (TextView) findViewById(R.id.file_upload_list_activity_tv_phone_number);

        storageRef = FirebaseStorage.getInstance().getReference();

        btImageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePhoto();
            }
        });

        btDirectoryImageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAllPhoto();
            }
        });

    }

    private void singlePhoto(){
        storageRef.child("photos/20220600_5710.png").getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(ivArea);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //이미지 로드 실패시
                Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //폴더에 있는 이미지 전부 가져와서 동적으로 뿌려준다.
    private void loadAllPhoto(){
        storageRef.child("photos").listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        int i = 1;
                        // 폴더 내의 item이 동날 때까지 모두 가져온다.
                        for (StorageReference item : listResult.getItems()) {

                            // imageview와 textview를 생성할 레이아웃 id 받아오기
                            LinearLayout layout = (LinearLayout) findViewById(R.id.maskImageLayout);
                            // textview 동적생성
                            TextView tv = new TextView(FileUploadListActivity.this);
                            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            tv.setText(Integer.toString(i)+"번 이미지 / 파일명 : " + item.getName());
                            tv.setTextSize(30);
                            tv.setTextColor(0xff004497);
                            layout.addView(tv);

                            //imageview 동적생성
                            ImageView iv = new ImageView(FileUploadListActivity.this);
                            iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            layout.addView(iv);
                            i++; // 구현에는 의미 없는 코드.. 내 프로젝트에만 필요함

                            // reference의 item(이미지) url 받아오기
                            item.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        // Glide 이용하여 이미지뷰에 로딩
                                        Glide.with(FileUploadListActivity.this)
                                                .load(task.getResult())
                                                .into(iv);
                                    } else {
                                        // URL을 가져오지 못하면 토스트 메세지
                                        Toast.makeText(FileUploadListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Uh-oh, an error occurred!
                                    Toast.makeText(FileUploadListActivity.this, "File Get Error!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Toast.makeText(FileUploadListActivity.this, "File Get Error!!2", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
