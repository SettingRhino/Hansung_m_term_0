#### EOM-TJ-  
##### 1127_0 
##### 회원가입시 중복검사후 텍스트 변경시 캐치 못하는 문제 수정.-> 텍스트 수정시 중복검사 다시해야함
##### 레이아웃 프로토타입용 변경진행중[이미지 삽입 예정//] 
#### -EOM-TJ  
#### EOM-TJ-  
##### useruid를 이용한 자료 접근방법으로 바꾸면서 생긴 버그들 수정.
##### 이미지 테스트 임시의 클래스 생성[마이페이지 시에 사용하고 제거요망] 
##### storage사용을 위한 의존성 추가 수정.
#### -EOM-TJ  
#### EOM-TJ-  
##### 구현할것- 가로세로 안정화.가로세로에 따라 액티비티의 디자인을 바꿔야 할것은 없는지 고려중.
##### 게시글 검색의 글번호 검색기능 삭제 예정[불필요]
##### 금지아이디설정
##### 프로필이미지를 위한 파이어베이스스토리지 설정.
##### 소스코드 간략화
#### -EOM-TJ  
#### EOM-TJ-  
##### 1121_0
##### 포스트 접근방식을 nickname이 아닌 uid로 바꿈
##### 글,코멘트의 쓰기, 수정 삭제까지 변경완료. 
#### -EOM-TJ  
##  
#### EOM-TJ-  
##### 1120_0  
##### 1.게시글 수정, 삭제 기능 추가[본인의 것만 추후 관리자에게도 권한 부여할 예정]  
##### 2.덧글 수정 삭제가능[본인의 것만 추후 관리자에게도 권한 부여할 예정]  
##### 3.글번호를 category별로 변경.[검색부분 수정 예정]  
##### 4.postview에서 현재 로그인 된 유저를 받아옴[데이터를 쓰는 작업을 하는곳은 대부분 로그인 확인 작업 거칠 예정]  
##### 5.3번의 영향으로 comment구조 변경(category추가)  
##### 6.구현예정-README.md정리 예정.  
#### -EOM-TJ  
##  
#### EOM-TJ-  
##### 1119_4  
##### 1.게시판에서 글 선택시 해당 글 불러오기기능  
##### 2.덧글 기능[등록 글에 띄우기](아직 익명기능 x:구현예정)  
##### 3.회원가입시 UID값 저장.  
#### -EOM-TJ  
##  
#### EOM-TJ-  
##### 1119_3  
##### 1.페이지 1으로 가는 아이콘 변경  
##### 2.인텐트설정 수정[뒤로 가기버튼액션 지정하여 액티비티 이동 안정화.]  
#### -EOM-TJ  
##
#### EOM-TJ-  
##### branch-firebaseconnect branch  
##### 1119_2  
##### 1. 1119의 페이지이동 기능의 또다른 오류 발견 10개이하의 게시판 들어가면 페이지이동이 기능이 한번에 작동하지 않음[함수내 순서 조건 추가로 해결]  
##### 2.검색기능 추가.[글작성자,글제목,번호등 검색가능-글작성자 검색시 익명 게시글은 검색되지 않음]  
#### -EOM-TJ  
##  
#### EOM-TJ-  
##### branch-firebaseconnect branch  
##### -1119  
##### 1. 11.18의 4번 오류 해결 페이지이동 원할히 됩니다.  
##### 2.검색기능 추가예정  
#### -EOM-TJ  
##  
##### branch-firebaseconnect branch  
#### EOM-TJ-commit memo:11.18_0  
##### 기능구현-  
##### 1.회원가입부분을 전반적으로 수정함.  
##### 	1-1.기존방식- 회원가입을 위해 입력해야할 사항(id,password,email)  
##### 	1-2.변경방식- 회원가입을 위해 입력해야할 사항(nickname)  
##### 		구글의 Auth서비스를 이용하여, 로그인하고 어플리케이션에서는 nickname만을 설정하게끔 변경  
##### 		회원 가입액티비티에서 중복검사후 회원가입 시에 firebase에 아이디를 등록하면서 DB에 회원 정보를 등록시킴.  
##### 	1-3.추가예정-nickname의 글자, 제한 nickname,등 등록하여 필터링할 예정.  
#####   
##### 2.회원 가입-변경에따른 mainactivity수정  
##### 	1.로그인 정보 입력 TextView2개,로그인버튼,비밀번호찾기 view삭제.  
##### 		-등록이 된 회원은 바로 구글아이디로 자동로그인되어 바로 이용가능.->로그인 정보 입력 TextView2개,로그인버튼 삭제  
##### 		-비밀번호는 수집하지 않음[구글아이디 곧 이 어플의 아이디.]->비밀번호찾기 삭제.  
##### 		-빈 공간에 인삿말추가  
#####  
##### 3.Intent의 Extra값 수정- firebaseAuth의 기능 이용으로 불안정하던 username의 값제거.  
#####  
##### 4.category별 게시판 내 오류 발견.  
##### 	오류내용: 게시판을 이용하는 중 게시글 데이터 삭제시 페이지 이동이 되지 않음  
##### 		-예상원인 현재 페이지와 총페이지의 설정이 액티비티 초기에 고정되어 생기는 문제.  
##### 		-페이지 이동 혹은 아래 데이터 변경시 페이지 변수를 설정하는 식으로 변경예정.  
#### -EOM-TJ  
##### 추가사항은 계속 적어주세요.  
##  
>>>>>>>
####  Hansung_m_term_0
#### 고급모바일소프트웨어 텀프로젝트.
#### 이범학, 최진규, 엄태준 참여.
#### 텀프로젝트 계획:  firebase를 이용한 익명의 게시판어플
####                   덧글, 혹은 채팅(or익명쪽지)을 연계
####                   게시판 외의 추가적인 기능[계획중]
####                   카테고리의 구분으로 게시판 분화.
####                   오픈소스의 사용은 최소화.
####                   
#### 사용언어:java & kotlin
####          kotlin위주의 작업으로 진행예정중 잠깐
#### 
