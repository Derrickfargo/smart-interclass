package cn.com.incito.classroom.vo;

/**
 * Created by JOHN on 2014/7/21.
 */
public class GroupNumberRes2Vo {
    private String membername;
    private String membernumber;
    private String membergender;
    private boolean isLogon = false;

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getMembernumber() {
        return membernumber;
    }

    public void setMembernumber(String membernumber) {
        this.membernumber = membernumber;
    }

    public String getMembergender() {
        return membergender;
    }

    public void setMembergender(String membergender) {
        this.membergender = membergender;
    }

    public boolean isLogon() {
        return isLogon;
    }

    public void setLogon(boolean isLogon) {
        this.isLogon = isLogon;
    }
}
