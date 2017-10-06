package app.com.bakatsuki.bakatsuki;


public class UserInformation {

    public static enum ACCTYPE{
        Solider,DOC,CES
    }

    private String uid,email;
    private ACCTYPE accountType;

    public UserInformation() {
    }

    public UserInformation(String uid, String email, ACCTYPE accountType) {
        this.uid = uid;
        this.email = email;
        this.accountType = accountType;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ACCTYPE getAccountType() {
        return accountType;
    }

    public void setAccountType(ACCTYPE accountType) {
        this.accountType = accountType;
    }
}
