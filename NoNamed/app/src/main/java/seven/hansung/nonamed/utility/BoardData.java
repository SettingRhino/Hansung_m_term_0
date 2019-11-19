package seven.hansung.nonamed.utility;

public class BoardData {
    public String category;
    public String boardnum;
    public String boardtitle;
    public String boardowner;
    public String isnoname;
    public String creattiem;
    public String content;

    public BoardData() {
    }

    public BoardData(String category,String boardnum, String boardtitle, String boardowner, String isnoname, String creattiem, String content) {
        this.category = category;
        this.boardnum = boardnum;
        this.boardtitle = boardtitle;
        this.boardowner = boardowner;
        this.isnoname = isnoname;
        this.creattiem = creattiem;
        this.content = content;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setBoardnum(String boardnum) {
        this.boardnum = boardnum;
    }

    public void setBoardtitle(String boardtitle) {
        this.boardtitle = boardtitle;
    }

    public void setBoardowner(String boardowner) {
        this.boardowner = boardowner;
    }

    public void setIsnoname(String isnoname) {
        this.isnoname = isnoname;
    }

    public void setCreattiem(String creattiem) {
        this.creattiem = creattiem;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
