package seven.hansung.nonamed.utility;

public class Comment_Info {
    public String commentisnonamed;
    public String commentowneruid;
    public String content;
    public String creattime;
    public String postnum;
    public String category;

    public Comment_Info() {
    }

    public Comment_Info(String commentisnonamed, String commentowneruid, String content, String creattime, String postnum,String category) {
        this.commentisnonamed = commentisnonamed;
        this.commentowneruid = commentowneruid;
        this.content = content;
        this.creattime = creattime;
        this.postnum = postnum;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public String getCommentisnonamed() {
        return commentisnonamed;
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
