package seven.hansung.nonamed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BoardCategory extends AppCompatActivity {
    DatabaseReference database;
    DatabaseReference mcategoryRef;
    LinearLayout categorymainframe;
    TextView categorytext;
    ArrayList<String> categorylist;
    String uid="";
    String email="";
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardcategory);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        email=intent.getStringExtra("email");
        database = FirebaseDatabase.getInstance().getReference();
        mcategoryRef = database.child("category");
        //카테고리 값 가져옴 categorylist
        init();
    }
    protected void init(){
        mcategoryRef.addListenerForSingleValueEvent(getcategory);
        //여러개 반복구간끝
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent(this,SelectMenu.class);
        //유저네임을 보내줌.X
        intent.putExtra("uid",uid);
        intent.putExtra("email",email);
        //intent.putExtra("username",username);
        startActivity(intent);
    }
    View.OnClickListener Listen_boardcategory00=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView categoryitem=(TextView) v;
            String categoryname=categoryitem.getText().toString();
            //Toast.makeText(BoardCategory.this, categoryname, Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(BoardCategory.this,Board_0.class);
            //유저네임을 보내줌.X
            intent.putExtra("uid",uid);
            intent.putExtra("email",email);
            intent.putExtra("categoryname",categoryname);
            //intent.putExtra("username",username);
            startActivity(intent);
        }
    };
    ValueEventListener getcategory=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            categorylist=new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Object str=snapshot.getValue(Object.class);
                categorylist.add(str.toString());
            }
            categorymainframe=findViewById(R.id.categorymainframe);
            LinearLayout.LayoutParams categoryparam=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for(int i=0;i<categorylist.size();i++){
                categorytext=new TextView(getApplicationContext());
                categorytext.setText(categorylist.get(i));
                categorytext.setTextSize(35);
                categorytext.setGravity(Gravity.CENTER_HORIZONTAL);
                categorytext.setLayoutParams(categoryparam);
                categorytext.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.textviewbottomsolid));
                categorytext.setOnClickListener(Listen_boardcategory00);

                categorymainframe.addView(categorytext);
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
