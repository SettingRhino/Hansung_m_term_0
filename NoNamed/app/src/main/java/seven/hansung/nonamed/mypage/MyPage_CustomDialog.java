package seven.hansung.nonamed.mypage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
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

public class MyPage_CustomDialog extends AppCompatActivity {

    DatabaseReference database;
    DatabaseReference userref;
    static boolean isover=true;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleActivity";

    private Context context;

    public MyPage_CustomDialog(Context context) {
        this.context = context;
    }
    /*
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
    */
    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        mAuth = FirebaseAuth.getInstance();
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_nickname);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText txt_nickname = (EditText) dlg.findViewById(R.id.dialog_txt_nickname);
        final Button checkButton = dlg.findViewById(R.id.dialog_btn_check);
        final Button okButton = (Button) dlg.findViewById(R.id.dialog_btn_confirm);
        final Button cancelButton = (Button) dlg.findViewById(R.id.dialog_btn_cancel);

        checkButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String nickname=txt_nickname.getText().toString();
                database = FirebaseDatabase.getInstance().getReference();
                userref = database.child("user").getRef();
                if(nickname.length()>20||nickname.length()<1)//글자가 1이하거나 20이상이면 안됨
                {
                    Toast.makeText(context,"글자수는 1~20사이로 설정해주세요.",Toast.LENGTH_LONG).show();
                }else {
                    userref.orderByChild("nickname").equalTo(nickname).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String overnick = "";
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Object nick = snapshot.child("nickname").getValue(Object.class);
                                overnick = nick.toString();
                            }
                            TextView v = dlg.findViewById(R.id.dialog_txt_nickname);
                            if (overnick.equals(v.getText().toString())) {
                                Toast.makeText(context, "중복", Toast.LENGTH_LONG).show();
                                isover = true;
                            } else {
                                Toast.makeText(context, "중복X", Toast.LENGTH_LONG).show();
                                isover = false;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isover){
                    String cur = mAuth.getUid();
                    database = FirebaseDatabase.getInstance().getReference();
                    userref = database.child("user").getRef();
                    userref.orderByChild("uid").equalTo(cur).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Object nickname;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                TextView v = dlg.findViewById(R.id.dialog_txt_nickname);
                                String wanted = v.getText().toString();
                                snapshot.child("nickname").getRef().setValue(wanted);
                                dlg.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError){

                        }
                    });
                    dlg.dismiss();
                }
                else{
                    Toast.makeText(context, "중복검사 필요", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
