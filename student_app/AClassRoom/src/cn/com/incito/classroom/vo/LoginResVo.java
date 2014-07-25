package cn.com.incito.classroom.vo;

/**
 * Created by popoy on 2014/7/25.
 */
public class LoginResVo {
    private String groupid;
    private String groupname;
    private String slogan;
    private String logo;
    private String glory;
    private String medal;
    private LoginRes2Vo groupmember;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGlory() {
        return glory;
    }

    public void setGlory(String glory) {
        this.glory = glory;
    }

    public String getMedal() {
        return medal;
    }

    public void setMedal(String medal) {
        this.medal = medal;
    }

    public LoginRes2Vo getGroupmember() {
        return groupmember;
    }

    public void setGroupmember(LoginRes2Vo groupmember) {
        this.groupmember = groupmember;
    }
}
