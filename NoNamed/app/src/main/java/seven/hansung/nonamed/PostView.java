package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import seven.hansung.nonamed.utility.Comment_Info;
import seven.hansung.nonamed.utility.PostInfo;

public class PostView  extends AppCompatActivity {
    DatabaseReference commentreq;
    DatabaseReference postreq;
    protected TextView tx_view_board_name;
    protected TextView tx_view_post_no;
    protected TextView tx_view_post_owner;
    protected TextView tx_view_post_time;
    protected TextView tx_view_post_content;
    protected PostInfo test;
    protected LinearLayout layout_comment;
    protected LinearLayout comment_frame;
    protected TextView commentno;
    protected TextView commentcontent;
    protected TextView commentowner;
    protected EditText tx_comment_write;
    protected CheckBox comment_noname;
    protected Button bt_comment_ok;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    String postnum;
    String categoryname;
    String email;


    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);
        tx_view_board_name=findViewById(R.id.tx_view_board_name);
        tx_view_post_no=findViewById(R.id.tx_view_post_no);
        tx_view_post_owner=findViewById(R.id.tx_view_post_owner);
        tx_view_post_time=findViewById(R.id.tx_view_post_time);
        tx_view_post_content=findViewById(R.id.tx_view_post_content);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //2.googleAPI클라이언트 생성
        mSignInClient= GoogleSignIn.getClient(this,gso);
        //여기서는 주소만 가져옴 가입 x
        mAuth = FirebaseAuth.getInstance();
        //client로부터 인텐트를 불러와 엑티비티 시행->사용자의 데이터를 받아올수 있음
        //결과를 넘겨주면서 activity호출 코드값은 9001
        Intent signintent=mSignInClient.getSignInIntent();
        startActivityForResult(signintent,RC_SIGN_IN);
        Intent intent=getIntent();
        categoryname=intent.getStringExtra("categoryname");
        postnum=intent.getStringExtra("postnum");
        Toast.makeText(this,postnum,Toast.LENGTH_LONG).show();
        init();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
    }

    protected void  init(){
        postreq= FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
        //해당 글 띄우기
        postreq.orderByChild("postnum").equalTo(postnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PostInfo post=new PostInfo();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //작성자,번호,내용,익명여부,카테고리,시간,
                    post=snapshot.getValue(PostInfo.class);
                }
                if(post.getIsnoname().equals("true"))
                    tx_view_post_owner.setText("nonamed");
                else
                    tx_view_post_owner.setText(post.getPostowner());
                tx_view_post_no.setText(post.getPostnum());
                tx_view_post_time.setText(post.getCreattime());
                tx_view_post_content.setText(post.getContent());
                tx_view_board_name.setText(post.getPosttitle());
                tx_view_board_name.setFocusableInTouchMode(true);
                tx_view_board_name.requestFocus();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //코멘트 띄우기
        Intent intent=getIntent();
        categoryname=intent.getStringExtra("categoryname");
        postnum=intent.getStringExtra("postnum");
        commentreq=FirebaseDatabase.getInstance().getReference().child("comment").getRef();
        commentreq.orderByChild("postnum").equalTo(postnum).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comment_Info> commentlist=new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment_Info comment= snapshot.getValue(Comment_Info.class);
                    commentlist.add(comment);
                }
                layout_comment=findViewById(R.id.layout_comment);
                layout_comment.removeAllViews();
                LinearLayout.LayoutParams comment_frame_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams comment_no_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
                LinearLayout.LayoutParams comment_comment_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,5f);
                for(int i=0;i<commentlist.size();i++){
                    comment_frame=new LinearLayout(getApplicationContext());
                comment_frame.setLayoutParams(comment_frame_param);
                comment_frame.setOrientation(LinearLayout.HORIZONTAL);

                commentno=new TextView(getApplicationContext());
                commentno.setText(commentlist.get(i).getCreattime().substring(5,10));
                commentno.setTextSize(15);
                commentno.setGravity(View.TEXT_ALIGNMENT_CENTER);
                commentno.setLayoutParams(comment_no_param);

                commentcontent=new TextView(getApplicationContext());
                commentcontent.setText(commentlist.get(i).getContent());
                commentcontent.setTextSize(15);
                commentcontent.setGravity(View.TEXT_ALIGNMENT_CENTER);
                commentcontent.setLayoutParams(comment_comment_param);

                commentowner=new TextView(getApplicationContext());
                commentowner.setText(commentlist.get(i).getCommentowner());
                commentowner.setTextSize(15);
                commentowner.setGravity(View.TEXT_ALIGNMENT_CENTER);
                commentowner.setLayoutParams(comment_no_param);

                comment_frame.addView(commentno);
                comment_frame.addView(commentcontent);
                comment_frame.addView(commentowner);

                layout_comment.addView(comment_frame);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        /*
        //코멘트여러개 반복구간
        layout_comment=findViewById(R.id.layout_comment);

        LinearLayout.LayoutParams comment_no_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        LinearLayout.LayoutParams comment_comment_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,5f);

        comment_frame=new LinearLayout(getApplicationContext());
        comment_frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        comment_frame.setOrientation(LinearLayout.HORIZONTAL);

        commentno=new TextView(this);
        commentno.setText("no"+0+"");
        commentno.setTextSize(15);
        commentno.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentno.setLayoutParams(comment_no_param);

        commentcontent=new TextView(this);
        commentcontent.setText("글내용"+0+"");
        commentcontent.setTextSize(15);
        commentcontent.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentcontent.setLayoutParams(comment_comment_param);

        commentowner=new TextView(this);
        commentowner.setText("작성자"+0+"");
        commentowner.setTextSize(15);
        commentowner.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentowner.setLayoutParams(comment_no_param);

        comment_frame.addView(commentno);
        comment_frame.addView(commentcontent);
        comment_frame.addView(commentowner);

        layout_comment.addView(comment_frame);
        //여러개 반복구간끝
*/
        bt_comment_ok=findViewById(R.id.bt_comment_ok);
        bt_comment_ok.setOnClickListener(listen_comment_ok);

    }

    View.OnClickListener listen_comment_ok=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //username가져옴
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String commentuser="";
                    String commentowneruid="";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                       Object nick= snapshot.child("nickname").getValue(Object.class);
                        Object uid= snapshot.child("uid").getValue(Object.class);
                        commentuser=nick.toString();
                        commentowneruid=uid.toString();
                    }
                    //게시글넘버,익명여부,작성자,시간,내용을 저장함.
                    //일단 테스트 내용.익명여부만 체크
                    comment_noname=findViewById(R.id.comment_noname);
                    tx_comment_write=findViewById(R.id.tx_comment_write);
                    String noname;
                    if(comment_noname.isChecked())
                        noname="true";
                    else
                        noname="false";
                    //시간
                    // 현재시간을 msec 으로 구한다.
                    long now = System.currentTimeMillis();
                    // 현재시간을 date 변수에 저장한다.
                    Date date = new Date(now);
                    //                //타임존 설정
                    TimeZone time;
                    time = TimeZone.getTimeZone("Asia/Seoul");

                    // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
                    final SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    //타임존 세팅.
                    sdfNow.setTimeZone(time);
                    // nowDate 변수에 값을 저장한다.
                    String formatDate = sdfNow.format(date);
                    EditText tx_comment_write=findViewById(R.id.tx_comment_write);
                    //게시글 번호를 가져온다. 여기만 달면 되다가 안되다가 함.... 그래서 초기에도 하나 달아줌.
                    //여기에 등록. FirebaseDatabase.getInstance().getReference().child(categoryname).child(postnum).getRef()
                    Map<String,String> commenttValue=new HashMap<>();
                    // 코멘트 시간 코멘트 내용. 코멘트오너 코멘트익명여부 .
                    commenttValue.put("creattime",formatDate);
                    commenttValue.put("content",tx_comment_write.getText().toString());
                    commenttValue.put("commentowner",commentuser);
                    commenttValue.put("commentisnonamed",noname);
                    commenttValue.put("commentowneruid",commentowneruid);
                    commenttValue.put("postnum",postnum);

                    //push
                    commentreq= FirebaseDatabase.getInstance().getReference().child("comment").getRef();
                    String key= commentreq.push().getKey();
                    DatabaseReference keyref=commentreq.child(key);
                    keyref.setValue(commenttValue);
                    tx_comment_write.setText("");
                    Toast.makeText(getApplicationContext(),"등록완료",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //다시 뷰띄우기

        };
    };
    @Override//4.사용자 계정을 얻어온다.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                //사용자 계정을 얻어온다.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                email=account.getEmail();
                //account부터 접근이 가능한것.[접근만. 아직 우리쪽에 등록된것 아님.]
                //데이터 베이스 조회
               // Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
        else{}
    }
}