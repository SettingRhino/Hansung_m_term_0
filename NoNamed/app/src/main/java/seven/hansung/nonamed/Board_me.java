package seven.hansung.nonamed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import seven.hansung.nonamed.utility.Board_post;
import seven.hansung.nonamed.utility.Spinnercreat;

public class Board_me extends AppCompatActivity {
    //파이어베이스 참조객체 필요한것?-> board->해당 카테고리.
    DatabaseReference board_categoryreq;
    String uid;
    static String email="";
    String intentpostowner;
    String intentpostnum;
    public static int PAGENUM=1;
    public static int PAGEMAXNUM=1;
    public static int SEARCHFLAG=0;
    //동적액티비티를 위한 리스트
    protected ArrayList<LinearLayout> postcontainerlist;
    protected ArrayList<TextView> postnolist;
    protected ArrayList<TextView> posttitlelist;
    protected ArrayList<TextView> postownerlist;
    ArrayList<Board_post> postviewlist;
    LinearLayout.LayoutParams postnoparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.5f);
    LinearLayout.LayoutParams posttitleparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 10f);
    LinearLayout.LayoutParams postownerparam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
    LinearLayout postcontainer;
    TextView postno;
    TextView posttitle;
    TextView postowner;
    //검색기능을 위한 뷰
    protected EditText tx_post_search;
    protected Button bt_post_search;
    protected Spinner spinner;
    protected Spinnercreat spinnercraet;
    static String categoryname;
    ArrayAdapter<CharSequence> spinerAdapter;
    TextView categorytitle;
    String searchspinner;
    ArrayList<TextView> post_text_owner;
    Boolean postOK = false;
    ImageButton nextpagebtn;
    ImageButton prepagebtn;
    ImageButton next5pagebtn;
    ImageButton pre5pagebtn;
    TextView pagenavi;
    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_base);
        post_text_owner=new ArrayList<>();
        intent = getIntent();
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        email=intent.getStringExtra("email");
        categoryname = intent.getStringExtra("categoryname");
        categorytitle = findViewById(R.id.board_title);
        nextpagebtn=findViewById(R.id.nextpagebtn);
        next5pagebtn=findViewById(R.id.next5pagebtn);
        prepagebtn=findViewById(R.id.prepagebtn);
        pre5pagebtn=findViewById(R.id.pre5pagebtn);
        pagenavi=findViewById(R.id.pagenavi);
        tx_post_search = findViewById(R.id.tx_post_search);
        bt_post_search = findViewById(R.id.bt_post_search);


    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(getApplicationContext(),BoardCategory.class));
    }
    @Override
    protected void onResume() {
        super.onResume();
        postviewlist=new ArrayList<>();
        //board-category 참조 가져옴
        board_categoryreq = FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
        board_categoryreq.addValueEventListener(getpostlistten);

        //board_categoryreq.addListenerForSingleValueEvent(getpostlist);
        //글쓰기 후 다시 돌아올경우.
        if (intent.getBooleanExtra("postOK", postOK)) {
            Toast.makeText(this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
        }
        if (intent.getBooleanExtra("modifyOK", postOK)) {
            Toast.makeText(this, "글이 수정되었습니다.", Toast.LENGTH_SHORT).show();
        }
        categorytitle.setText(categoryname);

        init();
    }

    //init()기능
    //카테고리의 글들을 불러온다.->뷰에 배치한다.
    protected void init() {
        //게시글을 불러온다. 동적액티비티임.
        //나중에 따로 클래스로 만들어 관리. [파이어베이스 연결후->연결]
        //데이터를 한번만 가져올까... 아니면 자식 업뎃마다 가져올까..
        //한번만 가져오면 데이터 낭비 적음. 대신 업데이트된 내용을 보려면 액티비티 나가야함.
        //여러번 가져오면 데이터 낭비가 있음.
        //일단 한번 가져오고. 페이지 이동시 변경사항 있으면 다시 가져오는걸로? 업뎃마다 가져오면 너무 무거울듯핟.
        //글이 10개 이하일 경우?그럼 빈칸으로 표시하게끔 하기. 나중에 구현
        //ArrayList<자바클래스>로 담아서 인덱스로 주고 분해해서 하면될듯.
        //동적뷰 생성.

        //creatactive();
        //Toast.makeText(getApplicationContext(),boardnumlist.get(0),Toast.LENGTH_SHORT).show();
        //검색기능

        //페이지 이동
        nextpagebtn.setOnClickListener(pagemovebtnlisten);
        next5pagebtn.setOnClickListener(pagemovebtnlisten);
        prepagebtn.setOnClickListener(pagemovebtnlisten);
        pre5pagebtn.setOnClickListener(pagemovebtnlisten);
        bt_post_search.setOnClickListener(listen_post_search);

        spinner = findViewById(R.id.board_search_spinner);
        spinnercraet = new Spinnercreat();
        spinerAdapter = spinnercraet.creatAdapter(spinerAdapter, R.array.board_search_spinner, this);
        spinner.setAdapter(spinerAdapter);
        spinner.setOnItemSelectedListener(Spinner_select);
    }

    //이하 리스너 혹은 다른 함수
    void creatview(ArrayList<Board_post> postlist){
        postcontainerlist = new ArrayList<LinearLayout>();
        postnolist = new ArrayList<TextView>();
        posttitlelist = new ArrayList<TextView>();
        postownerlist = new ArrayList<TextView>();
        for (int i = 0; i < 10; i++) {
            //연습 닉네임을 이메일로
            postcontainer = findViewById(R.id.post0 + i);
            //postcontainer.removeAllViews();
            //글번호
            postno = findViewById(R.id.postno0+i);

            postno.setTextSize(15);
            postno.setGravity(View.TEXT_ALIGNMENT_CENTER);
            postno.setLayoutParams(postnoparam);
            //글제목
            posttitle = findViewById(R.id.posttitle0+i);
            posttitle.setTextSize(15);
            posttitle.setLayoutParams(posttitleparam);
            //글작성자
            postowner = findViewById(R.id.postowner0+i);
            //post_text_owner.add(postowner);
            // postowner.setGravity(View.TEXT_ALIGNMENT_VIEW_END);
            // postowner.setLayoutParams(postownerparam);

            if(postlist.get(i)!=null) {
                postno.setText(postlist.get(i).getPostnum());
                posttitle.setText(postlist.get(i).getPosttitle());
                postowner.setText("");
            }
            else{
                postno.setText("");
                posttitle.setText("");
                postowner.setText("");
            }

            post_text_owner.add(postowner);
            //
            //컨테이너 추가부분.
            //postcontainer.addView(postno);
            //postcontainer.addView(posttitle);
            //postcontainer.addView(postowner);
            postcontainer.setOnClickListener(listen_post);

            postnolist.add(postno);
            // posttitlelist.add(posttitle);
            postownerlist.add(postowner);
            postcontainerlist.add(postcontainer);
        }
        pagenavi.setTextSize(20);
        pagenavi.setText(PAGENUM+" OF "+PAGEMAXNUM);
        FirebaseDatabase.getInstance().getReference().child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int x=0;x<10;x++){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if(!postviewlist.get(x).getPostowner().equals("noname")){
                            if(postviewlist.get(x).getPostowner().equals(snapshot.child("uid").getValue(Object.class).toString())){
                                post_text_owner.get(x).setText(snapshot.child("nickname").getValue(Object.class).toString());
                            }
                        }
                        else
                            post_text_owner.get(x).setText("noname");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //다음페이지
    View.OnClickListener pagemovebtnlisten=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //버튼마다 할일은 PAGENUM을 조절할뿐.
            if(v.equals(nextpagebtn)){
                if(PAGENUM<PAGEMAXNUM)
                    PAGENUM=PAGENUM+1;
                else{
                    PAGENUM=PAGEMAXNUM;
                }
            }
            else if(v.equals(prepagebtn)){
                if(PAGENUM>1)
                    PAGENUM--;
                else
                    PAGENUM=1;
            }
            else if(v.equals(next5pagebtn)){
                if(PAGENUM+5<PAGEMAXNUM)
                    PAGENUM=PAGENUM+5;
                else{
                    PAGENUM=PAGEMAXNUM;
                }
            }
            else if(v.equals(pre5pagebtn)){
                if(PAGENUM-5>1)
                    PAGENUM=PAGENUM-5;
                else
                    PAGENUM=1;
            }
            board_categoryreq = FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
            if(SEARCHFLAG==0)
                board_categoryreq.addValueEventListener(getpostlistten);
            else
                board_categoryreq.addValueEventListener(getsearchpostlistten);
        }
    };

    //글 선택시.
    View.OnClickListener listen_post = new View.OnClickListener() {
        //LinerLayout자체에 클릭리스너 걸어서 클릭시 글정보 알수 있게
        //나중에 게시글 띄울때 정보 넘겨줌.(글넘버.)
        @Override
        public void onClick(View v) {
            int index = -1;
            for (int i = 0; i < 10; i++) {
                if (v.getId() == postcontainerlist.get(i).getId()) {
                    //똑같은 애다?
                    index = i;
                    break;
                } else
                    continue;
            }
            if (index >= 0) {
                if(!postownerlist.get(index).getText().equals("")){
                    intentpostowner=postownerlist.get(index).getText().toString();
                    intentpostnum=postnolist.get(index).getText().toString();
                    //1.Google로그임옵션 객체를 만든다
                    //client로부터 인텐트를 불러와 엑티비티 시행->사용자의 데이터를 받아올수 있음
                    //결과를 넘겨주면서 activity호출 코드값은 9001
                    Intent intent = new Intent(getApplicationContext(), PostView.class);
                    intent.putExtra("postowner", intentpostowner);
                    intent.putExtra("categoryname", categoryname);
                    intent.putExtra("postnum",intentpostnum);
                    intent.putExtra("email",email);
                    intent.putExtra("uid",uid);
                    startActivity(intent);

                    //글넘버 카테고리명


                /*Toast.makeText(getApplicationContext(),
                        postnolist.get(index).getText().toString() + posttitlelist.get(index).getText().toString() + postownerlist.get(index).getText().toString()
                        , Toast.LENGTH_SHORT).show();*/
                }
            } else
                Toast.makeText(getApplicationContext(), "에러 타이틀 인덱스를 못 얻어옴", Toast.LENGTH_SHORT).show();

        }
    };
    //검색버튼 선택시.
    View.OnClickListener listen_post_search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PAGENUM=1;
            if(spinner.getSelectedItem().toString().equals("글제목")||spinner.getSelectedItem().toString().equals("글번호"))
                FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef().addValueEventListener(getsearchpostlistten);
            else
            {
                FirebaseDatabase.getInstance().getReference().addValueEventListener(getsearchpostlisttenowner);
            }
        }
    };
    //검색스피너 선택 액션
    AdapterView.OnItemSelectedListener Spinner_select = new AdapterView.OnItemSelectedListener() {
        //스피너 선택이 되었을 때
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            searchspinner = spinner.getSelectedItem().toString();
            // 테스트용
            // Toast.makeText(getApplicationContext(),tmp_share,Toast.LENGTH_SHORT).show();
        }

        //스피너 설정 선택이 안되었을 때
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(), "검색설정을 불러오지 못했습니다..." + "\n" +
                    "뒤로가기후 시도해주세요.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boardactionbar, menu);
        return true;
    }

    //글쓰기 액션버튼을 클릭했을때의 동작->글쓰기 액티비티로 전송(username,categoryname)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.board_action_write) {
            Intent intent = new Intent(this, PostWrite.class);

            intent.putExtra("categoryname", categoryname);
            startActivity(intent);
            return true;
        }
        if(id==R.id.board_action_num_one){
            PAGENUM=1;
            FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef().addValueEventListener(getpostlistten);
        }

        return super.onOptionsItemSelected(item);
    }
    ValueEventListener getpostlistten=new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            postviewlist=new ArrayList<>();
            SEARCHFLAG=0;
            int pagenum=PAGENUM;
            int i=0;//맥스페이지 설정
            if((int)dataSnapshot.getChildrenCount()%10>0){
                PAGEMAXNUM=((int)dataSnapshot.getChildrenCount()/10)+1;
            }
            else{
                PAGEMAXNUM=(int)dataSnapshot.getChildrenCount()/10;
            }
            //view재갱신 안되다가 한번에 될경우 혹은 버튼클릭사이에 글이 삭제될경우 PAGENUM이 PAGEMAXNUM을 안넘도록 제한.
            if(PAGENUM>PAGEMAXNUM)
                PAGENUM=PAGEMAXNUM;
            //13개가 있다면?0123456789101112
            // dataSnapshot.getChildrenCount()이 전체 갯수
            //13-0 13-1.......13-9// 2page? 13-10...
            if(dataSnapshot.getChildrenCount()<10){
                PAGENUM=1;
                PAGEMAXNUM=1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Object p_noname=snapshot.child("isnoname").getValue(Object.class);
                    if(p_noname.toString().equals("true")){
                        Object p_n=snapshot.child("postnum").getValue(Object.class);
                        Object p_t=snapshot.child("posttitle").getValue(Object.class);
                        if(p_t==null) p_t="";
                        if(p_n==null) p_n="";
                        postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),"noname"));
                    }
                    else{
                        Object p_n=snapshot.child("postnum").getValue(Object.class);
                        Object p_t=snapshot.child("posttitle").getValue(Object.class);
                        Object p_o=snapshot.child("postowneruid").getValue(Object.class);
                        if(p_t==null) p_t="";
                        if(p_n==null) p_n="";
                        if(p_o==null) p_o="";

                        postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),p_o.toString()));
                    }
                }
            }
            else{
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //해당 페이지의 게시글만 읽겠다.
                    //24 23222120191817161514 //13121110987654//
                    //작은 값(ex14)&&큰값(ex23)
                    if(i>=dataSnapshot.getChildrenCount()-10*(pagenum)&&i<dataSnapshot.getChildrenCount()-10*(pagenum-1)){
                        Object p_noname=snapshot.child("isnoname").getValue(Object.class);
                        if(p_noname.toString().equals("true")){
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),"noname"));
                        }
                        else{
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            Object p_o=snapshot.child("postowneruid").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            if(p_o==null) p_o="";

                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),p_o.toString()));
                        }

                    }
                    //해당 페이지가 아닌 순서는 그냥 null
                    else{

                    }
                    i++;

                }
            }
            //i=12 12
            //순서 바꾸기 1098765...
            Collections.reverse(postviewlist);
            for(int j= postviewlist.size();j<10;j++){
                postviewlist.add(j,new Board_post("","",""));
            }

            creatview(postviewlist);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"글정보를 읽어오지 못했습니다.",Toast.LENGTH_SHORT).show();
            Log.d("databaseError","postdata road fail");
        }
    };
    ValueEventListener getsearchpostlisttenowner=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            TextView v=findViewById(R.id.tx_post_search);
            ArrayList<String> uidlist=new ArrayList<>();
            //포함되는 애들 가져옴
            for (DataSnapshot snapshot : dataSnapshot.child("user").getChildren()) {
                if(snapshot.child("nickname").getValue(Object.class).toString().contains(v.getText().toString()))
                {
                    uidlist.add(snapshot.child("uid").getValue(Object.class).toString());
                }
            }
            for (DataSnapshot snapshot : dataSnapshot.child("board").child(categoryname).getChildren()) {
                for(int h=0;h<uidlist.size();h++){
                    if(snapshot.child("postowneruid").getValue(Object.class).toString().equals(uidlist.get(h))){
                        Object p_noname=snapshot.child("isnoname").getValue(Object.class);
                        if(p_noname.toString().equals("true")){
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),"noname"));
                        }
                        else{
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            Object p_o=snapshot.child("postowneruid").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            if(p_o==null) p_o="";

                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),p_o.toString()));
                        }
                    }
                }
            }
            if(postviewlist.size()%10>0){
                PAGEMAXNUM=postviewlist.size()/10+1;
            }
            else{
                PAGEMAXNUM=postviewlist.size()/10;
            }
            //i=12 12
            Collections.reverse(postviewlist);
            //10이하.
            if(postviewlist.size()<10){
                for(int j= postviewlist.size();j<10;j++){
                    postviewlist.add(j,new Board_post("","",""));
                }
            }
            else {//열개 이상 ->앞 10개만 받자.
                for (int r = 0; r <= ((PAGENUM - 1) * 10 - 1); r++)
                    postviewlist.remove(0);
                //검색의 마지막 페이지일 경우 빈칸 만들기.
                for (int j = postviewlist.size(); j < 10; j++) {
                    postviewlist.add(j, new Board_post("", "", ""));
                }
                //위처럼 하니까 1페이지일때 그냥 다 받아오게됨... 1페이지 일 경우 뒤 제거.
                if (PAGENUM == 1) {
                    for (int t = 10; t <= postviewlist.size(); t++) {
                        postviewlist.remove(10);
                    }
                }

            }
            creatview(postviewlist);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"작성자 정보를 읽어오지 못했습니다.",Toast.LENGTH_SHORT).show();
            Log.d("databaseError","postownerdata road fail");
        }
    };
    ValueEventListener getsearchpostlistten=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            postviewlist=new ArrayList<>();
            SEARCHFLAG=1;
            int i=0;
            //스피너 설정과 입력값 가져오기
            String searchoption="posttitle";
            if(spinner.getSelectedItem().toString().equals("글제목"))
                searchoption="posttitle";
            if(spinner.getSelectedItem().toString().equals("글번호"))
                searchoption="postnum";
            if(spinner.getSelectedItem().toString().equals("작성자"))
                searchoption="postowner";
            EditText searchvaluetx= (EditText)findViewById(R.id.tx_post_search);
            String searchvalue=searchvaluetx.getText().toString();
            //다시생각-> 글작성자 일때만 익명글 검색 하면안됨.
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                if(searchoption.equals("postowner")){//검색조건이 글작성자면//나중에 삭제
                    if(snapshot.child(searchoption).getValue(Object.class).toString().contains(searchvalue)){//검색조건에 포함되면
                        if(snapshot.child("isnoname").getValue(Object.class).toString().equals("false")){//isnoname이 false->익명아닌애들만 검색
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            Object p_o=snapshot.child("postowneruid").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            if(p_o==null) p_o="";

                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),p_o.toString()));}
                    }
                    else{}
                }
                else{//검색조건이 글작성자가 아니면
                    if(snapshot.child(searchoption).getValue(Object.class).toString().contains(searchvalue)){//조건에 포함되는 애들 모두 검색
                        if(snapshot.child("isnoname").getValue(Object.class).toString().equals("true")){//만약 익명의 글이면 noname로 표시
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),"noname"));
                        }
                        else{
                            Object p_n=snapshot.child("postnum").getValue(Object.class);
                            Object p_t=snapshot.child("posttitle").getValue(Object.class);
                            Object p_o=snapshot.child("postowneruid").getValue(Object.class);
                            if(p_t==null) p_t="";
                            if(p_n==null) p_n="";
                            if(p_o==null) p_o="";

                            postviewlist.add(new Board_post(p_n.toString(),p_t.toString(),p_o.toString()));
                        }
                    }
                    else{}
                }
            }
            if(postviewlist.size()%10>0){
                PAGEMAXNUM=postviewlist.size()/10+1;
            }
            else{
                PAGEMAXNUM=postviewlist.size()/10;
            }
            //i=12 12
            Collections.reverse(postviewlist);
            //10이하.
            if(postviewlist.size()<10){
                for(int j= postviewlist.size();j<10;j++){
                    postviewlist.add(j,new Board_post("","",""));
                }
            }
            else{//열개 이상 ->앞 10개만 받자.
                for(int r=0;r<=((PAGENUM-1)*10-1);r++)
                    postviewlist.remove(0);
                //검색의 마지막 페이지일 경우 빈칸 만들기.
                for(int j= postviewlist.size();j<10;j++){
                    postviewlist.add(j,new Board_post("","",""));
                }
                //위처럼 하니까 1페이지일때 그냥 다 받아오게됨... 1페이지 일 경우 뒤 제거.
                if(PAGENUM==1){
                    for(int t=10;t<=postviewlist.size();t++){
                        postviewlist.remove(10);
                    }
                }
            }
            //순서 바꾸기 1098765...

            creatview(postviewlist);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"글정보를 읽어오지 못했습니다.",Toast.LENGTH_SHORT).show();
            Log.d("databaseError","postdata road fail");
        }
    };
}