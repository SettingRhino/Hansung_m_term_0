package seven.hansung.nonamed;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Board_0 extends AppCompatActivity {
    protected ArrayList<LinearLayout> postcontainerlist =new ArrayList<LinearLayout>();
    protected ArrayList<TextView> postnolist=new ArrayList<TextView>();
    protected  ArrayList<TextView> posttitlelisst=new ArrayList<TextView>();
    protected  ArrayList<TextView> postownerlist=new ArrayList<TextView>();
    protected LinearLayout postcontainer;
    protected TextView postno;
    protected TextView posttitle;
    protected TextView postowner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_0);
        init();
    }
    protected void init(){
        LinearLayout.LayoutParams postnoparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f);
        LinearLayout.LayoutParams posttitleparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,10f);
        LinearLayout.LayoutParams postownerparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.5f);
        for(int i=0;i<10;i++) {
            postcontainer=findViewById(R.id.post0+i);


            postno=new TextView(this);
            postno.setText("no"+i+"");
            postno.setTextSize(15);
            postno.setGravity(View.TEXT_ALIGNMENT_CENTER);
            postno.setLayoutParams(postnoparam);

            posttitle=new TextView(this);
            posttitle.setText("제목은 :"+i+"번째");
            posttitle.setTextSize(15);
            posttitle.setLayoutParams(posttitleparam);

            postowner=new TextView(this);

            postowner.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
            postowner.setText(""+i+" "+"작성자");
            postowner.setLayoutParams(postownerparam);

            postcontainer.addView(postno);
            postcontainer.addView(posttitle);
            postcontainer.addView(postowner);

            postnolist.add(postno);
            posttitlelisst.add(posttitle);
            postownerlist.add(postowner);
            postcontainerlist.add(postcontainer);
        }

    }
}
