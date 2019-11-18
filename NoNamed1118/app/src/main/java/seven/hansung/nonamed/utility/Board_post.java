package seven.hansung.nonamed.utility;


//게시글 리스트에 쓸 데이터형
public class Board_post {
    private String postnum;
    private String posttitle;
    private String postowner;

    public String getPostnum() {
        return postnum;
    }

    public String getPosttitle() {
        return posttitle;
    }

    public String getPostowner() {
        return postowner;
    }

    public Board_post(String postnum, String posttitle, String postowner) {
        this.postnum = postnum;
        this.posttitle = posttitle;
        this.postowner = postowner;
    }
}
