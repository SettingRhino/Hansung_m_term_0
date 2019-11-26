package seven.hansung.nonamed;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
    StorageReference storageReference;
    StorageReference imageref;
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

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("characterimage");

        imageref=storageRef.child("character0.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into((ImageView) character0);

        imageref=storageRef.child("character1.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character1);
        imageref=storageRef.child("character2.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character2);
        imageref=storageRef.child("character3.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character3);
        imageref=storageRef.child("character4.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character4);
        imageref=storageRef.child("character5.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character5);
        imageref=storageRef.child("character6.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character6);
        imageref=storageRef.child("character7.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character7);
        imageref=storageRef.child("character8.jpg");
        Glide.with(getApplicationContext() ).load(imageref).into(character8);

    }
}
