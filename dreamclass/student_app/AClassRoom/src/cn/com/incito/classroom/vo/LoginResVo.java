package cn.com.incito.classroom.vo;

import java.util.List;

/**
 * Created by popoy on 2014/7/25.
 */
public class LoginResVo {
    private String id;
    private String name;
    private String slogan;
    private String logo;
    private String glory;
    private String medal;
    private List<LoginRes2Vo> students;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<LoginRes2Vo> getStudents() {
        return students;
    }

    public void setStudents(List<LoginRes2Vo> students) {
        this.students = students;
    }
}
