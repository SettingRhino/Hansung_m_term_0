package seven.hansung.nonamed;

public class Logincontroller {
    UserInfo accessUser;
    UserInfo firebaseUser;
    Byte Logincode;//0:id!=id //1:id==id ->pw==pw//2:id==id ->pw!=pw

    Logincontroller(UserInfo accessUser, UserInfo firebaseUser){
        this.accessUser=accessUser;
        this.firebaseUser=firebaseUser;
    }
    public Byte checkLogin(){
        if(accessUser.getId_info().equals(firebaseUser.getId_info())){
            if(accessUser.getPw_info().equals(firebaseUser.getPw_info())){
                Logincode=1;
                return Logincode;
            }
            else{
                Logincode=2;
                return Logincode;
            }
        }
        else {
            Logincode=0;
            return Logincode;
        }
    }
}
