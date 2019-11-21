package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

public class PostWrite extends AppCompatActivity {
    DatabaseReference database;
    DatabaseReference mcategoryRef;
    DatabaseReference boardref;
    DatabaseReference categoryref;
    DatabaseReference userref;
    DatabaseReference modifypostreq;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";
    private GoogleSignInClient mSignInClient;
    private FirebaseAuth mAuth;
    static String email="";
    static String uid="";
    static String categoryname="";
    protected Spinner spinner;
    protected ArrayAdapter<CharSequence> spinerAdapter;
    protected EditText tx_write_title;
    protected EditText tx_write_content;
    protected CheckBox isnoname;
    protected Button bt_write_ok;
    ArrayList<String> spinner_arr;
    String tmp_share;


    int boarn;
    String username;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.post_write);
            database = FirebaseDatabase.getInstance().getReference();
            mcategoryRef = database.child("category");
            boardref= database.child("board").getRef();
            categoryref=boardref.child(categoryname).getRef();
        }
    @Override
    public void onStart() {
        super.onStart();
        //1.Google로그임옵션 객체를 만든다
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
        //데이터 구성시 사용
        categoryname=intent.getStringExtra("categoryname");
        init();

    }
    @Override
    public void onBackPressed(){
            Intent intent = new Intent(getApplicationContext(),Board_0.class);
            intent.putExtra("categoryname",categoryname);
        startActivity(intent);
    }

        protected void init(){
            categoryref=boardref.child(categoryname).getRef();
            //카테고리 목록을 한번만 가져온다.
            mcategoryRef.addListenerForSingleValueEvent(categoryspinner);
            //게시글 번호를 가져온다. 이상하게 버튼에만 달면 가끔 안된다... 보험용으로 하나 더 단다.
            categoryref.addListenerForSingleValueEvent(getboardnum);

            tx_write_title=findViewById(R.id.tx_write_title);
            tx_write_content=findViewById(R.id.tx_write_content);
            isnoname=findViewById(R.id.isnoname);
            bt_write_ok=findViewById(R.id.bt_write_ok);
            bt_write_ok.setOnClickListener(listen_ok);

            Intent intent=getIntent();
            int num=intent.getIntExtra("postmodifynum",-1);
            Boolean modifypost=intent.getBooleanExtra("postmodify",false);
            categoryname=intent.getStringExtra("categoryname");
            if(modifypost&&num!=-1){
                //있는 내용 띄우기->재작성->데이터베이스 수정[버튼리스너 다시.]
                modifypostreq=FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
                modifypostreq.orderByChild("postnum").equalTo(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Object modifycontent=snapshot.child("content").getValue(Object.class);
                            Object modifyposttitle=snapshot.child("posttitle").getValue(Object.class);
                            Toast.makeText(getApplicationContext(),modifycontent.toString(),Toast.LENGTH_LONG).show();
                            TextView modifycontenttxt=findViewById(R.id.tx_write_content);
                            TextView modifytitletxt=findViewById(R.id.tx_write_title);
                            modifycontenttxt.setText(modifycontent.toString());
                            modifytitletxt.setText(modifyposttitle.toString());
                        }
                        bt_write_ok=findViewById(R.id.bt_write_ok);
                        bt_write_ok.setOnClickListener(modify_ok);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        }
        View.OnClickListener modify_ok=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                int num=intent.getIntExtra("postmodifynum",-1);
                modifypostreq=FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
                modifypostreq.orderByChild("postnum").equalTo(Integer.toString(num)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            TextView modifycontenttxt=findViewById(R.id.tx_write_content);
                            TextView modifytitletxt=findViewById(R.id.tx_write_title);
                            snapshot.child("content").getRef().setValue(modifycontenttxt.getText().toString());
                            snapshot.child("posttitle").getRef().setValue(modifytitletxt.getText().toString());
                        }
                        Intent boardintent=new Intent(getApplicationContext(),Board_0.class);
                        boardintent.putExtra("categoryname",categoryname);
                        boardintent.putExtra("modifyOK",true);
                        startActivity(boardintent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        View.OnClickListener listen_ok=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userref=database.child("user").getRef();
                userref.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                           Object usernickname= snapshot.child("nickname").getValue(Object.class);
                            username=usernickname.toString();
                            uid=snapshot.child("uid").getValue(Object.class).toString();
                        }
                        categoryref=boardref.child(categoryname).getRef();

                        categoryref.addListenerForSingleValueEvent(getboardnum);
                        if(isnoname.isChecked()){
                            tmp_share="true";
                        }
                        else
                            tmp_share="false";
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
                        //게시글 번호를 가져온다. 여기만 달면 되다가 안되다가 함.... 그래서 초기에도 하나 달아줌.

                        Map<String,String> postValue=new HashMap<>();
                        postValue.put("category",categoryname);
                        postValue.put("postnum",Integer.toString(boarn+1));
                        postValue.put("posttitle",tx_write_title.getText().toString());
                        postValue.put("postowner",username);
                        postValue.put("postowneruid",uid);
                        postValue.put("isnoname",tmp_share);
                        postValue.put("creattime",formatDate);
                        postValue.put("content",tx_write_content.getText().toString());


                        //push
                        categoryref=boardref.child(categoryname);
                        String key= categoryref.push().getKey();
                        DatabaseReference keyref=categoryref.child(key);
                        keyref.setValue(postValue);

                        Intent boardintent=new Intent(getApplicationContext(),Board_0.class);
                        boardintent.putExtra("categoryname",categoryname);
                        boardintent.putExtra("postOK",true);
                        startActivity(boardintent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //업로드할 데이터를 구성한다.
            }
        };
        //스피너 선택시.
    AdapterView.OnItemSelectedListener Spinner_select=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //데이터 구성시 사용
            categoryname = spinner_arr.get(position);

        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    //게시글의 번호를 부여한다.
    ValueEventListener getboardnum=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Integer> arr=new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Object tmp= snapshot.child("postnum").getValue(Object.class);
                arr.add(Integer.parseInt(tmp.toString()));
            }
            int max=0;
            for(int i=0;i<arr.size();i++){
                if(arr.get(i)>max)
                    max=arr.get(i);
            }
            boarn=max;
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    //카테고리 목록을 불러와 스피너에 연결
    ValueEventListener categoryspinner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            spinner_arr=new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Object str=snapshot.getValue(Object.class);
                spinner_arr.add(str.toString());
            }
            spinner = findViewById(R.id.post_select_board);
            spinerAdapter=new ArrayAdapter(getApplicationContext(),R.layout.spinnerlayout,spinner_arr);

            spinner.setAdapter(spinerAdapter);
            spinner.setOnItemSelectedListener( Spinner_select);
            for(int i=0;i<spinner_arr.size();i++){
                if(spinner_arr.get(i).equals(categoryname))
                    spinner.setSelection(i);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
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
                uid=account.getId();
                //account부터 접근이 가능한것.
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
}