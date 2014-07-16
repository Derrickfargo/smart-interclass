package cn.com.incito.classroom.vo;

import cn.com.incito.classroom.utils.Utils;

public class Customer {

    private String id;
    private String nickName;
    private String imgUrl;

    public Customer(String id, String nickName, String imgUrl) {
        super();
        this.id = id;
        this.nickName = nickName;
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.id);
        sb.append(Utils.MARK);
        sb.append(this.nickName);
        sb.append(Utils.MARK);
        sb.append(this.imgUrl);
        return sb.toString();
    }
}
