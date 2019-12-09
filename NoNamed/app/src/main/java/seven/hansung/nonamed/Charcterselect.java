package seven.hansung.nonamed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import seven.hansung.nonamed.mypage.MyPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Charcterselect extends AppCompatActivity {
    ImageView character0;
    ImageView character1;
    ImageView character2;
    ImageView character3;
    ImageView character4;
    ImageView character5;
    ImageView character6;
    ImageView character7;
    ImageView character8;
    StorageReference imageref;

    String uid;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charcterselect);
        character0=findViewById(R.id.character0);
        character1=findViewById(R.id.character1);
        character2=findViewById(R.id.character2);
        character3=findViewById(R.id.character3);
        character4=findViewById(R.id.character4);
        character5=findViewById(R.id.character5);
        character6=findViewById(R.id.character6);
        character7=findViewById(R.id.character7);
        character8=findViewById(R.id.character8);

        character0.setOnClickListener(CListen);
        character1.setOnClickListener(CListen);
        character2.setOnClickListener(CListen);
        character3.setOnClickListener(CListen);
        character4.setOnClickListener(CListen);
        character5.setOnClickListener(CListen);
        character6.setOnClickListener(CListen);
        character7.setOnClickListener(CListen);
        character8.setOnClickListener(CListen);

        Intent intent = getIntent();
        uid=intent.getStringExtra("uid");
        email=intent.getStringExtra("email");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("characterimage");

        imageref=storageRef.child("avatar0.png");
        Glide.with(getApplicationContext() ).load(imageref).into((ImageView) character0);

        imageref=storageRef.child("avatar1.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character1);
        imageref=storageRef.child("avatar2.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character2);
        imageref=storageRef.child("avatar3.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character3);
        imageref=storageRef.child("avatar4.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character4);
        imageref=storageRef.child("avatar5.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character5);
        imageref=storageRef.child("avatar6.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character6);
        imageref=storageRef.child("avatar7.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character7);
        imageref=storageRef.child("avatar8.png");
        Glide.with(getApplicationContext() ).load(imageref).into(character8);
    }

    View.OnClickListener CListen = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            DatabaseReference avatarref = FirebaseDatabase.getInstance().getReference().child("user").getRef();
            avatarref.orderByChild("uid").equalTo(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        v.getContentDescription();
                        snapshot.child("avatar").getRef().setValue(v.getContentDescription());
                    }
                    Intent boardintent=new Intent(getApplicationContext(), MyPage.class);
                    boardintent.putExtra("email",email);
                    boardintent.putExtra("uid",uid);
                    startActivity(boardintent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    };
}
