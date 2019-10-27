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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmenu);
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
            startActivity(new Intent(SelectMenu.this,BoardCategory.class));
        }
    };
    View.OnClickListener Listen_Mypage=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SelectMenu.this,MyPage.class));
        }
    };
    View.OnClickListener Listen_Chatting=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SelectMenu.this,Chatting_Main.class));
        }
    };
}
