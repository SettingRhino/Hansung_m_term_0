package seven.hansung.nonamed.utility;

public class board_0back {
    /*
    public class Board_0 extends AppCompatActivity {
    //파이어베이스 참조객체 필요한것?-> board->해당 카테고리.
    DatabaseReference board_categoryreq;
    public enum PageMove {
        PRE5, PRE, NEXT, NEXT5
    }
    static int pageflag=0;
    //동적액티비티를 위한 리스트
    protected ArrayList<LinearLayout> postcontainerlist;
    protected ArrayList<TextView> postnolist;
    protected ArrayList<TextView> posttitlelist;
    protected ArrayList<TextView> postownerlist;
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
    ArrayAdapter<CharSequence> spinerAdapter;
    //
    TextView categorytitle;
    String searchspinner;

    String categoryname;
    Boolean postOK = false;
    int postcount;
    ImageButton nextpagebtn;
    ImageButton prepagebtn;
    ImageButton next5pagebtn;
    ImageButton pre5pagebtn;
    TextView pagenavi;
    int totalpage;
    int curpage;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_base);
        intent = getIntent();

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
    protected void onResume() {
        super.onResume();

        //board-category 참조 가져옴
        board_categoryreq = FirebaseDatabase.getInstance().getReference().child("board").child(categoryname).getRef();
        board_categoryreq.addListenerForSingleValueEvent(getpostlist);
        //글쓰기 후 다시 돌아올경우.
        if (intent.getBooleanExtra("postOK", postOK)) {
            Toast.makeText(this, "글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
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
        spinnercraet = new Spinnercreat();
        spinner = findViewById(R.id.board_search_spinner);
        spinner.setAdapter(spinerAdapter);
        spinner.setOnItemSelectedListener(Spinner_select);
        bt_post_search.setOnClickListener(listen_post_search);
        spinerAdapter = spinnercraet.creatAdapter(spinerAdapter, R.array.board_search_spinner, this);
    //페이지 이동
        nextpagebtn.setOnClickListener(pagemovebtnlisten);
        next5pagebtn.setOnClickListener(pagemovebtnlisten);
        prepagebtn.setOnClickListener(pagemovebtnlisten);
        pre5pagebtn.setOnClickListener(pagemovebtnlisten);
    }
    //이하 리스너 혹은 다른 함수

    //동적 뷰 생성.
    protected void creatactive(ArrayList<Board_post> boardnumlist) {
        postcount = boardnumlist.size()-1;
        if(boardnumlist.size()%10>0){
            totalpage=(boardnumlist.size()/10)+1;
            curpage=(totalpage-((postcount/10)+1))+1;
        }
        else{
            totalpage=boardnumlist.size()/10;
            curpage=(totalpage-(postcount/10));
        }
        pagenavi.setText("cur:"+curpage+"total:"+totalpage);
        //갯수는 10개를 만든다.
        postcontainerlist = new ArrayList<LinearLayout>();
        postnolist = new ArrayList<TextView>();
        posttitlelist = new ArrayList<TextView>();
        postownerlist = new ArrayList<TextView>();
        for (int i = 0; i < 10; i++) {
            postcontainer = findViewById(R.id.post0 + i);
            postcontainer.removeAllViews();
            //글번호
            postno = new TextView(this);

            postno.setTextSize(15);
            postno.setGravity(View.TEXT_ALIGNMENT_CENTER);
            postno.setLayoutParams(postnoparam);
            //글제목
            posttitle = new TextView(this);

            posttitle.setTextSize(15);
            posttitle.setLayoutParams(posttitleparam);
            //글작성자
            postowner = new TextView(this);
            postowner.setGravity(View.TEXT_ALIGNMENT_VIEW_END);

            postowner.setLayoutParams(postownerparam);

            if (postcount >=i){
                if(boardnumlist.get(i)!=null) {
                    postno.setText(boardnumlist.get(postcount - i).getPostnum());
                    posttitle.setText(boardnumlist.get(postcount - i).getPosttitle());
                    postowner.setText(boardnumlist.get(postcount - i).getPostowner());
                }
                else{
                    postno.setText("");
                    posttitle.setText("");
                    postowner.setText("");
                }
            }
            else{
                postno.setText("");
                posttitle.setText("");
                postowner.setText("");
            }

            //
            //컨테이너 추가부분.
            postcontainer.addView(postno);
            postcontainer.addView(posttitle);
            postcontainer.addView(postowner);
            postcontainer.setOnClickListener(listen_post);

            postnolist.add(postno);
            posttitlelist.add(posttitle);
            postownerlist.add(postowner);
            postcontainerlist.add(postcontainer);
        }
    }

    //동적뷰 수정(페이지 이동시)
    protected void updateactive(ArrayList<Board_post> boardnumlist,PageMove MOVE) {
        //PageMove PRE5, PRE, NEXT, NEXT5

        if(MOVE==PageMove.NEXT){
        postcount=postcount-10;
        if(postcount>=0)
            for (int i = 0; i < 10; i++) {
                if(postcount - i>=0){
                    postnolist.get(i).setText(boardnumlist.get(postcount - i).getPostnum());
                    posttitlelist.get(i).setText(boardnumlist.get(postcount - i).getPosttitle());
                    postownerlist.get(i).setText(boardnumlist.get(postcount - i).getPostowner());
                }
                else{
                    postnolist.get(i).setText("");
                    posttitlelist.get(i).setText("");
                    postownerlist.get(i).setText("");
                }
             }
        else{
            Toast.makeText(getApplicationContext(),"마지막페이지입니다.",Toast.LENGTH_SHORT).show();
            postcount=postcount+10;
        }
        }
        if(MOVE==PageMove.PRE){
            postcount=postcount+10;
            if(postcount<boardnumlist.size())
                for (int i = 0; i < 10; i++) {
                    //if(postcount<boardnumlist.size()) {
                        postnolist.get(i).setText(boardnumlist.get(postcount - i).getPostnum());
                        posttitlelist.get(i).setText(boardnumlist.get(postcount - i).getPosttitle());
                        postownerlist.get(i).setText(boardnumlist.get(postcount - i).getPostowner());
                   // }
                    //else{

                    //}
                }
            else{
                postcount=postcount-10;
                Toast.makeText(getApplicationContext(),"마지막페이지입니다.",Toast.LENGTH_SHORT).show();
            }
        }
        if(MOVE==PageMove.NEXT5){
            postcount=postcount-50;
            if(postcount>=0)
                for (int i = 0; i < 10; i++) {
                    if(postcount - i>=0) {
                        postnolist.get(i).setText(boardnumlist.get(postcount - i).getPostnum());
                        posttitlelist.get(i).setText(boardnumlist.get(postcount - i).getPosttitle());
                        postownerlist.get(i).setText(boardnumlist.get(postcount - i).getPostowner());
                    }
                    else{
                        postnolist.get(i).setText("");
                        posttitlelist.get(i).setText("");
                        postownerlist.get(i).setText("");
                    }
                }
            else{
                Toast.makeText(getApplicationContext(),"존재하지 않는 페이지입니다.",Toast.LENGTH_SHORT).show();
                postcount=postcount+50;
            }
        }
        if(MOVE==PageMove.PRE5){
            postcount=postcount+50;
            if(postcount<boardnumlist.size())
                for (int i = 0; i < 10; i++) {
                    //if(postcount<boardnumlist.size()){
                    postnolist.get(i).setText(boardnumlist.get(postcount - i).getPostnum());
                    posttitlelist.get(i).setText(boardnumlist.get(postcount - i).getPosttitle());
                    postownerlist.get(i).setText(boardnumlist.get(postcount - i).getPostowner());
                    //}
                    //else{

                    //}
                }
            else{
                Toast.makeText(getApplicationContext(),"존재하지 않는 페이지입니다.",Toast.LENGTH_SHORT).show();
                postcount=postcount-50;
                }
        }
        if(boardnumlist.size()%10>0){
            totalpage=(boardnumlist.size()/10)+1;
            curpage=(totalpage-((postcount/10)+1))+1;
        }
        else{
            totalpage=boardnumlist.size()/10;
            curpage=(totalpage-(postcount/10));
        }
        pagenavi.setText("cur:"+curpage+"total:"+totalpage);

        //플래그 다시 돌려주기
        pageflag=0;

    }
    //다음페이지
    View.OnClickListener pagemovebtnlisten=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.equals(nextpagebtn)){
                pageflag=1;
                //board_categoryreq.addValueEventListener(getpostlist);
               // board_categoryreq.addListenerForSingleValueEvent(getpostlist);
            }
            else if(v.equals(prepagebtn)){
                pageflag=-1;
                //board_categoryreq.addValueEventListener(getpostlist);
                //board_categoryreq.addListenerForSingleValueEvent(getpostlist);
            }
            else if(v.equals(next5pagebtn)){
                pageflag=5;
                //board_categoryreq.addValueEventListener(getpostlist);
               //board_categoryreq.addListenerForSingleValueEvent(getpostlist);
            }
            else if(v.equals(pre5pagebtn)){
                pageflag=-5;
                //board_categoryreq.addValueEventListener(getpostlist);
                //board_categoryreq.addListenerForSingleValueEvent(getpostlist);
            }
            board_categoryreq.addListenerForSingleValueEvent(getpostlist);
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
                Intent intent = new Intent(getApplicationContext(), PostView.class);
                //일단 유저넘버랑 카테고리명 넘겨주고
                intent.putExtra("categoryname", categoryname);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),
                        postnolist.get(index).getText().toString() + posttitlelist.get(index).getText().toString() + postownerlist.get(index).getText().toString()
                        , Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "에러 타이틀 인덱스를 못 얻어옴", Toast.LENGTH_SHORT).show();
        }
    };
    //검색버튼 선택시.
    View.OnClickListener listen_post_search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //tx_post_search.getText().toString(); edittext값
            //spinner.getSelectedItem().toString() 스피너 값
            Toast.makeText(getApplicationContext(), tx_post_search.getText().toString() + spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

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

        return super.onOptionsItemSelected(item);
    }

    ValueEventListener getpostlist = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<Board_post> boardnumlist = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Object num = snapshot.child("postnum").getValue(Object.class);
                Object owner = snapshot.child("postowner").getValue(Object.class);
                Object title = snapshot.child("posttitle").getValue(Object.class);
                Object isnoname =snapshot.child("isnoname").getValue(Object.class);
                if(isnoname.toString().equals("true")){
                    boardnumlist.add(new Board_post(num.toString(),title.toString(),"noname"));
                }
                else{
                    boardnumlist.add(new Board_post(num.toString(),title.toString(),owner.toString()));
                }

            }
            if(pageflag==0)
                creatactive(boardnumlist);
            else if(pageflag==1){
                updateactive(boardnumlist,PageMove.NEXT);
            }
            else if(pageflag==-1){
                updateactive(boardnumlist,PageMove.PRE);
            }
            else if(pageflag==5){
                updateactive(boardnumlist,PageMove.NEXT5);
            }
            else if(pageflag==-5){
                updateactive(boardnumlist,PageMove.PRE5);
            }
            else{
                Toast.makeText(getApplicationContext(), "페이지이동설정을 불어오지 못했습니다" + "\n" +
                        "뒤로가기후 시도해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
    */
}
