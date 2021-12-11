package hk.edu.cuhk.ie.iems5722.a2_1155161809;

public class info_mess {
    private String user; //发送人姓名
    private int user_id; //发送人id
    private String content; //消息内容
    private String time; //发送时间
    private boolean self; //判断发送人

    public info_mess(String user, int user_id, String content, String time, boolean self) {
        this.user = user;
        this.user_id = user_id;
        this.content = content;
        this.time = time;
        this.self = self;
    }

    public boolean isSelf() {
        return self;
    }

    public void setSelf(boolean self) {
        this.self = self;
    }

    public info_mess(){}

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
