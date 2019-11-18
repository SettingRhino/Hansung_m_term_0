package seven.hansung.nonamed.utility;

public class PostInfo {
    private String board_name;
    private String post_no;
    private String noname;
    private String post_time;
    private String post_owner;
    private String post_content;

    public PostInfo(String board_name, String post_no, String noname, String post_time, String post_owner, String post_content) {
        this.board_name = board_name;
        this.post_no = post_no;
        this.noname = noname;
        this.post_time = post_time;
        this.post_owner = post_owner;
        this.post_content = post_content;
    }

    public String getBoard_name() {
        return board_name;
    }

    public String getPost_no() {
        return post_no;
    }

    public String getNoname() {
        return noname;
    }

    public String getPost_time() {
        return post_time;
    }

    public String getPost_owner() {
        return post_owner;
    }

    public String getPost_content() {
        return post_content;
    }
}
