package kr.co.tjeit.lecturemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.tjeit.lecturemanager.util.ContextUtil;
import kr.co.tjeit.lecturemanager.util.ServerUtil;

public class MyProfileActivity extends BaseActivity {

    final int GALLERY = 1;

    private de.hdodenhof.circleimageview.CircleImageView profileImg;
    private android.widget.TextView nameTxt;
    private Button editProfileBtn;
    private TextView userIdTxt;
    private TextView phoneTxt;

//    아이디, 폰번 표시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        bindViews();
        setupEvents();
        setValues();
    }

    @Override
    public void setupEvents() {

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditMyProfileActivity.class);
                startActivity(intent);
            }
        });
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setType("image/+");
                myIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(myIntent, GALLERY);
                // 일단은 갤러리가 뜬다
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY){
            if (resultCode == RESULT_OK){
                // 서버에 프로필 사진 전송, 후처리
                // 사진 전송 => Bitmap을 따서 서버에 보낸다.

                // 1. Butmap 얻어오기
                Uri uri = data.getData();
                // 어떤 데이터형식으로 가져왔니를 얻어옴

                // 2. 비트맵 따기
                // 에러 => try/catch
                try {
                    final Bitmap myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    ServerUtil.updateProfilePhoto(mContext, ContextUtil.getLoginUser(mContext).getId() + "",
                            myBitmap, new ServerUtil.JsonResponseHandler() {
                                @Override
                                public void onResponse(JSONObject json) {
                                    Toast.makeText(mContext, "서버에 이미지파일 업로드 완료", Toast.LENGTH_SHORT).show();
                                    profileImg.setImageBitmap(myBitmap);
                                }
                            });
               
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setValues() {

//        페이스북의 Graph API 이용해서, 더 많은 정보를 불러오기.

//        GraphRequest request = GraphRequest.newMeRequest(
//                AccessToken.getCurrentAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(final JSONObject object, GraphResponse response) {
//
//                        Log.d("사용자정보", object.toString());
//
////                      페이스북 페이지 방문 버튼을 누르면
//                        linkBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
////                       이 사람의 페이지로 방문할 수 있도록 설정.
////                                Intent 기능 활용
//
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_VIEW);
//                                try {
//                                    String link = object.getString("link");
//
//                                    intent.setData(Uri.parse(link));
//                                    startActivity(intent);
//
//
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        });
//
////                        성별 표시 기능.
//
//
//                        String gender = null;
//                        try {
//                            gender = object.getString("gender");
//
//                            if (gender.equals("male")) {
//                                genderTxt.setText("남성");
//                            }
//                            else {
//                                genderTxt.setText("여성");
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }
//        );
//        Bundle params = new Bundle();
//        params.putString("fields", "id,name,gender,link");
//        request.setParameters(params);
//        request.executeAsync();

        phoneTxt.setText(ContextUtil.getLoginUser(mContext).getPhoneNum());

        userIdTxt.setText(ContextUtil.getLoginUser(mContext).getUserId());

        nameTxt.setText(ContextUtil.getLoginUser(mContext).getName());

        Glide.with(mContext).load(ContextUtil.getLoginUser(mContext).getProfileURL()).into(profileImg);
    }

    @Override
    public void bindViews() {
        this.editProfileBtn = (Button) findViewById(R.id.editProfileBtn);
        this.phoneTxt = (TextView) findViewById(R.id.phoneTxt);
        this.userIdTxt = (TextView) findViewById(R.id.userIdTxt);
        this.nameTxt = (TextView) findViewById(R.id.nameTxt);
        this.profileImg = (CircleImageView) findViewById(R.id.profileImg);
    }
}
