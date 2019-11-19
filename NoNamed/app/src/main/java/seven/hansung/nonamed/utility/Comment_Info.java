package seven.hansung.nonamed.utility;

public class Comment_Info {
    public String commentisnonamed;
    public String commentowner;
    public String commentowneruid;
    public String content;
    public String creattime;
    public String postnum;

    public Comment_Info() {
    }

    public Comment_Info(String commentisnonamed, String commentowner, String commentowneruid, String content, String creattime, String postnum) {
        this.commentisnonamed = commentisnonamed;
        this.commentowner = commentowner;
        this.commentowneruid = commentowneruid;
        this.content = content;
        this.creattime = creattime;
        this.postnum = postnum;
    }

    public String getCommentisnonamed() {
        return commentisnonamed;
    }

    public String getCommentowner() {
        return commentowner;
    }

    public String getCommentowneruid() {
        return commentowneruid;
    }

    public String getContent() {
        return content;
    }

    public String getCreattime() {
        return creattime;
    }

    public String getPostnum() {
        return postnum;
    }
}
