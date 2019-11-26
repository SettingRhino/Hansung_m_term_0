package seven.hansung.nonamed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    protected LinearLayout layout_comment;
    protected LinearLayout comment_frame;
    protected TextView commentno;
    protected TextView commentcontent;
    protected TextView commentowner;
    protected EditText tx_comment_write;
    protected CheckBox comment_noname;
    protected Button bt_comment_ok;
    Button postremovebtn;
    Button postmodifybtn;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    String postnum;
    String categoryname;
    String postowner;
    String email;
    String useruid;
    ArrayList<TextView>commentownerlist;
    ArrayList<LinearLayout> comment_frame_list;
    ArrayList<String> comment_content_list;
    ArrayList<String> comment_no_list;
    ArrayList<String>comment_owneruid_list;
    PostInfo post;
    String modifycommentowneruid;
    String modifycommentcreattime;
    String modifycommentcontent;
    String usernickname;
    int commentcount;
    ArrayList<Comment_Info> commentlist;
    ArrayList<String> comment_real_owner;
    LinearLayout.LayoutParams comment_frame_param;
    LinearLayout.LayoutParams comment_no_param;
    ViewGroup.LayoutParams lp;
    LinearLayout.LayoutParams comment_comment_param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);
        tx_view_board_name=findViewById(R.id.tx_view_board_name);
        tx_view_post_no=findViewById(R.id.tx_view_post_no);
        tx_view_post_owner=findViewById(R.id.tx_view_post_owner);
        tx_view_post_time=findViewById(R.id.tx_view_post_time);
        tx_view_post_content=findViewById(R.id.tx_view_post_content);
        postremovebtn=findViewById(R.id.postremovebtn);
        postremovebtn.setOnClickListener(removepost);
        postmodifybtn=findViewById(R.id.postmodifybtn);
        postmodifybtn.setOnClickListener(modifypost);
        Intent intent=getIntent();
        categoryname=intent.getStringExtra("categoryname");
        postnum=intent.getStringExtra("postnum");
        postowner=intent.getStringExtra("postowner");
        email=intent.getStringExtra("email");
        useruid=intent.getStringExtra("uid");
        Toast.makeText(this,useruid,Toast.LENGTH_SHORT).show();
        comment_frame_list=new ArrayList<>();
        comment_no_list=new ArrayList<>();
        comment_content_list=new ArrayList<>();
        comment_owneruid_list=new ArrayList<>();
        commentownerlist=new ArrayList<>();
        comment_real_owner=new ArrayList<>();
        modifycommentowneruid="";
        modifycommentcreattime="";
        modifycommentcontent="";
        usernickname="";
        comment_frame_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        comment_no_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        comment_comment_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,5f);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //제거시 필요한 동작- 포스트 삭제, 덧글 모두 삭제, 후 게시판으로
        //  Toast.makeText(this,postnum,Toast.LENGTH_LONG).show();
        //롱클릭으로 수정 삭제 기능 추가할 예정.

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(getApplicationContext(),email,Toast.LENGTH_LONG).show();
    }

    protected void  init(){
        Intent intent=getIntent();
        categoryname=intent.getStringExtra("categoryname");
        postnum=intent.getStringExtra("postnum");
        postreq= FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
        //해당 글 띄우기
        postreq.orderByChild("postnum").equalTo(postnum).addListenerForSingleValueEvent(getpost);
        //코멘트 띄우기
        commentreq=FirebaseDatabase.getInstance().getReference().child("comment").getRef();
        commentreq.orderByChild("postnum").equalTo(postnum).addValueEventListener(getcommentlist);

        bt_comment_ok=findViewById(R.id.bt_comment_ok);
        bt_comment_ok.setOnClickListener(listen_comment_ok);

    }

    View.OnClickListener listen_comment_ok=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           //comment push
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(pushcomment);
        }
    };
    ValueEventListener pushcomment=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            comment_noname=findViewById(R.id.comment_noname);
            tx_comment_write=findViewById(R.id.tx_comment_write);
            String commentuser="";
            String commentowneruid="";
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Object nick= snapshot.child("nickname").getValue(Object.class);
                Object uid= snapshot.child("uid").getValue(Object.class);
                commentuser=nick.toString();
                commentowneruid=uid.toString();
                useruid=uid.toString();


            }
            //게시글넘버,익명여부,작성자,시간,내용을 저장함.
            //일단 테스트 내용.익명여부만 체크

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
            //commenttValue.put("commentowner",commentuser);
            commenttValue.put("commentisnonamed",noname);
            commenttValue.put("commentowneruid",commentowneruid);
            commenttValue.put("category",getIntent().getStringExtra("categoryname"));
            commenttValue.put("postnum",postnum);

            //push
            commentreq= FirebaseDatabase.getInstance().getReference().child("comment").getRef();
            String key= commentreq.push().getKey();
            DatabaseReference keyref=commentreq.child(key);
            keyref.setValue(commenttValue);
            tx_comment_write.setText("");

            Toast.makeText(getApplicationContext(),"등록완료",Toast.LENGTH_SHORT).show();

            Intent intent=getIntent();
            categoryname=intent.getStringExtra("categoryname");
            postnum=intent.getStringExtra("postnum");
            postowner=intent.getStringExtra("postowner");
            email=intent.getStringExtra("email");
            useruid=intent.getStringExtra("uid");
            Intent re=new Intent(PostView.this,PostView.class);
            re.putExtra("categoryname",categoryname);
            re.putExtra("postnum",postnum);
            re.putExtra("postowner",postowner);
            re.putExtra("email",email);
            re.putExtra("uid",useruid);

            startActivity(new Intent(re));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    ValueEventListener getpost=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                //작성자,번호,내용,익명여부,카테고리,시간,
                post=snapshot.getValue(PostInfo.class);
            }
            if(post.getIsnoname().equals("true"))
                tx_view_post_owner.setText("noname");
            else
                tx_view_post_owner.setText(postowner);
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
    };
    //코멘트 가져오기
    ValueEventListener getcommentlist=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            commentlist=new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(snapshot.child("category").getValue(Object.class).toString().equals(categoryname)){
                    Comment_Info comment= snapshot.getValue(Comment_Info.class);
                    commentlist.add(comment);
                }
            }
            layout_comment=findViewById(R.id.layout_comment);
            layout_comment.removeAllViews();

            commentcount=commentlist.size();

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
                if(commentlist.get(i).getCommentisnonamed().equals("true")){
                    commentowner.setText("noname");
                }
                else{
                    if(comment_real_owner.size()>i){
                        commentowner.setText(comment_real_owner.get(i));}
                    else
                        commentowner.setText("");
                }
                commentowner.setTextSize(15);
                commentowner.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
                commentowner.setLayoutParams(comment_no_param);
                commentownerlist.add(commentowner);

                comment_frame.addView(commentno);
                comment_frame.addView(commentcontent);
                comment_frame.addView(commentowner);
                comment_frame.setOnLongClickListener(commentmodifylistener);
                comment_no_list.add(commentlist.get(i).getCreattime());
                comment_content_list.add(commentcontent.getText().toString());
                comment_frame_list.add(comment_frame);
                comment_owneruid_list.add(commentowner.getText().toString());


                layout_comment.addView(comment_frame);

            }
            FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    comment_real_owner=new ArrayList<>();
                    for(int x=0;x<commentlist.size();x++){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(commentlist.get(x).getCommentowneruid().equals(snapshot.child("uid").getValue(Object.class).toString())){
                                comment_real_owner.add(snapshot.child("nickname").getValue(Object.class).toString());
                                if(commentlist.get(x).getCommentisnonamed().equals("true")){
                                    commentownerlist.get(x).setText("noname");
                                }
                                else{
                                    commentownerlist.get(x).setText(snapshot.child("nickname").getValue(Object.class).toString());
                                }
                            }

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener removepost_comment=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                usernickname=snapshot.child("nickname").getValue(Object.class).toString();
                useruid=snapshot.child("uid").getValue(Object.class).toString();
            }
            //삭제권한이 있는경우
            if(useruid.equals(post.getPostowneruid())){
                //포스트 넘버랑 카테고리 이중 확인
                TextView numtx=findViewById(R.id.tx_view_post_no);
                Intent intent=getIntent();
                categoryname=intent.getStringExtra("categoryname");
                String num=numtx.getText().toString();
                //
                FirebaseDatabase.getInstance().getReference().child("comment").orderByChild("postnum").equalTo(num).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //comment삭제
                FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).orderByChild("postnum").equalTo(num).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent backintent=new Intent(getApplicationContext(),Board_0.class);
                backintent.putExtra("postowner", postowner);
                backintent.putExtra("categoryname", categoryname);
                backintent.putExtra("postnum",postnum);
                backintent.putExtra("email",email);
                backintent.putExtra("uid",useruid);
                startActivity(backintent);
            }
            else{
                Toast.makeText(getApplicationContext(),"글삭제권한이 없습니다!",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    View.OnClickListener modifypost=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        usernickname=snapshot.child("nickname").getValue(Object.class).toString();
                        //useruid=snapshot.child("uid").getValue(Object.class).toString();
                    }
                    if(useruid.equals(post.getPostowneruid())){
                        Intent modifyintent=new Intent(getApplicationContext(),PostWrite.class);

                        modifyintent.putExtra("postowner", postowner);
                        modifyintent.putExtra("categoryname", categoryname);
                        modifyintent.putExtra("postnum",postnum);
                        modifyintent.putExtra("email",email);
                        modifyintent.putExtra("uid",useruid);
                        modifyintent.putExtra("postmodify",true);

                        startActivity(modifyintent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"글수정권한이 없습니다!",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    };

    View.OnClickListener removepost=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(PostView.this);
            builder.setTitle("게시글 삭제")        // 제목 설정
                    .setMessage("해당 게시글을 삭제하시겠습니까?")        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        // 확인 버튼 클릭시 설정, 오른쪽 버튼
                        public void onClick(DialogInterface dialog, int whichButton){
                            //이벤트
                            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(removepost_comment);                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        // 취소 버튼 클릭시 설정, 왼쪽 버튼입니다.
                        public void onClick(DialogInterface dialog, int whichButton){
                            //이벤트
                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();

        }
    };
    View.OnLongClickListener commentmodifylistener=new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            for(int f=0;f<comment_frame_list.size();f++){
                if(v.equals(comment_frame_list.get(f))){
                    Toast.makeText(getApplicationContext(),""+comment_no_list.get(f)+""+comment_owneruid_list.get(f),Toast.LENGTH_SHORT).show();
                    modifycommentowneruid=comment_owneruid_list.get(f);
                    modifycommentcreattime=comment_no_list.get(f);
                    modifycommentcontent=comment_content_list.get(f);
                    FirebaseDatabase.getInstance().getReference().child("comment").orderByChild("creattime").equalTo(comment_no_list.get(f)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                //만들어진 시간이랑 소유자가 맞으면(왜? 같은 사람이 1초안에 여러개를 올리긴 힘드니까 확률이 적음)
                                if(snapshot.child("commentowneruid").getValue(Object.class).toString().equals(modifycommentowneruid)){
                                    modifycommentcontent=snapshot.child("content").getValue(Object.class).toString();
                                }
                            }
                            //텍스트 있는 다이얼로그
                            AlertDialog.Builder ad = new AlertDialog.Builder(PostView.this);
                            ad.setTitle("덧글 수정하기");       // 제목 설정
                            ad.setMessage("수정할 내용:");   // 내용 설정
                            // EditText 삽입하기
                            final EditText et = new EditText(PostView.this);
                            et.setText(modifycommentcontent);
                            ad.setView(et);
                            // 확인 버튼 설정
                            ad.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v(TAG, "확인클릭");
                                    String value = et.getText().toString();
                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                        if(snapshot2.child("commentowneruid").getValue(Object.class).toString().equals(useruid))
                                            snapshot2.child("content").getRef().setValue(value);
                                        else{
                                            Toast.makeText(getApplicationContext(),"수정권한이 없습니다!"+useruid,Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    dialog.dismiss();     //닫기
                                }
                            });
                            // 삭제버튼 설정
                            ad.setNeutralButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v(TAG,"삭제클릭");
                                    for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {
                                        if(snapshot2.child("commentowneruid").getValue(Object.class).toString().equals(useruid)){
                                            snapshot2.getRef().setValue(null);
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"삭제권한이 없습니다!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    dialog.dismiss();     //닫기
                                }
                            });
                            // 취소 버튼 설정
                            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.v(TAG,"취소클릭");
                                    dialog.dismiss();     //닫기
                                }
                            });
                            // 창 띄우기
                            ad.show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            return false;
        }
    };
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),Board_0.class);
        intent.putExtra("postowner", postowner);
        intent.putExtra("categoryname", categoryname);
        intent.putExtra("postnum",postnum);
        intent.putExtra("email",email);
        intent.putExtra("uid",useruid);
        startActivity(intent);
    }

}