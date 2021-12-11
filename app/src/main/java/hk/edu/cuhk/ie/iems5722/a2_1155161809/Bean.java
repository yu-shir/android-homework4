package hk.edu.cuhk.ie.iems5722.a2_1155161809;

public class Bean {
    private String text; //button里面的内容
    private int id;

    public Bean(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
