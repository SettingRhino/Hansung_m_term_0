package seven.hansung.nonamed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;
    DatabaseReference userref;
    private FirebaseAuth mAuth;
    protected Button bt_Wcome;
    static String email="";
    Logincontroller logincontroller;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    private GoogleSignInClient mSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //테스트용
        database= FirebaseDatabase.getInstance().getReference();
        userref=database.child("user").getRef();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //액티비티 초기화시에 로그인 되어있는지 확인한다.
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
        init();
    }


    protected void init(){
        bt_Wcome=findViewById(R.id.bt_Wcome);
        bt_Wcome.setOnClickListener(Listen_Wellcome);
    }
    View.OnClickListener Listen_Wellcome= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent well=new Intent(MainActivity.this,WellCome.class);
            startActivity(well);
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
                //account부터 접근이 가능한것.[접근만. 아직 우리쪽에 등록된것 아님.]
                //데이터 베이스 조회
                database= FirebaseDatabase.getInstance().getReference();
                userref=database.child("user").getRef();
                userref.orderByChild("email").equalTo(account.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String fireemail="";
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Object str=snapshot.child("email").getValue(Object.class);
                            fireemail=str.toString();
                        }
                        if(fireemail.equals(email)){//등록된 회원이면
                            Toast.makeText(getApplicationContext(),"firebase조회:"+fireemail+"    현재사용자조회:"+email,Toast.LENGTH_LONG).show();

                            Intent intent=new Intent(MainActivity.this,SelectMenu.class);
                            //이제는 인텐트로 값 안넘겨줌
                            //intent.putExtra("username",login_id);
                            startActivity(intent);
                        }
                        else{
                            //등록되지 않았으면 가만히
                            Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"DD",Toast.LENGTH_LONG).show();
                    }
                });
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
}