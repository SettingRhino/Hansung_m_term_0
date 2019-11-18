package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import seven.hansung.nonamed.utility.PostInfo;

public class PostView  extends AppCompatActivity {
    protected TextView tx_view_board_name;
    protected TextView tx_view_post_no;
    protected TextView tx_view_post_owner;
    protected TextView tx_view_post_time;
    protected TextView tx_view_post_content;
    protected PostInfo test;
    protected LinearLayout layout_comment;
    protected LinearLayout comment_frame;
    protected TextView commentno;
    protected TextView commentcontent;
    protected TextView commentowner;
    protected EditText tx_comment_write;
    protected CheckBox comment_noname;
    protected Button bt_comment_ok;
    //String username;

    String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);
        Intent intent=getIntent();
        //username=intent.getStringExtra("username");
        init();
    }
    protected void  init(){
        tx_view_board_name=findViewById(R.id.tx_view_board_name);
        tx_view_post_no=findViewById(R.id.tx_view_post_no);
        tx_view_post_owner=findViewById(R.id.tx_view_post_owner);
        tx_view_post_time=findViewById(R.id.tx_view_post_time);
        tx_view_post_content=findViewById(R.id.tx_view_post_content);
        content="1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"
        +"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n"+"1.\n";
        test=new PostInfo("게시판0","999","true","0000-00-00","wsadaw",content);

        tx_view_board_name.setText(test.getBoard_name());
        tx_view_post_no.setText(test.getPost_no());
        if(test.getNoname().equals("true"))
             tx_view_post_owner.setText("익명");
        else
            tx_view_post_owner.setText(test.getPost_owner());
        tx_view_post_time.setText(test.getPost_time());
        tx_view_post_content.setText(test.getPost_content());
        //






        layout_comment=findViewById(R.id.layout_comment);
        //여러개 반복구간
        LinearLayout.LayoutParams comment_no_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
        LinearLayout.LayoutParams comment_comment_param=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,5f);

        comment_frame=new LinearLayout(getApplicationContext());
        comment_frame.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        comment_frame.setOrientation(LinearLayout.HORIZONTAL);

        commentno=new TextView(this);
        commentno.setText("no"+0+"");
        commentno.setTextSize(15);
        commentno.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentno.setLayoutParams(comment_no_param);

        commentcontent=new TextView(this);
        commentcontent.setText("글내용"+0+"");
        commentcontent.setTextSize(15);
        commentcontent.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentcontent.setLayoutParams(comment_comment_param);

        commentowner=new TextView(this);
        commentowner.setText("작성자"+0+"");
        commentowner.setTextSize(15);
        commentowner.setGravity(View.TEXT_ALIGNMENT_CENTER);
        commentowner.setLayoutParams(comment_no_param);

        comment_frame.addView(commentno);
        comment_frame.addView(commentcontent);
        comment_frame.addView(commentowner);

        layout_comment.addView(comment_frame);
        //여러개 반복구간끝

        bt_comment_ok=findViewById(R.id.bt_comment_ok);
        bt_comment_ok.setOnClickListener(listen_comment_ok);


    }

    View.OnClickListener listen_comment_ok=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          //게시글넘버,익명여부,작성자,시간,내용을 저장함.
          //일단 테스트 내용.익명여부만 체크
            comment_noname=findViewById(R.id.comment_noname);
            tx_comment_write=findViewById(R.id.tx_comment_write);
            String noname;
            if(comment_noname.isChecked())
                noname="true";
            else
                noname="false";
            Toast.makeText(getApplicationContext(),"익명체크:"+noname+" 글내용: "+tx_comment_write.getText().toString(),Toast.LENGTH_SHORT).show();
        };
    };
}