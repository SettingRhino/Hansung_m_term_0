package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Chatting_Main extends AppCompatActivity {
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_main);
        Intent intent=getIntent();
        username=intent.getStringExtra("username");
    }
}
