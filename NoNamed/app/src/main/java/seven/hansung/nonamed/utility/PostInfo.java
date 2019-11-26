package seven.hansung.nonamed.utility;

public class PostInfo {
    public String category;
    public String content;
    public String creattime;
    public String isnoname;
    public String postnum;
    public String postowneruid;
    public String posttitle;

    public PostInfo() {
        this.category = "";
        this.content = "";
        this.creattime = "";
        this.isnoname = "";
        this.postnum = "";
        this.postowneruid = "";
        this.posttitle = "";
    }

    public PostInfo(String category, String content, String creattime, String isnoname, String postnum, String postowneruid, String posttitle) {
        this.category = category;
        this.content = content;
        this.creattime = creattime;
        this.isnoname = isnoname;
        this.postnum = postnum;
        this.postowneruid = postowneruid;
        this.posttitle = posttitle;
    }

    public String getPostowneruid() {
        return postowneruid;
    }

    public String getCategoy() {
        return category;
    }
    public String getContent() {
        return content;
    }
    public String getCreattime() {
        return creattime;
    }
    public String getIsnoname() {
        return isnoname;
    }
    public String getPostnum() {
        return postnum;
    }
    public String getPosttitle() {
        return posttitle;
    }
}
