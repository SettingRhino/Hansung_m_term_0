package seven.hansung.nonamed.mypage;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import seven.hansung.nonamed.R;

public class MyPage_ChangeNickname extends AppCompatActivity {

    private boolean notover = false;

    TextView textview_want;
    Button button_over;
    Button button_ok;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    FirebaseUser currentUser;
    DatabaseReference database;
    DatabaseReference userref;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //액티비티 초기화시에 로그인 되어있는지 확인한다.
        currentUser = mAuth.getCurrentUser();
        //userref.getRoot()
    }

    @Override//4.사용자 계정을 얻어온다.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                //사용자 계정을 얻어온다.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //account부터 접근이 가능한것.[접근만. 아직 우리쪽에 등록된것 아님.]
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //ID토큰으로 AuthCredential 객체를 받음. 이객체는 바로 아래에 로그인 시킴-> 없으면 생성.[여기서 우리 파이어베이스에 등록됨]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //사용자를 Firebase 인증 시스템에 로그인(credential).성공유무를 알려주는 리스너
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //로그인한 유저의 FirebaseUser객체를 반환.
                            FirebaseUser user = mAuth.getCurrentUser();
                            //UI설정임
                            //updateFirebas(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            //updateFirebas(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_changenickname);
        Intent intent = getIntent();

        textview_want = findViewById(R.id.textview_want);
        button_ok = findViewById(R.id.button_ok);
        button_over = findViewById(R.id.button_over);

        button_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cur = textview_want.getText().toString();
                database = FirebaseDatabase.getInstance().getReference();
                userref = database.child("user").getRef();
                userref.orderByChild("nickname").equalTo(cur).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String overnick="";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Object nick= snapshot.child("nickname").getValue(Object.class);
                            overnick=nick.toString();
                        }
                        TextView v=findViewById(R.id.textview_want);
                        if(overnick.equals(v.getText().toString())){
                            Toast.makeText(getApplicationContext(),"중복",Toast.LENGTH_LONG).show();
                            notover = false;
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"중복X",Toast.LENGTH_LONG).show();
                            notover = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError){

                    }
                });
            }
        });


        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notover){
                    String cur = mAuth.getUid();
                    database = FirebaseDatabase.getInstance().getReference();
                    userref = database.child("user").getRef();
                    userref.orderByChild("uid").equalTo(cur).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Object nickname;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TextView v = findViewById(R.id.textview_want);
                                String wanted = v.getText().toString();
                                snapshot.child("nickname").getRef().setValue(wanted);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError){

                        }
                    });
                }
            }
        });

        //1.Google로그임옵션 객체를 만든다
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        //2.googleAPI클라이언트 생성
        mSignInClient= GoogleSignIn.getClient(this,gso);
        //이건 FirebaseAuth의 공유 인스턴스임.
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance().getReference();
        userref = database.child("user").getRef();
    }
}
