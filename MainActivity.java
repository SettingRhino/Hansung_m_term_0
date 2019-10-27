package seven.hansung.nonamed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected TextView tx_id_Info;
    protected TextView tx_pw_Info;
    protected Button bt_Login;
    protected Button bt_Wcome;
    protected Button bt_Pwch;
    public UserInfo LoginUser;
    public UserInfo tmpUser;
    Logincontroller logincontroller;
    String login_id;
    String login_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tx_id_Info=findViewById(R.id.tx_id_Info);
        tx_pw_Info=findViewById(R.id.tx_pw_Info);
        bt_Login=findViewById(R.id.bt_Login);
        bt_Wcome=findViewById(R.id.bt_Wcome);
        bt_Pwch=findViewById(R.id.bt_Pwch);
        bt_Login.setOnClickListener(Listen_Login);
        bt_Wcome.setOnClickListener(Listen_Wellcome);
        bt_Pwch.setOnClickListener(Listen_Pwchange);
        //테스트용
        tmpUser=new UserInfo("root","root");

    }
    View.OnClickListener Listen_Pwchange=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,PwChange.class));
        }
    };
    View.OnClickListener Listen_Wellcome= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent well=new Intent(MainActivity.this,WellCome.class);
            startActivity(well);
        }
    };
    View.OnClickListener Listen_Login=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            login_id=tx_id_Info.getText().toString();
            login_pw=tx_pw_Info.getText().toString();
            LoginUser=new UserInfo(login_id,login_pw);
            //tmpLogin값은 파이어 베이스에서 가져옴(지금은 임시로 설정.)
            logincontroller=new Logincontroller(LoginUser,tmpUser);
            if(logincontroller.checkLogin()==0)
                Toast.makeText(getApplicationContext(),"로그인 실패:아이디가 존재하지 않습니다.",Toast.LENGTH_LONG).show();
            if(logincontroller.checkLogin()==2)
                Toast.makeText(getApplicationContext(),"로그인 실패:비밀번호가 틀립니다.",Toast.LENGTH_LONG).show();
            if(logincontroller.checkLogin()==1)//로그인 성공으로 페이지 이동.
                startActivity(new Intent(MainActivity.this,SelectMenu.class));
        }
    };
}
