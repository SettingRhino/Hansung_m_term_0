package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import seven.hansung.nonamed.mypage.MyPage;

public class SelectMenu extends AppCompatActivity {
    protected Button bt_go_Board;
    protected Button bt_go_Chatting;
    protected Button bt_go_Mypage;
    Button test;
    String uid="";
    String email="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmenu);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        email=intent.getStringExtra("email");
        init();
    }
    protected void init(){
        bt_go_Board=findViewById(R.id.bt_go_Board);
        bt_go_Chatting=findViewById(R.id.bt_go_Chatting);
        bt_go_Mypage=findViewById(R.id.bt_go_Mypage);
        bt_go_Board.setOnClickListener(Listen_Board);
        bt_go_Mypage.setOnClickListener(Listen_Mypage);
        bt_go_Chatting.setOnClickListener(Listen_Chatting);
        test=findViewById(R.id.imagetest);
        test.setOnClickListener(tester);
    }
    View.OnClickListener Listen_Board=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,BoardCategory.class);
            intent.putExtra("uid",uid);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    };
    View.OnClickListener Listen_Mypage=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this, MyPage.class);
            intent.putExtra("uid",uid);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    };
    View.OnClickListener Listen_Chatting=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,Chatting_Main.class);
            intent.putExtra("uid",uid);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    };
    View.OnClickListener tester=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,Charcterselect.class);
            intent.putExtra("uid",uid);
            intent.putExtra("email",email);
            startActivity(intent);
        }
    };
}
