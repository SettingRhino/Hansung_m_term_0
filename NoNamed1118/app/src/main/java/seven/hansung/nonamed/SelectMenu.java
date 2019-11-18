package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectMenu extends AppCompatActivity {
    protected Button bt_go_Board;
    protected Button bt_go_Chatting;
    protected Button bt_go_Mypage;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmenu);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
        init();
    }
    protected void init(){
        bt_go_Board=findViewById(R.id.bt_go_Board);
        bt_go_Chatting=findViewById(R.id.bt_go_Chatting);
        bt_go_Mypage=findViewById(R.id.bt_go_Mypage);
        bt_go_Board.setOnClickListener(Listen_Board);
        bt_go_Mypage.setOnClickListener(Listen_Mypage);
        bt_go_Chatting.setOnClickListener(Listen_Chatting);
    }
    View.OnClickListener Listen_Board=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,BoardCategory.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
    };
    View.OnClickListener Listen_Mypage=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,MyPage.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
    };
    View.OnClickListener Listen_Chatting=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(SelectMenu.this,Chatting_Main.class);
            intent.putExtra("username",username);
            startActivity(intent);
        }
    };
}
