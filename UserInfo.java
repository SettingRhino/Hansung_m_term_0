package seven.hansung.nonamed;

public class UserInfo {
    private String id_info;
    private String pw_info;
    public String getId_info() {
        return id_info;
    }
    public String getPw_info() {
        return pw_info;
    }

    UserInfo(String id_info, String pw_info){
        this.id_info=id_info;
        this.pw_info=pw_info;
    }
}
