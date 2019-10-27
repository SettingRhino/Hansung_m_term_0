package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BoardCategory extends AppCompatActivity {
    protected TextView tx_boardcategory00;
    protected TextView tx_boardcategory01;
    protected TextView tx_boardcategory02;
    protected TextView tx_boardcategory03;
    protected TextView tx_boardcategory04;
    protected TextView tx_boardcategory05;
    protected TextView tx_boardcategory06;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boardcategory);
        init();
    }
    protected void init(){
        tx_boardcategory00=findViewById(R.id.tx_boardcategory00);
        tx_boardcategory01=findViewById(R.id.tx_boardcategory01);
        tx_boardcategory02=findViewById(R.id.tx_boardcategory02);
        tx_boardcategory03=findViewById(R.id.tx_boardcategory03);
        tx_boardcategory04=findViewById(R.id.tx_boardcategory04);
        tx_boardcategory05=findViewById(R.id.tx_boardcategory05);
        tx_boardcategory06=findViewById(R.id.tx_boardcategory06);
        tx_boardcategory00.setOnClickListener(Listen_boardcategory00);

    }
    View.OnClickListener Listen_boardcategory00=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(BoardCategory.this,Board_0.class));
        }
    };
}
